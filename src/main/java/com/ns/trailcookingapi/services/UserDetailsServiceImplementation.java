package com.ns.trailcookingapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ns.trailcookingapi.models.User;
import com.ns.trailcookingapi.repositories.UserRepository;
import com.ns.trailcookingapi.securities.UserPrincipal;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	
	@Override
	@Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return UserPrincipal.create(user);
    }
	
	@Transactional
	public UserDetails loadUserById(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		
		if (!optionalUser.isPresent()) {
			throw new UsernameNotFoundException("User not found with id : " + id);
		}
		User user = optionalUser.get();
		return UserPrincipal.create(user);
	}
    
    // 2
//    private List<GrantedAuthority> getAuthorities(User user){
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        for(Role role : user.getRoles()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
//            authorities.add(grantedAuthority);
//        }
//        return authorities;
//    }
}