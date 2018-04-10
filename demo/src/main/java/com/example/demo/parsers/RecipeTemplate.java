package com.example.demo.parsers;

import org.json.JSONException;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public interface RecipeTemplate {

    Document getDocument() throws IOException;
    String getTitle() throws IOException;
    int getServings() throws IOException;
    String getPrepTime() throws IOException;
    String getIngredients() throws IOException, JSONException;
    String getInstructions() throws IOException;
    String getOriginalSource();
    String getOriginalAuthor() throws IOException;
}
