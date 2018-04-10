package com.example.demo.allrecipes;

import com.example.demo.parsers.GeneralIngredientsParser;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.example.demo.parsers.RecipeTemplate;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AbstractDocument.BranchElement;

public class AllRecipesParser implements RecipeTemplate {

    private String url;
    private GeneralIngredientsParser ingredientParser;
    private Document document;


    public AllRecipesParser(String url) {
        this.url = url;
        ingredientParser = new GeneralIngredientsParser();
        getDocument();
    }

    @Override
    public Document getDocument() {
        document = new Document("");
        try {
            if (url.isEmpty()) {
                System.out.println("Please provide an URL");
            } else {
                this.document = Jsoup.connect(url).get();
            }
        } catch (IOException e) {
            System.out.println("Please provide correct URL");
        }
        return document;
    }

    public void setDocument(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("SB: " + sb.toString());
                this.document = Jsoup.parse(sb.toString());
                br.close();
            
            } catch (FileNotFoundException e) {
                System.out.println("Could not find a file to read!");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
       
    }


    List<String> getUnParsedIngreds() {
        List<String> ingredients = new ArrayList<>();
        for (Element ingredient : document.select("ul.checklist.dropdownwrapper.list-ingredients-1 li")) {
            ingredients.add(ingredient.text().replace(" ADVERTISEMENT", ""));
        }
        for (Element ingredient : document.select("ul.checklist.dropdownwrapper.list-ingredients-2 li")) {
            ingredients.add(ingredient.text().replace(" ADVERTISEMENT", ""));
        }
        ingredients.remove(ingredients.size() - 1);
        return ingredients;
    }

    @Override
    public String getTitle() {
        return document.select("h1.recipe-summary__h1").text();
    }

    @Override
    public String getPrepTime() {
        return document.select("span.ready-in-time").text();
    }

    @Override
    public String getInstructions() {
        StringBuilder instructions = new StringBuilder();
        for (Element el : document.select("ol.list-numbers.recipe-directions__list li")) {
            instructions.append(el.text());
            instructions.append("\n");
        }
        return instructions.toString().trim();
    }

    @Override
    public String getOriginalSource() {
        return "allrecipes.com";
    }

    @Override
    public String getOriginalAuthor() {
        return document.select("span.submitter__name").text();
    }

    @Override
    public int getServings() {
        String servings = "1";
        Elements metaTags = document.getElementsByTag("meta");
        for (Element metaTag : metaTags) {
            if (metaTag.attr("id").equals("metaRecipeServings")) {
                servings = metaTag.attr("content");
            }
        }
        return Integer.parseInt(servings);
    }

    @Override
    public String getIngredients() throws JSONException {
        return ingredientParser.getIngredients(getUnParsedIngreds());
    }
}
