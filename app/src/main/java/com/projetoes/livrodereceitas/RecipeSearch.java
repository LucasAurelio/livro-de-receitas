package com.projetoes.livrodereceitas;


public class RecipeSearch {

    private int ingredients;
    private int searchIngradients;
    private Recipe recipe;

    public RecipeSearch(Recipe recipe, int ingredients, int searchIngradients){
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.searchIngradients = searchIngradients;

    }

    public int getIngredients() {
        return ingredients;
    }

    public int getSearchIngradients() {
        return searchIngradients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setIngredients(int ingredients){
        this.ingredients = ingredients;
    }

    public void setSearchIngradients(int searchIngradients) {
        this.searchIngradients = searchIngradients;
    }

    public String ingredientsToString(){
        return String.valueOf(ingredients) + "/" + String.valueOf(searchIngradients);
    }

}
