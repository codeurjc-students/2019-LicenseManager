package tfg.licensoft.licenses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LicenseRepository  extends JpaRepository<License, Long> {
	License findBySerial(String serial);
	Page<License> findByProduct(String product,Pageable page);
	License findBySerialAndProduct(String serial, String product);
}
