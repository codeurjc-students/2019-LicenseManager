package tfg.licensoft.stripe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tfg.licensoft.users.User;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StripeCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String number;
	private String exp_year;
	private String exp_month;
	private String owner;
	private String cvv;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	
	
	public StripeCard() {}
	
	public StripeCard(String number, String exp_year, String exp_month, String owner, String cvv) {
		this.number = number;
		this.exp_year = exp_year;
		this.exp_month = exp_month;
		this.owner = owner;
		this.cvv= cvv;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getExp_year() {
		return exp_year;
	}
	public void setExp_year(String exp_year) {
		this.exp_year = exp_year;
	}
	public String getExp_month() {
		return exp_month;
	}
	public void setExp_month(String exp_month) {
		this.exp_month = exp_month;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
