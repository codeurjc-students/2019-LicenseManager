package tfg.licensoft.licenses;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import tfg.licensoft.configurations.PropertiesLoader;
import tfg.licensoft.products.Product;
import tfg.licensoft.statistics.LicenseStatistics;

import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

import javax0.license3j.Feature;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.KeyPairReader;
import net.bytebuddy.asm.Advice.This;

import org.springframework.context.annotation.PropertySource;
@PropertySource("classpath:/application.properties")
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
	private String owner;
	private double price;
	@Column(columnDefinition = "LONGTEXT")
	private String licenseString;
	
	@OneToMany(mappedBy="license")
	private List<LicenseStatistics> licenseStats;
	
 
	
	public License(boolean active, Product product, String owner) {
        this.type = "L";
		this.serial = this.generateSerial();
		this.active = active;
		this.product=product;
		if(active) {
			Calendar ahoraCal = Calendar.getInstance();
			this.startDate = ahoraCal.getTime();
			this.owner=owner;
		}
		try {
			this.price = product.getPlansPrices().get(type);
		}catch(NullPointerException e) {
			this.price=0;
		}
		this.licenseString =  this.generateLicenseFile("licenseFile-"+this.getProduct().getName()+".txt");


	}
	
	public License() {}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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
		System.out.println("SETTING ACTIVE: " + active);
		this.active = active;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	

	public String getLicenseString() {
		return licenseString;
	}

	public void setLicenseString(String licenseString) {
		this.licenseString = licenseString;
	}

	public List<LicenseStatistics> getLicenseStats() {
		return licenseStats;
	}

	public void setLicenseStats(List<LicenseStatistics> licenseStats) {
		this.licenseStats = licenseStats;
	}

	//Common methods
	protected String generateSerial() {
		return UUID.randomUUID().toString();
	}
	
	protected String generateLicenseFile(String path) {
		if(this instanceof LicenseSubscription) {
			return null;
		}
		String privateKeyPath="";
		try {
			privateKeyPath= PropertiesLoader.loadProperties("application.properties").getProperty("licencheck.keys.private");
		} catch (IOException e) {
			e.printStackTrace();
		}
		javax0.license3j.License license = new javax0.license3j.License();
        license.add(Feature.Create.stringFeature("serial",this.getSerial()));
        license.add(Feature.Create.dateFeature("startDate",this.getStartDate()));
        license.add(Feature.Create.stringFeature("product",this.getProduct().getName()));
        license.add(Feature.Create.stringFeature("type",this.getType()));
        license.setLicenseId(UUID.fromString(license.get("serial").getString()));

        try {
            KeyPairReader kpr = new KeyPairReader(privateKeyPath);
            LicenseKeyPair lkp = kpr.readPrivate();
            license.sign(lkp.getPair().getPrivate(),"SHA-512");
            kpr.close();
            return license.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((licenseStats == null) ? 0 : licenseStats.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (active != other.active)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (licenseStats == null) {
			if (other.licenseStats != null)
				return false;
		} else if (!licenseStats.equals(other.licenseStats))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
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

	



}
