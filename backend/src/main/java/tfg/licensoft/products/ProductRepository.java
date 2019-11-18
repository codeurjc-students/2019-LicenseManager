package tfg.licensoft.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByName(String name);
	Page<Product> findAll(Pageable page);
}
