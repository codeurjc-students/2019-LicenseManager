package tfg.licensoft.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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



	public Product() {}
	
	public Product(String name) {
		this.name=name;
		this.licenses = new ArrayList();
		this.plans = new HashMap();
		this.typeSubs= new ArrayList<>();

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
