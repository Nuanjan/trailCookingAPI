package com.ns.trailcookingapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ns.trailcookingapi.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
	List<Recipe> findAll();
	Optional<Recipe> findById(Long id);
	//List<Recipe> findByUser(Long id);

	void deleteById(Long id);

}
