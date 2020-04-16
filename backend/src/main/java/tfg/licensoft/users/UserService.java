package tfg.licensoft.users;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Transactional
@Service
public class UserService {

	@Autowired
	private UserRepository userRep;

	public Optional<User> findOne(long id) {
		return userRep.findById(id);
	}

	public Page<User> findAll(Pageable page) {
		return userRep.findAll(page);
	}

	public List<User> findAll() {
		return userRep.findAll();
	}

	public void save(User user) {
		userRep.save(user);
	}

	public void delete(long id) {
		userRep.deleteById(id);
	}

	public User findByName(String name) {
		return userRep.findByName(name);
	}
	
	public User findByEmail(String email) {
		return userRep.findByEmail(email);
	}
	
	public User findByCustomerStripeId(String customerStripeId) {
		return userRep.findByCustomerStripeId(customerStripeId);
	}
	
}
