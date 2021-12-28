package com.ns.trailcookingapi.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Table(name="recipes")
public class Recipe {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank(message="Recipe Name is Required!")
	@Size(min=2,max=250, message="Name cannot be short less than 2 or greater than 250 characters!")
	private String recipeName;
	
	@Lob
	@Size(min=5, message="Description cannot be short less than 5 characters!")
	private String description;
	
	@Size(min=5)
	private String imgUrl;
	
	@Size(min=5)
	private String link;
	
	
	// ====================== Related Data - n:1 ==================================================
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;

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
	//========================= Constructors ======================================
	
	public Recipe() {}

	public Recipe(
			@NotBlank(message = "Recipe Name is Required!") @Size(min = 2, max = 250, message = "Name cannot be short less than 2 or greater than 250 characters!") String recipeName,
			@Size(min = 5, message = "Description cannot be short less than 5 characters!") String description,
			@Size(min = 5) String imgUrl, @Size(min = 5) String link, Date createdAt, Date updatedAt, User user) {
		super();
		this.recipeName = recipeName;
		this.description = description;
		this.imgUrl = imgUrl;
		this.link = link;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
		System.out.println("user: " + user);
	}
	// ============================== Getter and Setter =======================================
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	

	
}
