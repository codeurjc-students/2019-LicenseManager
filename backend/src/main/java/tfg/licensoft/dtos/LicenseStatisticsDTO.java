package tfg.licensoft.dtos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class LicenseStatisticsDTO {
	private Long id;
	private LicenseSubscriptionDTO license;
	private String ip;
	private int nUsage;
	private List<Date> usages;
	private String userName;
	private HashMap<String,Integer> usagePerTime;
	private Integer period;

	
	public LicenseStatisticsDTO() {}
	public LicenseStatisticsDTO(LicenseSubscriptionDTO l) {
		this.nUsage=0;
		this.usages= new ArrayList<>();
		this.license=l;
		this.setUsagePerTimeReady();
		this.period=l.getPeriod();
		
	}
	
	private void setUsagePerTimeReady() {
		this.usagePerTime = new HashMap<>();
	    try{
		    Calendar calendar = new GregorianCalendar();
		    calendar.setTime(this.license.getStartDate());
		     
		    Calendar endCalendar = new GregorianCalendar();
		    LicenseSubscriptionDTO ls = (LicenseSubscriptionDTO)(this.license);
		    endCalendar.setTime(ls.getEndDate());
		    
		    int numberAdd;
		    switch(this.license.getType()) {
		    	case "MB":
			    case "M":{
			    	numberAdd = Calendar.DATE;
			    	break;
			    }
			    case "D":{
			    	numberAdd = Calendar.HOUR_OF_DAY;
			    	break;
			    }
			    case "A":{
			    	numberAdd = Calendar.MONTH;
			    	break;
			    }
			    default:{
			    	numberAdd = Calendar.DATE;
			    	break;
			    }
		    }
		    
		    while (calendar.before(endCalendar)) {
		        Date result = calendar.getTime();
		        this.usagePerTime.put(this.getFormattedDate(result),0);
		        calendar.add(numberAdd, 1);
		    }
		    System.out.println(this.usagePerTime);
		}catch (ClassCastException e) {
			System.out.println("Error while setting up the usage per time ready");
		}

	 

	}
	
	public String getFormattedDate(Date result) {
		 switch(this.license.getType()) {
	    	case "MB":
		    case "M":{
		        return new SimpleDateFormat("MM/dd/yyyy").format(result);
		    }
		    case "D":{
		        return new SimpleDateFormat("MM/dd HH").format(result) + "h";
		    }
		    case "A":{
		    	return new SimpleDateFormat("yyyy/MM").format(result);
		    }
		    default:{
		    	return new SimpleDateFormat("MM/dd/yyyy").format(result);
		    }
	    }
	}
	

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public LicenseSubscriptionDTO getLicense() {
		return license;
	}
	public void setLicense(LicenseSubscriptionDTO license) {
		this.license = license;
	}



	public HashMap<String, Integer> getUsagePerTime() {
		return usagePerTime;
	}


	public void setUsagePerTime(HashMap<String, Integer> usagePerTime) {
		this.usagePerTime = usagePerTime;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}



	public List<Date> getUsages() {
		return usages;
	}


	public void setUsages(List<Date> usages) {
		this.usages = usages;
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
