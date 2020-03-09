package tfg.licensoft.statistics;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tfg.licensoft.licenses.License;

@Entity
public class LicenseStatistics {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private License license;
	private String ip;
	private int nUsage;
	private Date lastUsage;
	
	
	
	
	public LicenseStatistics() {}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}

	



	public Date getLastUsage() {
		return lastUsage;
	}


	public void setLastUsage(Date lastUsage) {
		this.lastUsage = lastUsage;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getnUsage() {
		return nUsage;
	}


	public void setnUsage(int nUsage) {
		this.nUsage = nUsage;
	}




	
	
	

}
