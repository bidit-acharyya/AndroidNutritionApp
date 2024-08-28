package com.example.afinal;

public class SavedRecipes {
    String name;
    String recipeURL;
    public SavedRecipes(String name, String recipeURL){
        this.name = name;
        this.recipeURL = recipeURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }
}
