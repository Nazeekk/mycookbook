package com.nazeekk.mycookbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nazeekk.mycookbook.Adapter.RecipesListAdapter;
import com.nazeekk.mycookbook.DataBase.RoomDB;
import com.nazeekk.mycookbook.Models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    RecipesListAdapter recipesListAdapter;
    RoomDB database;
    List <Recipe> recipes = new ArrayList<>();
    SearchView searchView_home;
    Recipe selectedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        database = RoomDB.getInstance(this);
        recipes = database.mainDAO().getAll();

        searchView_home = findViewById(R.id.searchView_home);

        updateRecycle(recipes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    //фільтрація нотаток
    private void filter(String newText) {
        List<Recipe> filteredList = new ArrayList<>();
        for (Recipe singleRecipe : recipes) {
            if (singleRecipe.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleRecipe.getRecipe().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleRecipe);
            }
        }
        recipesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Recipe new_recipe = (Recipe) data.getSerializableExtra("recipe");
                database.mainDAO().insert(new_recipe);
                recipes.clear();
                recipes.addAll(database.mainDAO().getAll());
                recipesListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Recipe new_recipe = (Recipe) data.getSerializableExtra("recipe");
                database.mainDAO().update(new_recipe.getID(), new_recipe.getTitle(), new_recipe.getRecipe());
                recipes.clear();
                recipes.addAll(database.mainDAO().getAll());
                recipesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycle(List<Recipe> recipes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recipesListAdapter = new RecipesListAdapter(MainActivity.this, recipes, recipeClickListener);
        recyclerView.setAdapter(recipesListAdapter);
    }

    private final IRecipeClickListener recipeClickListener = new IRecipeClickListener() {
        @Override
        public void onClick(Recipe recipe) {
            Intent intent = new Intent(MainActivity.this, RecipeTakerActivity.class);
            intent.putExtra("old_recipe", recipe);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Recipe recipe, CardView cardView) {
//            selectedNote = new Notes();
            selectedRecipe = recipe;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.pin) {
            database.mainDAO().pin(selectedRecipe.getID(), selectedRecipe.isPinned());
            Toast.makeText(MainActivity.this, selectedRecipe.isPinned() ? "Unpinned" : "Pinned", Toast.LENGTH_SHORT).show();
            recipes.clear();
            recipes.addAll(database.mainDAO().getAll());
            recipesListAdapter.notifyDataSetChanged();
            return true;
        }

        if (item.getItemId() == R.id.delete) {
            database.mainDAO().delete(selectedRecipe);
            recipes.remove(selectedRecipe);
            recipesListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}