package com.projetoes.livrodereceitas;


public class RecipeSearch {

    private int ingredients;
    private int searchIngradients;
    private String name;

    public RecipeSearch(String name, int ingredients, int searchIngradients){
        this.name = name;
        this.ingredients = ingredients;
        this.searchIngradients = searchIngradients;

    }

    public int getIngredients() {
        return ingredients;
    }

    public int getSearchIngradients() {
        return searchIngradients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(int ingredients){
        this.ingredients = ingredients;
    }

    public void setSearchIngradients(int searchIngradients) {
        this.searchIngradients = searchIngradients;
    }

}
