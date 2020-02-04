package tfg.licensoft.licenses;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
	
	public License(boolean active, String type, Product product, String owner) {
		super();
		this.serial = this.generateSerial();
		Calendar ahoraCal = Calendar.getInstance();
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
			case "D":{
				ahoraCal.add(Calendar.HOUR, 24);
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
		Calendar ahoraCal = Calendar.getInstance();
		this.calculateEndDate(ahoraCal);
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
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
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
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	private String generateSerial() {
		return UUID.randomUUID().toString();
	}

	/*
	private void generateSerial(String productName,String type, String userName) throws NoSuchAlgorithmException {
		String serialNumberEncodedName = this.calculateSecurityHash(userName,"MD2") +
			    calculateSecurityHash(userName,"MD5") +
			        calculateSecurityHash(userName,"SHA1");
		
		String serialNumberEncodedType = this.calculateSecurityHash(type,"MD2") +
			    calculateSecurityHash(type,"MD5") +
			        calculateSecurityHash(type,"SHA1");
		
		String serialNumberEncodedProduct = this.calculateSecurityHash(productName,"MD2") +
			    calculateSecurityHash(productName,"MD5") +
			        calculateSecurityHash(productName,"SHA1");
		
		String serialNumber = ""
			    + serialNumberEncodedName.charAt(32)  + serialNumberEncodedName.charAt(76)
			    + serialNumberEncodedProduct.charAt(100) + serialNumberEncodedName.charAt(50) + "-"
			    + serialNumberEncodedType.charAt(2)   + serialNumberEncodedName.charAt(91)
			    + serialNumberEncodedName.charAt(73)  + serialNumberEncodedType.charAt(72)
			    + serialNumberEncodedProduct.charAt(98)  + "-"
			    + serialNumberEncodedName.charAt(47)  + serialNumberEncodedProduct.charAt(65)
			    + serialNumberEncodedType.charAt(18)  + serialNumberEncodedType.charAt(85) + "-"
			    + serialNumberEncodedName.charAt(27)  + serialNumberEncodedName.charAt(53)
			    + serialNumberEncodedProduct.charAt(102) + serialNumberEncodedType.charAt(15)
			    + serialNumberEncodedType.charAt(99);
		this.setSerial(serialNumber);
	}
	
	private String calculateSecurityHash(String stringInput, String algorithmName) throws java.security.NoSuchAlgorithmException {
		        String hexMessageEncode = "";
		        byte[] buffer = stringInput.getBytes();
		        java.security.MessageDigest messageDigest =
		            java.security.MessageDigest.getInstance(algorithmName);
		        messageDigest.update(buffer);
		        byte[] messageDigestBytes = messageDigest.digest();
		        for (int index=0; index < messageDigestBytes.length ; index ++) {
		            int countEncode = messageDigestBytes[index] & 0xff;
		            if (Integer.toHexString(countEncode).length() == 1)
		                hexMessageEncode = hexMessageEncode + "0";
		            hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
		        }
		        return hexMessageEncode;
	}
	
*/

}
