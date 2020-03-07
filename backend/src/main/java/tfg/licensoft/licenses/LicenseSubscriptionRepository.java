package tfg.licensoft.licenses;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.products.Product;

public interface LicenseSubscriptionRepository extends JpaRepository<LicenseSubscription, Long> {
	LicenseSubscription findBySerial(String serial);
	Page<LicenseSubscription> findByProduct(Product product,Pageable page);
	LicenseSubscription findBySerialAndProduct(String serial, Product product);
	Page<LicenseSubscription> findByOwner(String owner, Pageable page);
	List<LicenseSubscription> findByProductAndActiveAndOwner(Product product, boolean active, String owner);
	Page<LicenseSubscription> findByProductAndOwner(Product product, String owner,Pageable page);
	List<LicenseSubscription> findByProductAndOwnerAndType(Product product, String owner, String type);
	LicenseSubscription findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active);
	LicenseSubscription findBySerialAndProductAndActive(String serial, Product product,boolean active);

}
