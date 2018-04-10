package com.example.demo.Recipe;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.foodnetwork.FoodNetworkParser;

@RestController
public class RecipeController {
	RecipeService recipeService;
	
	RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/recipes/add", method = RequestMethod.POST, consumes = "application/json")
	public Recipe addRecipe(@RequestBody Recipe recipe) {
		return recipeService.addRecipe(recipe);	
	}
	
	@CrossOrigin
	@RequestMapping(value = "/external/add", method = RequestMethod.POST, consumes = "text/plain")
	public Recipe addRecipe(@RequestBody String url) throws IOException {
		return recipeService.addRecipe(Recipe.getExternalRecipe(url));
	}
	
	/*
	 * Tagastab kujul {id number:{instructions:*juhised*, size:*suurus*, name:*nimi*, ingredients:*koostisosad*,
	 * time:*aeg*}, id number:{jne}}
	 */
	@RequestMapping(value = "/search/name/{name}", method=RequestMethod.GET)
	public String searchByName(@PathVariable("name") String name) {
		return recipeService.searchByName(name);	
	}
	
	/* Argumendi formaat: {ingredient1:{amount:*number*, unit:*unit*}, ingredient2:{jne}}
	 * Kui kogust pole m2rgitud, siis peab amount -1 olema.
	 * EELDAB et ingredientsis pole jutum2rke.
	 */
	@RequestMapping(value = "/search/ingredients/{ingredients}", method=RequestMethod.GET)
	public String searchByIngredients(String ingredients) {
		return recipeService.searchByIngredients(ingredients);
	}
	
	@RequestMapping(value = "/recipes/all", method=RequestMethod.GET)
	public String getAll() {
		return recipeService.getAll();	
	}
	
	
}
