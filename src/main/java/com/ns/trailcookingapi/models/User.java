package com.ns.trailcookingapi.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="First Name must not be blank")
    @Size(min=2,max = 40, message="First Name must be between 2 and 40 characters")
    private String firstname;
    
    @NotBlank(message="Last Name must not be blank")
    @Size(min=2,max = 40, message="Last Name must be between 2 and 40 characters")
    private String lastname;

    @NotBlank(message="User Name is required!")
    @Size(min=3,max = 15, message="User Name must be between 2 and 40 characters")
    private String username;

    @NaturalId
    @NotBlank(message="Email is required!")
    @Size(min=6,max = 40, message="Email must be between 6 and 40 characters")
    @Email
    private String email;

    @NotBlank(message="Password is required!")
    @Size(max = 128)
    private String password;
    
    @Transient
    @NotNull(groups = Confirm.class )
    @Size(min=6, max=128, message="Confirm Password must be between 6 and 128 characters")
    private transient String confirm;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private List<Role> roles;
    
    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
 // ====================== Related Data - 1:n ==================================================
    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private List<Recipe> recipes;
  

    public User() {

    }

    public User(String firstname,String lastname, String username, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    
    
    
    
  public User(Long id,
			@NotBlank(message = "First Name must not be blank") @Size(min = 2, max = 40, message = "First Name must be between 2 and 40 characters") String firstname,
			@NotBlank(message = "Last Name must not be blank") @Size(min = 2, max = 40, message = "Last Name must be between 2 and 40 characters") String lastname,
			@NotBlank(message = "User Name is required!") @Size(min = 3, max = 15, message = "User Name must be between 2 and 40 characters") String username,
			@NotBlank(message = "Email is required!") @Size(min = 6, max = 40, message = "Email must be between 6 and 40 characters") @Email String email,
			@NotBlank(message = "Password is required!") @Size(max = 128) String password) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
	}

// ============================== Getter and Setter =========================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
    	return lastname;
    }
    
    public void setLastname(String lastname) {
    	this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirm() {
    	return confirm;
    }
    
    public void setConfirm(String confirm) {
    	this.confirm = confirm;
    }
    

    public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}