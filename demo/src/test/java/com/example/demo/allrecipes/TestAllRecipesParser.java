package com.example.demo.allrecipes;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAllRecipesParser {

    AllRecipesParser parser;


    @Before
    public void createAllRecipeParser() {
        parser = new AllRecipesParser("https://www.com.example.demo.allrecipes.com/recipe/88123/apple-cider-sauce-and-pork-loin-chops/?internalSource=staff%20pick&referringId=80&referringContentType=recipe%20hub");
        parser.setDocument("C:\\Users\\Merlin\\eclipse-workspace\\demoBig\\demo\\src\\test\\java\\com\\example\\demo\\allrecipes\\AllRecipesResultExample.txt");
    }

    @Test
    public void testAllRecipesTitle() {
        assertEquals("Apple Cider Sauce and Pork Loin Chops", parser.getTitle());
    }

    @Test
    public void testAllRecipesPrepTime() {
        assertEquals("55 m", parser.getPrepTime());
    }

    @Test
    public void testAllRecipesServings() {
        assertEquals(4, parser.getServings());
    }

    @Test
    public void testAllRecipesGetOriginalSource() {
        assertEquals("allrecipes.com", parser.getOriginalSource());
    }

    @Test
    public void testAllRecipesGetOriginalAuthor() {
        assertEquals("suzette", parser.getOriginalAuthor());
    }

    @Test
    public void testAllRecipesUnparsedIngedients() {
        List<String> ingreds = Arrays.asList("3 tablespoons olive oil", "4 pork loin chops", "seasoning salt to taste",
                "black pepper to taste", "garlic powder to taste", "1/2 teaspoon poultry seasoning",
                "3 tablespoons Worcestershire sauce", "1 (8 ounce) container frozen apple cider concentrate, undiluted",
                "1/4 cup dry sherry");
        assertEquals(ingreds, parser.getUnParsedIngreds());
    }


    @Test
    public void testGetIngredientsAsJson() throws JSONException {
        String ingred = "{" +
                "\"garlic powder to taste\":{\"amount\":1,\"unit\":\"NU\"}," +
                "\"Worcestershire sauce\":{\"amount\":3,\"unit\":\"tablespoons\"}," +
                "\"olive oil\":{\"amount\":3,\"unit\":\"tablespoons\"}," +
                "\"dry sherry\":{\"amount\":0.25,\"unit\":\"cups\"}," +
                "\"poultry seasoning\":{\"amount\":0.5,\"unit\":\"teaspoons\"}," +
                "\"(8 ounce) container frozen apple cider concentrate, undiluted\":{\"amount\":1,\"unit\":\"ounces\"}}";
        assertEquals(ingred, parser.getIngredients());
    }

    @Test
    public void testAllRecipesInstructions() {
        String instructions = "Preheat oven to 375 degrees F (190 degrees C).\nHeat olive oil in a large oven-safe " +
                "frying pan over medium-high heat. Sprinkle chops with salt, pepper, garlic powder, and " +
                "poultry seasoning. Place in hot oil, and brown on both sides. Drizzle Worcestershire sauce over " +
                "chops, and pour in apple cider.\nBake in preheated oven for 25 minutes. Remove chops to a plate, and " +
                "return frying pan to stove over medium-high heat. Stir sherry into pan, and boil until sauce thickens, " +
                "stirring frequently. Serve sauce over chops.";
        assertEquals(instructions, parser.getInstructions());
    }

    @Test
    public void testAllRecipesUrlEmpty() {
        AllRecipesParser parser1 = new AllRecipesParser("");
        System.out.println(parser1.getDocument().empty());
        assertTrue(!parser1.getDocument().hasText());
    }

    @Test
    public void testAllRecipesFilePathBroken() {
        AllRecipesParser parser1 = new AllRecipesParser("");
        parser1.setDocument("jik");
        assertTrue(!parser1.getDocument().hasText());
    }

    @Test
    public void testAllRecipesFilePathNull() {
        AllRecipesParser parser1 = new AllRecipesParser("");
        String path = "C:\\Users\\Merlin\\eclipse-workspace\\demoBig\\demo\\src\\test\\java\\com\\example\\demo\\allrecipes\\example.txt";
        try {
            final RandomAccessFile file = new RandomAccessFile(path, "rw");
            file.getChannel().lock();
            parser1.setDocument(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(!parser1.getDocument().hasText());
    }
}

