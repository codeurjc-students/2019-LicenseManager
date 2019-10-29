package tfg.licensoft.licenses;

import java.util.Calendar;
import java.util.Date;

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
	private Date startDate;
	private Date endDate;
	
	
	
	public License(String serial, boolean active, String type, String product) {
		super();
		Calendar ahoraCal = Calendar.getInstance();
		this.serial = serial;
		this.active = active;
		this.type = type;
		this.product=product;
		if(active) {
			this.startDate = ahoraCal.getTime();
			this.calculateEndDate(ahoraCal);
		}

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
