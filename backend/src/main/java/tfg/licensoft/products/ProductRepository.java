package tfg.licensoft.products;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByName(String name);
	List<Product> findAll(); 
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:search% AND p.active = TRUE")
	List<Product> findSearch( @Param("search") String search);
	List<Product> findByActive(boolean active);
}
