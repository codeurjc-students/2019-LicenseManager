package tfg.licensoft.dtos;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;



import javax0.license3j.Feature;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.KeyPairReader;
import tfg.licensoft.configurations.PropertiesLoader;
import tfg.licensoft.products.Product;

public class LicenseDTO {
	
	private Long id;
	private String serial;
	private boolean active;
	
	private String type;
	
	private Product product;
	
	private Date startDate;
	private String owner;
	private double price;
	private String licenseString;
	private List<LicenseStatisticsDTO> licenseStats;
	
 
	
	public LicenseDTO(boolean active, Product product, String owner) {
        this.type = "L";
		this.product=product;

        this.serial = this.generateSerial();
        

		this.active = active;
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
		
        String mode = this.product.getMode();
        if( !(this instanceof LicenseSubscriptionDTO) && mode!=null && (mode.equals("Offline") || mode.equals("Both"))){
        	javax0.license3j.License l = this.generateLicenseFile("licenseFile-"+this.getProduct().getName()+".txt");
    		this.licenseString =  this.signLicense(l);
        }


	}
	
	public LicenseDTO() {}
	
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

	public List<LicenseStatisticsDTO> getLicenseStats() {
		return licenseStats;
	}

	public void setLicenseStats(List<LicenseStatisticsDTO> licenseStats) {
		this.licenseStats = licenseStats;
	}

	//Common methods
	protected String generateSerial() {
		return UUID.randomUUID().toString();
	}
	
	protected javax0.license3j.License generateLicenseFile(String path) {
		javax0.license3j.License license = new javax0.license3j.License();
        license.add(Feature.Create.dateFeature("startDate",this.getStartDate()));
        license.add(Feature.Create.stringFeature("product",this.getProduct().getName()));
        license.add(Feature.Create.stringFeature("type",this.getType()));
        license.add(Feature.Create.stringFeature("owner",this.getOwner()));
		
		return license;

        
        
	}
	
	protected String signLicense(javax0.license3j.License license) {
		String privateKeyPath="";
		try {
			privateKeyPath= PropertiesLoader.loadProperties("application.properties").getProperty("licencheck.keys.private");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (privateKeyPath==null) {
			privateKeyPath = System.getenv("LICENCHECK.KEYS.PRIVATE");
		}
		if(privateKeyPath==null) {
			privateKeyPath = System.getenv("licencheck.keys.private");
		}
		if(privateKeyPath==null) {
			privateKeyPath = System.getenv("licencheck_keys_private");
		}

		if (privateKeyPath==null) {
			privateKeyPath = System.getenv("LICENCHECK_KEYS_PRIVATE");
		}
		
		try (KeyPairReader kpr = new KeyPairReader(privateKeyPath)){
            LicenseKeyPair lkp = kpr.readPrivate();
            license.sign(lkp.getPair().getPrivate(),"SHA-512");
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
		result = prime * result + ((licenseString == null) ? 0 : licenseString.hashCode());
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
		LicenseDTO other = (LicenseDTO) obj;
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
		if (licenseString == null) {
			if (other.licenseString != null)
				return false;
		} else if (!licenseString.equals(other.licenseString))
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
