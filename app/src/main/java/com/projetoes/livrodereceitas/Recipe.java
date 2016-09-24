package com.projetoes.livrodereceitas;


import java.util.ArrayList;

public class Recipe {

    private String name;
    private ArrayList<String> ingredients;
    private String description;
    private boolean isFavorite;
    private boolean isDone;
    private boolean isWannaDo;

    public Recipe(String name){
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
        this.isFavorite = false;
        this.isDone = false;
        this.isWannaDo = false;
    }

    public Recipe(String name, boolean isFavorite, boolean isDone, boolean isWannaDo){
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
        this.isFavorite = isFavorite;
        this.isDone = isDone;
        this.isWannaDo = isWannaDo;
    }

    public Recipe(String name, ArrayList<String> ingredients, String description, boolean isFavorite,
                       boolean isDone, boolean isWannaDo){
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.isFavorite = isFavorite;
        this.isDone = isDone;
        this.isWannaDo = isWannaDo;
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

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isWannaDo() {
        return isWannaDo;
    }

    public void setIsWannaDo(boolean isWannaDo) {
        this.isWannaDo = isWannaDo;
    }

    @Override
    public String toString() {
        return "Receita: " + name + ", favorita: " + String.valueOf(isFavorite) +
                ", Quero fazer: " + String.valueOf(isWannaDo) + ", Já fiz: " + String.valueOf(isDone);
    }
}
