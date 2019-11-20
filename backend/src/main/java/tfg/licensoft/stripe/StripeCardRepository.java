package tfg.licensoft.stripe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tfg.licensoft.users.User;


public interface StripeCardRepository extends JpaRepository<StripeCard, Long> {
	
	Page<StripeCard>findByUser(Pageable page,User user);

}
