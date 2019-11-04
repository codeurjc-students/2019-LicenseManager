package tfg.licensoft.licenses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.products.Product;


public interface LicenseRepository  extends JpaRepository<License, Long> {
	License findBySerial(String serial);
	Page<License> findByProduct(Product product,Pageable page);
	License findBySerialAndProduct(String serial, Product product);
	Page<License> findByOwner(String owner, Pageable page);
}
