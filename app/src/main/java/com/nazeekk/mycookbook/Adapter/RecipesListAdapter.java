package com.nazeekk.mycookbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nazeekk.mycookbook.IRecipeClickListener;
import com.nazeekk.mycookbook.Models.Recipe;
import com.nazeekk.mycookbook.R;

import java.util.List;

public class RecipesListAdapter extends RecyclerView.Adapter <RecipesViewHolder> {

    Context context;
    List<Recipe> list;

    IRecipeClickListener listener;

    public RecipesListAdapter(Context context, List<Recipe> list, IRecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {

        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_recipe.setText(list.get(position).getRecipe());

        //check if
        //
        // isPinned
        holder.imageView_pin.setImageResource(list.get(position).isPinned() ? R.drawable.ic_pin_icon : 0);

        holder.recipe_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)  {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }

        });
        holder.recipe_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.recipe_container);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Recipe> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}

class RecipesViewHolder extends RecyclerView.ViewHolder {

    CardView recipe_container;
    TextView textView_title, textView_recipe;
    ImageView imageView_pin;

    public RecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        recipe_container = itemView.findViewById(R.id.recipe_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_recipe = itemView.findViewById(R.id.textView_recipe);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}
