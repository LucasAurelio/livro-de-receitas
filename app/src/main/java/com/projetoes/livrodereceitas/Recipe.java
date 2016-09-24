package com.projetoes.livrodereceitas;


import java.util.ArrayList;

public class Recipe {

    private String name;
    private ArrayList<String> ingredients;
    private String description;
    private boolean isFavorite;
    private boolean isMade;
    private boolean isWanna;

    public Recipe(String name){
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
        this.isFavorite = false;
        this.isMade = false;
        this.isWanna = false;
    }

    public Recipe(String name, boolean isFavorite, boolean isMade, boolean isWanna){
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
        this.isFavorite = isFavorite;
        this.isMade = isMade;
        this.isWanna = isWanna;
    }

    public Recipe(String name, ArrayList<String> ingredients, String description, boolean isFavorite,
                       boolean isMade, boolean isWanna){
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.isFavorite = isFavorite;
        this.isMade = isMade;
        this.isWanna = isWanna;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isMade() {
        return isMade;
    }

    public void setIsMade(boolean isMade) {
        this.isMade = isMade;
    }

    public boolean isWanna() {
        return isWanna;
    }

    public void setIsWanna(boolean isWanna) {
        this.isWanna = isWanna;
    }

}
