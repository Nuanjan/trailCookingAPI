package com.ns.trailcookingapi.services;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.ns.trailcookingapi.models.LoginUser;
import com.ns.trailcookingapi.models.User;
import com.ns.trailcookingapi.payload.JwtAuthenticationResponse;
import com.ns.trailcookingapi.repositories.RoleRepository;
import com.ns.trailcookingapi.repositories.UserRepository;
import com.ns.trailcookingapi.securities.JwtTokenProvider;

@Service
public class UserService {
	
	private UserRepository userRepo;
	
	private RoleRepository roleRepo;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	private JwtTokenProvider tokenProvider;
	
	private AuthenticationManager authenticationManager;
	
	public UserService(UserRepository userRepo,RoleRepository roleRepo,BCryptPasswordEncoder passwordEncoder,JwtTokenProvider tokenProvider,AuthenticationManager authenticationManager) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}
	
	 // Saves a client with only the user role
	// Register a new user with a hashed password after first making sure that the email doesn't exist and that the passwords match.
	public User register(User newUser, BindingResult result) {
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(newUser.getPassword());
		
		if (userRepo.findByUsername(newUser.getUsername()) != null) {
			result.rejectValue("username", "Unique", "This email is already in use!");
		}
		if (!newUser.getPassword().equals(newUser.getConfirm())) {
			result.rejectValue("confirmPassword", "Matches", "Passwords must match!");
		}
		if (!m.matches()) {
			result.rejectValue("password", "Secure", "Password must have at least one lowercase letter, uppercase letter, number and special character, and be at least 8 characters long.");
		}
		if (result.hasErrors()) {
			return null;
		} else {
			newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
			newUser.setRoles(roleRepo.findByName("ROLE_USER"));
			return userRepo.save(newUser);
		}
	}
     
     // Saves a client with only the admin role -- not necessary at this point, but keeping the code. Will need to add the error implementation as above if I do decide to use this.
    public void saveUserWithAdminRole(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepo.findByName("ROLE_ADMIN"));
        userRepo.save(user);
    }    
    
    // Find a user by their username (email)
    public User findByUsername(String username) {
    	return userRepo.findByUsername(username);
    }
    
    // Find a user by their id
    public User findById(Long id) {
    	Optional<User> user = userRepo.findById(id);
    	if (!user.isPresent()) {
    		return null;
    	}
    	return user.get();
    }
    
			
 // Validate the login user's credentials.
 	public User validateLogin(LoginUser newLogin, BindingResult result) {
 		if(result.hasErrors()) {
 			return null;
 		} 
 		Optional<User> user = userRepo.findByUsernameOrEmail(newLogin.getUsername(), newLogin.getUsername());
 		if (!user.isPresent()) {
 			result.rejectValue("username", "Unique", "Invalid email or password");
 		}
 		if(result.hasErrors()) {
 			return null;
 		} 
 		if(!BCrypt.checkpw(newLogin.getPassword(), user.get().getPassword())) {
 			result.rejectValue("password", "Matches", "Invalid email or password");
 		}
 		if(result.hasErrors()) {
 			return null;
 		} 
 		System.out.println(user+" this is user");
 		return user.get();
 	}
 	
 	// Actually login if we pass the earlier validation, probably don't need that earlier one but just trying to get this working.
 	public ResponseEntity<Object> login(LoginUser newLogin) {
 		Authentication authentication = authenticationManager.authenticate(
 				new UsernamePasswordAuthenticationToken(
 						newLogin.getUsername(),
 						newLogin.getPassword()
 				)
 		);
 		SecurityContextHolder.getContext().setAuthentication(authentication);
 		
 		String jwt = tokenProvider.generateToken(authentication);
 		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
 	}
}