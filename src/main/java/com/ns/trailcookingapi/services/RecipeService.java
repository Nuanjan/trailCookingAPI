package com.ns.trailcookingapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ns.trailcookingapi.models.Recipe;
import com.ns.trailcookingapi.models.User;
import com.ns.trailcookingapi.repositories.RecipeRepository;
import com.ns.trailcookingapi.repositories.UserRepository;

@Service
public class RecipeService {

	private final RecipeRepository recipeRepo;
	private final UserRepository userRepo;
	public RecipeService(RecipeRepository recipeRepo, UserRepository userRepo) {
		this.recipeRepo = recipeRepo;
		this.userRepo = userRepo;
	}
//	public List<Recipe> findUserRecipes(Long id) {
//		return recipeRepo.fin(id);
//	}
	
	public List<Recipe> allRecipes(){
		return recipeRepo.findAll();
	}
	public List<User> allUsers(){
		return userRepo.findAll();
	}
	public Recipe saveRecipe(Recipe recipe) {
		return recipeRepo.save(recipe);
	}
	public void deleteRecipe(Long id) {
		recipeRepo.deleteById(id);
	}
}
