package com.ns.trailcookingapi.controllers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ns.trailcookingapi.models.ErrorResponse;
import com.ns.trailcookingapi.models.Recipe;
import com.ns.trailcookingapi.services.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
	@Autowired
	RecipeService recipeServ;
	
// ================================== Get Routes =======================================
	@GetMapping("/recipes/user/{id}")
	public ResponseEntity<Collection<Recipe>> getAllRecipesWithUser(@PathVariable("id") Long id){
		List<Recipe> recipesWithUser = recipeServ.findUserRecipes(id);
		return ResponseEntity.ok(recipesWithUser);
	}
	
// =================================Create Routes ========================================
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/create")
	public ResponseEntity<Object> createRecipe(@Valid @RequestBody Recipe recipe, BindingResult result){
		if (result.hasErrors()) {
	        List<String> errors = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
	        return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
	    }
		recipeServ.saveRecipe(recipe);
		return ResponseEntity.ok(recipe);
	}
// =========================== Delete Routes ===============================================
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBlog(@PathVariable("id") Long id)  {
		recipeServ.deleteRecipe(id);
		return ResponseEntity.ok("deleted successfully");
	}

}
