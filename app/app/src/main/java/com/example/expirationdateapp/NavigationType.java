package com.example.expirationdateapp;

enum NavigationType{
    ADD, VIEW, RECIPE, FORUM, FOOD_BANK;

    public static NavigationType getFromButtonId(int id){
        switch (id){
            case R.id.navigationFrag_button_add:
                return ADD;
            case R.id.navigationFrag_button_view:
                return VIEW;
            case R.id.navigationFrag_button_recipe:
                return RECIPE;
            case R.id.navigationFrag_button_forum:
                return FORUM;
            case R.id.navigationFrag_button_food_bank:
                return FOOD_BANK;
            default:
                throw new RuntimeException("Unsupported id");
        }

    }
}
