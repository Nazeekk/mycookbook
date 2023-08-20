package com.nazeekk.mycookbook;

import androidx.cardview.widget.CardView;

import com.nazeekk.mycookbook.Models.Recipe;

public interface IRecipeClickListener {

    void onClick(Recipe recipe);
    void onLongClick(Recipe recipe, CardView cardView);

}
