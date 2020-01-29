package tfg.licensoft.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByName(String name);
	Page<Product> findAll(Pageable page); 
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:search%")
	Page<Product> findSearch(Pageable page, @Param("search") String search);
}
