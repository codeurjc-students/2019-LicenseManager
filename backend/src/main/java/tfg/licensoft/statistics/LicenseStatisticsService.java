package tfg.licensoft.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfg.licensoft.licenses.License;

@Service
public class LicenseStatisticsService {
	
	@Autowired
	private LicenseStatisticsRepository licStatRepo;
	
	public LicenseStatistics save(LicenseStatistics l) {
		return this.licStatRepo.save(l);
	}
	
	public List<LicenseStatistics> findByLicenseAndIp(License lic, String ip) {
		return this.licStatRepo.findByLicenseAndIp(lic, ip);
	}
	
	public List<LicenseStatistics> findByLicenseAndUserName(License lic, String userName) {
		return this.licStatRepo.findByLicenseAndUserName(lic, userName);
	}
	
	public LicenseStatistics findByLicenseAndIpAndUserName(License lic, String ip, String userName) {
		return this.licStatRepo.findByLicenseAndIpAndUserName(lic, ip, userName);
	}
	
	public LicenseStatistics delete(LicenseStatistics l) {
		return this.delete(l);
	}
	
}
