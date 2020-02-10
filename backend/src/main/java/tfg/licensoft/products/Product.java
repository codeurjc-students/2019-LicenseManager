package tfg.licensoft.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tfg.licensoft.licenses.License;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<License>licenses;

	private String name;
	
	private String productStripeId;
	
	
	@JsonIgnore
	private HashMap<String,String> plans;
	
	@ElementCollection
	private List<String> typeSubs; 
		
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	private String webLink;
	private boolean photoAvailable;
	@NotNull
	private String photoSrc;
	private HashMap<String,Double> plansPrices;
	private String sku;
	private boolean active;
	

	public Product() {}
	
	public Product(String name) {
		this.name=name;
		this.licenses = new ArrayList();
		this.plans = new HashMap();
		this.typeSubs= new ArrayList<>();
		this.plansPrices = new HashMap<>();
		this.active=true;
 
	}
	
	
	
	//Getters & Setters
	
	
	public String getPhotoSrc() {
		return photoSrc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public boolean isPhotoAvailable() {
		return photoAvailable;
	}

	public void setPhotoAvailable(boolean photoAvailable) {
		this.photoAvailable = photoAvailable;
	}

	
	public List<String> getTypeSubs() {
		return typeSubs;
	}

	public void setTypeSubs(List<String> typeSubs) {
		this.typeSubs = typeSubs;
	}

	public String getProductStripeId() {
		return productStripeId;
	}

	public void setProductStripeId(String productStripeId) {
		this.productStripeId = productStripeId;
	}


	public HashMap<String, String> getPlans() {
		return plans;
	}

	public void setPlans(HashMap<String, String> plans) {
		this.plans = plans;
	}

	public List<License> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<License> licenses) {
		this.licenses = licenses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public HashMap<String, Double> getPlansPrices() {
		return plansPrices;
	}

	public void setPlansPrices(HashMap<String, Double> plansPrices) {
		this.plansPrices = plansPrices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	
}
