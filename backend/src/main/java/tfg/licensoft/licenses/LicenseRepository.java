package tfg.licensoft.licenses;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.products.Product;


public interface LicenseRepository  extends JpaRepository<License, Long> {
	License findBySerial(String serial);
	Page<License> findByProduct(Product product,Pageable page);
	License findBySerialAndProduct(String serial, Product product);
	Page<License> findByOwner(String owner, Pageable page);
	List<License> findByProductAndActiveAndOwner(Product product, boolean active, String owner);
	Page<License> findByProductAndOwner(Product product, String owner,Pageable page);
	List<License> findByProductAndOwnerAndType(Product product, String owner, String type);
	License findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active);
}
