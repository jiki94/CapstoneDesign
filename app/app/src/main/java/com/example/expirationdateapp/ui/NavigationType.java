package com.example.expirationdateapp.ui;

import com.example.expirationdateapp.R;

public enum NavigationType{
    ADD, VIEW, RECIPE, FORUM, FOOD_BANK;

    public static NavigationType getFromButtonId(int id){
        if (id == R.id.navigationFrag_button_add)
            return ADD;
        if (id == R.id.navigationFrag_button_view)
            return VIEW;
        if (id == R.id.navigationFrag_button_recipe)
            return RECIPE;
        if (id == R.id.navigationFrag_button_forum)
            return FORUM;
        if (id == R.id.navigationFrag_button_food_bank)
            return FOOD_BANK;

        throw new RuntimeException("Unsupported id");
    }
}
