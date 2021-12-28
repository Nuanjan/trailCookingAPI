package com.ns.trailcookingapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ns.trailcookingapi.models.ErrorResponse;
import com.ns.trailcookingapi.models.LoginUser;
import com.ns.trailcookingapi.models.User;
import com.ns.trailcookingapi.securities.CurrentUser;
import com.ns.trailcookingapi.securities.JwtTokenProvider;
import com.ns.trailcookingapi.securities.UserPrincipal;
import com.ns.trailcookingapi.services.UserService;



@CrossOrigin(origins= "http://localhost:3000")
@RequestMapping("/api/user")
@RestController
public class UserController {
	@Autowired
    AuthenticationManager authenticationManager;
	@Autowired
    JwtTokenProvider tokenProvider;

	private UserService userService;
	public UserController(UserService userService) {
		this.userService =userService;
	}
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Object> getLoggedInUser(@CurrentUser UserPrincipal currentUser) {
		User user = userService.findByUsername(currentUser.getUsername());
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@Valid @RequestBody User newUser, BindingResult result) {
		userService.register(newUser, result);
		if (result.hasErrors()) {
	        List<String> errors = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
	        return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
	    }
		return ResponseEntity.ok(newUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginUser newLogin, BindingResult result) {
		User user = userService.validateLogin(newLogin, result);
		if (result.hasErrors()) {
			List<String> errors = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
	        return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
		} else {
			newLogin.setUsername(user.getUsername());
			ResponseEntity<Object> jwtResponse = userService.login(newLogin);
			return ResponseEntity.ok(jwtResponse);
		}
		
		
	}
	
} 