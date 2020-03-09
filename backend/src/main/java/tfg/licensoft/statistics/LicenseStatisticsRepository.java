package tfg.licensoft.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.licenses.License;


public interface LicenseStatisticsRepository  extends JpaRepository<LicenseStatistics, Long>  {
	
	LicenseStatistics findByLicenseAndIp(License license, String ip);
	
}
