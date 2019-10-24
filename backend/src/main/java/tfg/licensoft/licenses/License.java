package tfg.licensoft.licenses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class License {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String serial;
	private boolean active;
	private String type;
	private String product;
	
	
	
	public License(String serial, boolean active, String type, String product) {
		super();
		this.serial = serial;
		this.active = active;
		this.type = type;
		this.product=product;
	}
	


	public License () {}
	
	
	
	
	
	public String getProduct() {
		return product;
	}


	public void setProduct(String product) {
		this.product = product;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
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
		License other = (License) obj;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		return true;
	}

}
