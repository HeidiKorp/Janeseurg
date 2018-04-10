package com.example.demo;

import java.io.IOException;

public interface RecipeTemplate {

    String getTitle() throws IOException;
    String getServings() throws IOException;
    String getPrepTime() throws IOException;
    String getIngredients() throws IOException;
    String getInstructions() throws IOException;
}
