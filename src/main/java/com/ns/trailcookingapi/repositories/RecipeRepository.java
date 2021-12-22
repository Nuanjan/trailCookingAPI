package com.ns.trailcookingapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ns.trailcookingapi.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	List<Recipe> findAll();
	Optional<Recipe> findById(Long id);
	void deleteById(Long id);

}
