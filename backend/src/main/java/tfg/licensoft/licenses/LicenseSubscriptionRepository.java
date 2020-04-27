package tfg.licensoft.licenses;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tfg.licensoft.products.Product;

public interface LicenseSubscriptionRepository extends JpaRepository<LicenseSubscription, Long> {
	LicenseSubscription findBySerial(String serial);
	List<LicenseSubscription> findByProduct(Product product);
	LicenseSubscription findBySerialAndProduct(String serial, Product product);
	List<LicenseSubscription> findByOwner(String owner);
	List<LicenseSubscription> findByProductAndActiveAndOwner(Product product, boolean active, String owner);
	List<LicenseSubscription> findByProductAndOwner(Product product, String owner);
	List<LicenseSubscription> findByProductAndOwnerAndType(Product product, String owner, String type);
	LicenseSubscription findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active);
	LicenseSubscription findBySerialAndProductAndActive(String serial, Product product,boolean active);

}
