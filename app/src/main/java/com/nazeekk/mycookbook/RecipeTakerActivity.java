package com.nazeekk.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nazeekk.mycookbook.Models.Recipe;

public class RecipeTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_recipe;
    ImageView imageView_save;
    Recipe recipe;
    boolean isOldRecipe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        editText_title = findViewById(R.id.editText_title);
        editText_recipe = findViewById(R.id.editText_recipe);

        imageView_save = findViewById(R.id.imageView_save);

        recipe = new Recipe();
        try {
            recipe = (Recipe) getIntent().getSerializableExtra("old_recipe");
            editText_title.setText(recipe.getTitle());
            editText_recipe.setText(recipe.getRecipe());
            isOldRecipe = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String description = editText_recipe.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(RecipeTakerActivity.this, "Please, enter description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isOldRecipe) recipe = new Recipe();

                recipe.setTitle(title);
                recipe.setRecipe(description);

                Intent intent = new Intent();
                intent.putExtra("recipe", recipe);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}