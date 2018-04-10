package com.example.demo.parsers;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TestGeneralIngredientsParser {

    GeneralIngredientsParser parser;
    private List<String> ingredient;
    private List<String> units;

    @Before
    public void createGeneralParserObject() {
        parser = new GeneralIngredientsParser();
        units = new MeasurementUnits().getMeasurementUnits();
    }

    @Test
    public void testUnitInList() {
        ingredient = Arrays.asList("1", "cup", "of sugar");
        assertEquals(Arrays.asList("1", "cups", "of sugar"), parser.validateInMeasurementUnitList(ingredient, units));
    }

    @Test
    public void testUnitNotInList() {
        ingredient = Arrays.asList("1", "big", "apple");
        assertEquals(Arrays.asList("1", "NU", "big apple"), parser.validateInMeasurementUnitList(ingredient, units));
    }

    @Test
    public void testNameContainsUnit() {
        ingredient = Arrays.asList("1", "big", "and nice bottle of wine");
        assertEquals(Arrays.asList("1", "bottles", "big and nice bottle of wine"), parser
                .validateInMeasurementUnitList(ingredient, units));
    }

    @Test
    public void testSecondElementNotToOrOr() {
        ingredient = Arrays.asList("1", "cup", "of sugar");
        assertEquals(ingredient, parser.secondElementIsToOr(ingredient));
    }

    @Test
    public void testIngredientOnlyTwoElements() {
        ingredient = Arrays.asList("1", "paprika");
        assertEquals(Arrays.asList("1", "NU", "paprika"), parser.validateInMeasurementUnitList(ingredient, units));
    }

    @Test
    public void testSecondElementIsTo() {
        ingredient = Arrays.asList("1", "to", "2 tablespoons of sugar");
        assertEquals(Arrays.asList("1", "tablespoons", "of sugar"), parser.secondElementIsToOr(ingredient));
    }

    @Test
    public void testSecondElementOr() {
        ingredient = Arrays.asList("1", "or", "2 teaspoons of coffee");
        assertEquals(Arrays.asList("1", "teaspoons", "of coffee"), parser.secondElementIsToOr(ingredient));
    }

    @Test
    public void testFractionConversion() {
        assertEquals("0.25", parser.fractionToDouble("1/4"));
    }

    @Test
    public void testTotalFractionConversion() {
        ingredient = Arrays.asList("1", "0.5", "cups of sugar");
        assertEquals(Arrays.asList("1.5", "cups", "of sugar"), parser.getTotalFraction(ingredient));
    }

    @Test
    public void testTotalFractionConversionBothFractions() {
        ingredient = Arrays.asList("0.5", "0.5", "cups of sugar");
        assertEquals(Arrays.asList("1.0", "cups", "of sugar"), parser.getTotalFraction(ingredient));
    }

    @Test
    public void testAddingMeasurementUnit() {
        MeasurementUnits units = new MeasurementUnits();
        units.addUnit("pinch");
        assertEquals("pinch", units.getMeasurementUnits().get(units.getMeasurementUnits().size() - 1));
    }

    @Test
    public void testNormalIngredGetter() throws JSONException {
        ingredient = Collections.singletonList("1 cup of sugar");
        assertEquals("{\"of sugar\":{\"amount\":1,\"unit\":\"cups\"}}", parser.getIngredients(ingredient));
    }

    @Test
    public void testFirstElFractionGetter() throws JSONException {
        ingredient = Collections.singletonList("1/2 cups of sugar");
        assertEquals("{\"of sugar\":{\"amount\":0.5,\"unit\":\"cups\"}}", parser.getIngredients(ingredient));
    }

    @Test
    public void testSecondElFractionGetter() throws JSONException {
        ingredient = Collections.singletonList("1 1/2 cups of sugar");
        assertEquals("{\"of sugar\":{\"amount\":1.5,\"unit\":\"cups\"}}", parser.getIngredients(ingredient));
    }

}
