package com.example.demo.foodnetwork;

import com.example.demo.parsers.GeneralIngredientsParser;
import com.example.demo.parsers.RecipeTemplate;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodNetworkParser implements RecipeTemplate {

    private String url;
    private GeneralIngredientsParser ingredientsParser;
    private Document document;

    public FoodNetworkParser(String url) {
        this.url = url;
        ingredientsParser = new GeneralIngredientsParser();
        getDocument();
    }

    @Override
    public Document getDocument() {
        document = new Document("");
            if (url.isEmpty()) {
                System.out.println("Please provide an URL");
            } else {
                try {
                    this.document = Jsoup.connect(url).get();
                } catch (IllegalArgumentException e) {
                    System.out.println("Please provide correct URL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return document;
    }

    @SuppressWarnings("Duplicates")
    public void setDocument(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                this.document = Jsoup.parse(sb.toString());
            } catch (FileNotFoundException e) {
                System.out.println("Could not find a file to read!");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getTitle() {
        return document.select("span.o-AssetTitle__a-HeadlineText").text();
    }

    @Override
    public int getServings() {
        String doubleServings = document.select("section.o-RecipeInfo.o-Yield dl dd.o-RecipeInfo__a-Description").text();
        return Integer.parseInt(Arrays.asList(doubleServings.split(" ", 2)).get(0));
    }

    @Override
    public String getPrepTime() {
        String doubleTime = document.select("dd.o-RecipeInfo__a-Description--Total").text();
        return doubleTime.substring(0, doubleTime.length() / 2);
    }

    @Override
    public String getIngredients() throws JSONException {
        return ingredientsParser.getIngredients(getUnParsedIngreds());
    }

    @Override
    public String getInstructions() {
        StringBuilder instructions = new StringBuilder();
        for (Element el : getDocument().select("div.o-Method__m-Body p")) {
            if (!el.text().equals(document.select(".o-Method__a-Cta").text())) {
                instructions.append(el.text());
                instructions.append("\n");
            }
        }
        return instructions.toString().trim();
    }

    @Override
    public String getOriginalSource() {
        return "foodnetwork.com";
    }

    @Override
    public String getOriginalAuthor() {
        return document.select("span.o-Attribution__a-Name").text();
    }

    List<String> getUnParsedIngreds() {
        List<String> ingredients = new ArrayList<>();
        for (Element ingredient : document.select("div.o-Ingredients__m-Body ul li")) {
            ingredients.add(ingredient.text());
        }
        return ingredients;
    }
}