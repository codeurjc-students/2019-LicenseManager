package tfg.licensoft.users;

import javax.persistence.OneToMany;
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
import javax.persistence.CascadeType;
import javax.persistence.Column;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.stripe.model.Customer;

import tfg.licensoft.stripe.StripeCard;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique=true)
	private String name;
	private String passwordHash;
	
	@JsonIgnore
	@Column(unique=true)
	private String customerStripeId;
	
	@ElementCollection
	@OneToMany(targetEntity=StripeCard.class, mappedBy="user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StripeCard> stripeCards;

	public User(String customerStripeId,String name, String password, String... roles) {
		this.name = name;
		this.passwordHash = new BCryptPasswordEncoder().encode(password);
		this.roles = new ArrayList<>(Arrays.asList(roles));
		this.customerStripeId=customerStripeId;
		this.stripeCards= new ArrayList<StripeCard>();
	}

	protected User() {}

	
	
	public List<StripeCard> getStripeCards() {
		return stripeCards;
	}

	public void setStripeCards(List<StripeCard> stripeCards) {
		this.stripeCards = stripeCards;
	}

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

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

} 
