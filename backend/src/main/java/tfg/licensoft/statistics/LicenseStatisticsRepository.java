package tfg.licensoft.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.licenses.License;


public interface LicenseStatisticsRepository  extends JpaRepository<LicenseStatistics, Long>  {
	
	List<LicenseStatistics> findByLicenseAndIp(License license, String ip);
	List<LicenseStatistics> findByLicenseAndUserName(License license, String userName);
	LicenseStatistics findByLicenseAndIpAndUserName(License license, String ip, String userName);

}
