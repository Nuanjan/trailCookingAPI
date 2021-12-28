package com.ns.trailcookingapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ns.trailcookingapi.models.ErrorResponse;
import com.ns.trailcookingapi.models.Recipe;
import com.ns.trailcookingapi.models.User;
import com.ns.trailcookingapi.services.RecipeService;
import com.ns.trailcookingapi.services.UserService;

@CrossOrigin(origins= "http://localhost:3000")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
	@Autowired
	RecipeService recipeServ;
	
	@Autowired
	UserService userServ;
	
// ================================== Get Routes =======================================
//	@GetMapping("/recipes/user/{id}")
//	public ResponseEntity<Collection<Recipe>> getAllRecipesWithUser(@PathVariable("id") Long id){
//		List<Recipe> recipesWithUser = recipeServ.findUserRecipes(id);
//		return ResponseEntity.ok(recipesWithUser);
//	}
	
// =================================Create Routes ========================================
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/create/{id}")
	public ResponseEntity<Object> createRecipe(@Valid @RequestBody Recipe newRecipe, 
			BindingResult result,
			@PathVariable("id") Long id
			){
		System.out.println("request body of recipe " +newRecipe.getRecipeName());
		System.out.println("request body of user recipe: " +newRecipe.getUser());
		System.out.println("user id: "+ id);
		User user = userServ.findById(id);
		System.out.println(user);
		newRecipe.setUser(user);
		if (result.hasErrors()) {
	        List<String> errors = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
	        return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
	    }
		
		recipeServ.saveRecipe(newRecipe);
		return ResponseEntity.ok(newRecipe);
	}
// =========================== Delete Routes ===============================================
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBlog(@PathVariable("id") Long id)  {
		recipeServ.deleteRecipe(id);
		return ResponseEntity.ok("deleted successfully");
	}

}
