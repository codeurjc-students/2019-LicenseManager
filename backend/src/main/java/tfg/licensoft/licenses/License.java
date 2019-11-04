package tfg.licensoft.licenses;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import tfg.licensoft.products.Product;


@Entity
public class License {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String serial;
	private boolean active;
	private String type;
	
	@ManyToOne
	private Product product;
	
	private Date startDate;
	private Date endDate;
	private String owner;
	
	
	
	public License(String serial, boolean active, String type, Product product, String owner) {
		super();
		Calendar ahoraCal = Calendar.getInstance();
		this.serial = serial;
		this.active = active;
		this.type = type;
		this.product=product;
		if(active) {
			this.startDate = ahoraCal.getTime();
			this.calculateEndDate(ahoraCal);
			this.owner=owner;
		}

	}
	


	public Product getProduct() {
		return product;
	}



	public void setProduct(Product product) {
		this.product = product;
	}



	public License () {}
	
	
	private void calculateEndDate(Calendar ahoraCal) {
		switch(type) {
			case "M": {
				ahoraCal.add(Calendar.MONTH, 1);
				this.endDate = ahoraCal.getTime();
				break;
			}
			case "A":{
				ahoraCal.add(Calendar.YEAR, 1);
				this.endDate = ahoraCal.getTime();
				break;	
			}
			default: this.endDate=null;
		}
	}

	public String getOwner() {
		return owner;
	}



	public void setOwner(String owner) {
		this.owner = owner;
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




	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		return true;
	}


}
