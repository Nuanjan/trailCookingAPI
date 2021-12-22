package com.ns.trailcookingapi.controllers;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ns.trailcookingapi.models.Recipe;
import com.ns.trailcookingapi.payload.ApiResponse;
import com.ns.trailcookingapi.services.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
	@Autowired
	RecipeService recipeServ;
	
// ================================== Get Routes =======================================
	@GetMapping
	public ResponseEntity<Collection<Recipe>> getAllRecipes(){
		List<Recipe> recipes = recipeServ.allRecipes();
		System.out.println(recipes);
		return ResponseEntity.ok(recipes);
	}
	
// =================================Create Routes ========================================
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createRecipe(@Valid @RequestBody Recipe recipe){
		Recipe createdRecipe = recipeServ.saveRecipe(recipe);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(createdRecipe.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Recipe Created Successfully"));
	}

}
