package com.example.demo.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
	RecipeRepository recipeRepository;
	
	RecipeService(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	Recipe addRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	Recipe getRecipe(long id) {
		return recipeRepository.findOne(id);
	}
	
	String getRecipeIngredients(long id) {
		return getRecipe(id).ingredients;
	}
	
	String getRecipeInstructions(long id) {
		return getRecipe(id).instructions;
	}
	
	String getRecipeName(long id) {
		return getRecipe(id).name;
	}
	
	String getRecipeTime(long id) {
		return getRecipe(id).time;
	}
	
	int getRecipeSize(long id) {
		return getRecipe(id).size;
	}
	
	// get all ids that match searched name
	private List<Long> getAllIdsByName(String name) {
		List<Long> ids = new ArrayList<Long>();
		
		recipeRepository.findAll().forEach(r -> {
			if (r.name.contains(name)) ids.add(r.id);	
		});
		
		return ids;
	}
	
	private String getAllRecipeInfoByIds(List<Long> ids) {
		JSONObject infoJSON = new JSONObject();
		
		for (long id : ids) {
			infoJSON.put(String.valueOf(id), getRecipe(id).toString());
		}
		
		return infoJSON.toString();
	}
	
	/*
	 * Input is name.
	 * Output is a json of the info of all recipes matching the name where the key is id and value is recipe info.
	 */
	String searchByName(String name) {
		List<Long> ids = getAllIdsByName(name);
		JSONObject infoJSON = new JSONObject();
		
		for (long id : ids) {
			infoJSON.put(String.valueOf(id), getRecipe(id).toString());	
		}
		return infoJSON.toString();
	}
	
	/* Argumendi formaat: {ingredient1:{size:*number*, unit:*unit*}, ingredient2:{jne}}
	 * Kui kogust pole m2rgitud, siis peab size 0 olema.
	 * Output is a json of the info of all recipes matching the name where the key is id and value is recipe info.
	 */
	
	public String searchByIngredients(String ingredients) {
		JSONObject json = new JSONObject(ingredients);
		List<Long> ids = new ArrayList<>();
		recipeRepository.findAll().forEach(recipe -> {
			if (recipe.checkIfMatchesIngredientsPartly(json)) {
				ids.add(recipe.id);
			}
		});

		return this.getAllRecipeInfoByIds(ids);
	}
	
	
	public String getAll() {
		JSONObject jsonObject = new JSONObject();
		recipeRepository.findAll().forEach(recipe -> {
			jsonObject.put(String.valueOf(recipe.id), recipe.toString());
		});
		return jsonObject.toString();
	}
	
	public Recipe getExternalRecipe(String url) throws IOException {
		return Recipe.getExternalRecipe(url);
		
	}
	
	
	
	
	
	

}
