package tfg.licensoft.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	@JsonIgnore
	private String passwordHash;
	
	@JsonIgnore
	private String customerStripeId;
	
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;
	
	

	public User(String email,String customerStripeId,String name, String password, String... roles) {
		this.name = name;
		this.passwordHash = new BCryptPasswordEncoder().encode(password);
		this.roles = new ArrayList<>(Arrays.asList(roles));
		this.customerStripeId=customerStripeId;
		this.email=email;
	}

	protected User() {}

	 
	

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public String getCustomerStripeId() {
		return customerStripeId;
	}

	public void setCustomerStripeId(String customerStripeId) {
		this.customerStripeId = customerStripeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	


} 
