package tfg.licensoft.licenses;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tfg.licensoft.products.Product;


public interface LicenseRepository  extends JpaRepository<License, Long> {
	License findBySerial(String serial);
	List<License> findByProduct(Product product);
	License findBySerialAndProduct(String serial, Product product);
	List<License> findByOwner(String owner);
	List<License> findByProductAndActiveAndOwner(Product product, boolean active, String owner);
	List<License> findByProductAndOwner(Product product, String owner);
	List<License> findByProductAndOwnerAndType(Product product, String owner, String type);
	License findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active);
	License findBySerialAndProductAndActive(String serial, Product product,boolean active);

}
