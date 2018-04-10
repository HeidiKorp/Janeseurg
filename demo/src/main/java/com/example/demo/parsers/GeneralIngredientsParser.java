package com.example.demo.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralIngredientsParser {

    private MeasurementUnits units;

    public GeneralIngredientsParser() {
        units = new MeasurementUnits();
    }

    List<String> validateInMeasurementUnitList(List<String> words, List<String> units) {
        String name;
        String unit = "NU";
        String amount = words.get(0);
        if (words.size() == 2) {
            name = words.get(1);
        } else {
            name = words.get(1) + " " + words.get(2);
            for (String un : units) {
                if (un.contains(words.get(1))) {
                    unit = un;
                    name = words.get(2);
                } else if (words.get(2).contains(un.substring(0, un.length() - 1))) {
                    unit = un;
                }
            }
        }
        return Arrays.asList(amount, unit, name);
    }

    public String getIngredientsAsJson(List<List<String>> allWords) throws JSONException {
        JSONObject obj = new JSONObject();
        for (List<String> words : allWords) {
            obj.put(words.get(2), new JSONObject().put("amount", Double.parseDouble(words.get(0)))
                    .put("unit", words.get(1)));
        }
        return obj.toString();
    }


    List<String> secondElementIsToOr(List<String> words) {
        List<String> newWords = new ArrayList<>();
        if (words.get(1).equals("to") | words.get(1).equals("or")) {
            newWords.add(words.get(0));
            List<String> neuArr = Arrays.asList(words.get(2).split(" ", 3));
            newWords.addAll(Arrays.asList(neuArr.get(1), neuArr.get(2)));
        } else {
            newWords = words;
        }
        return newWords;
    }

    String fractionToDouble(String fraction) {
        String[] fractionArray = fraction.split("/");
        double divided = Double.parseDouble(fractionArray[0]) / Double.parseDouble(fractionArray[1]);
        return String.valueOf(divided);
    }

    List<String> getTotalFraction(List<String> words) {
        double totalAmount = Double.parseDouble(words.get(0)) + Double.parseDouble(words.get(1));
        List<String> leftover = new ArrayList<>();
        leftover.add(String.valueOf(totalAmount));
        leftover.addAll(Arrays.asList(words.get(2).split(" ", 2)));
        return leftover;
    }


    public String getIngredients(List<String> getUnParsedIngreds) throws JSONException {
        List<String> ingredData = new ArrayList<>();
        List<List<String>> allIngredients = new ArrayList<>();
        for (String ingredient : getUnParsedIngreds) {

            // If the first element starts with a digit
            if (Character.isDigit(ingredient.substring(0, 1).toCharArray()[0])) {
                List<String> words = new ArrayList<>();
                words.addAll(Arrays.asList(ingredient.split(" ", 3)));
                if (words.get(0).contains("/")) {
                    words.set(0, fractionToDouble(words.get(0)));
                }

                // If the second elements starts with a digit
                if (Character.isDigit(words.get(1).substring(0, 1).toCharArray()[0])) {
                    words.set(1, fractionToDouble(words.get(1)));
                    words = getTotalFraction(words);
                }

                // If the second element is "to"
                words = secondElementIsToOr(words);
                words = validateInMeasurementUnitList(words, units.getMeasurementUnits());
                ingredData = words;
            } else {
                ingredData.set(0, "1");
                ingredData.set(1, "NU");
                ingredData.set(2, ingredient);
            }
            allIngredients.add(ingredData);
        }
        return getIngredientsAsJson(allIngredients);
    }

}
