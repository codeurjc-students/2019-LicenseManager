package tfg.licensoft.api;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;

import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.CardCollection;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.Order;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Plan;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import com.stripe.model.Token;
import com.stripe.param.SubscriptionCreateParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	@Autowired
	private LicenseService licenseServ;
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ProductService productServ;
	
	
	@PostMapping("/addCard/{tokenId}")
	public ResponseEntity<String> addCardStripeElements(@PathVariable String tokenId){
		User user = userComponent.getLoggedUser();
		try {
			Customer c = Customer.retrieve(user.getCustomerStripeId());
			Map<String,Object> source = new HashMap<String,Object>();
			source.put("source", tokenId);
			PaymentSource ps = c.getSources().create(source);
			return new ResponseEntity<String>(ps.getId(),HttpStatus.OK);

		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
 
	}
	
	//Will be subscribed to a M subscription with x free trial days
	@PutMapping("/{productName}/{userName}/addTrial/{days}/card/{token}")
	public ResponseEntity<License> addTrial(@PathVariable String productName, @PathVariable String userName, @PathVariable long days,  @PathVariable String token){
		
		
		
		User user = this.userServ.findByName(userName);
		Product product = this.productServ.findOne(productName);
		
		if(user!=null && product!=null) {
			try {
				String src = this.addCardStripeElements(token).getBody();

				SubscriptionCreateParams params =
						  SubscriptionCreateParams.builder()
						    .setCustomer(user.getCustomerStripeId())
						    .addItem(
						      SubscriptionCreateParams.Item.builder()
						        .setPlan(product.getPlans().get("M")) //Free trial is always monthly subscription
						        .build())
						    .setTrialPeriodDays(days)
						    .setDefaultSource(src)
						    .build();
				Subscription subscription = Subscription.create(params);
				
				License license = new License(true, "M", product, user.getName(),days);
				license.setCancelAtEnd(false);  //Trial Periods have automatic renewal
				license.setSubscriptionItemId(subscription.getItems().getData().get(0).getId());
				license.setSubscriptionId(subscription.getId());
				licenseServ.save(license);
				this.setTimerAndEndDate(license,days);
				return new ResponseEntity<License>(license,HttpStatus.OK);

			}catch(StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);

		}
	}


	
	@PutMapping("/{productName}/{typeSubs}/{userName}/addSubscription/renewal/{automaticRenewal}")
	public ResponseEntity<License> addSubscription(@PathVariable String productName, @PathVariable String typeSubs, @PathVariable String userName, @PathVariable boolean automaticRenewal){
		Product product = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		//User user = this.userServ.findByName(userName);
		Map<String,String> plans = product.getPlans();
				 
		String planId = plans.get(typeSubs);
		if (planId!=null && product!=null) {
				Map<String, Object> item = new HashMap<>();
				item.put("plan", planId);
				Map<String, Object> items = new HashMap<>();
				items.put("0", item);
				Map<String, Object> expand = new HashMap<>();
				expand.put("0", "latest_invoice.payment_intent");
				Map<String, Object> params = new HashMap<>();
				params.put("customer", user.getCustomerStripeId());
				params.put("items", items);
				params.put("expand", expand);
				params.put("cancel_at_period_end", !automaticRenewal);
				Subscription subscription;
				try {
					subscription = Subscription.create(params);
				} catch (StripeException e) { 
					e.printStackTrace();
					if(e.getCode().equals("resource_missing") && e.getMessage().contains("This customer has no attached payment source")) {
						return new ResponseEntity<License>(HttpStatus.PRECONDITION_REQUIRED); //The precondition is to have an attached payment source
					}else {
						return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				this.productServ.save(product);			
				License license = new License(true, typeSubs, product, user.getName());
				license.setCancelAtEnd(!automaticRenewal);
				license.setSubscriptionItemId(subscription.getItems().getData().get(0).getId());
				license.setSubscriptionId(subscription.getId());
				licenseServ.save(license);
				if(automaticRenewal) {
					this.setTimerAndEndDate(license,0);
				}
				return new ResponseEntity<License>(license,HttpStatus.OK);
			
		}else {
			return new ResponseEntity<License>(HttpStatus.BAD_REQUEST);
		}
	}
	
	private void setTimerAndEndDate(License license, long trialDays) {
		Timer time = new Timer();
		System.out.println(license.getEndDate());
		time.schedule(new TimerTask() {
			

			@Override
			public void run() {
				license.renew(trialDays);
				License newL = licenseServ.save(license);
				setTimerAndEndDate(newL,0);
				
			}
			
		}, 
				license.getEndDate()
				
		);
	}
	
	
	@PutMapping("/{userName}/buy/{productName}/{token}")
	public ResponseEntity<License> buyProduct(@PathVariable String productName, @PathVariable String userName, @PathVariable String token){
		Product product = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		System.out.println(user + " " + product);


		if(token!=null && product!=null && user!=null) {
			System.out.println("Dentro");
			List<Object> items = new ArrayList<>();
			Map<String, Object> item1 = new HashMap<>();
			item1.put("type", "sku");
			item1.put("parent", product.getSku());
			items.add(item1);
			Map<String, Object> params = new HashMap<>();
			params.put("currency", "eur");
			params.put("email", user.getName()+"@test.com");
			params.put("items", items);
			params.put("customer", user.getCustomerStripeId());

			try {
				Order order = Order.create(params);
				Map<String, Object> paramsNew = new HashMap<>();
				paramsNew.put("source", token);

				order = order.pay(paramsNew); //Paying order , state changed to Paid
				
				paramsNew.clear();
				
				paramsNew.put("status", "fulfilled");
				
				License license = new License(true, "L", product, user.getName());
				licenseServ.save(license);
				
				order = order.update(paramsNew);  //Order state changed to Fulfilled
				
				return new ResponseEntity<>(license,HttpStatus.OK);
			} catch (StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.CONFLICT);

			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
	
	@GetMapping("/{user}/cards")
	private ResponseEntity<List<PaymentSource>> getCardsFromUser(@PathVariable String user) {
		User u = this.userServ.findByName(user);
		if(u!=null) {
			try {
				Customer customer =
						  Customer.retrieve(u.getCustomerStripeId());
					Map<String, Object> params = new HashMap<>();
					params.put("object", "card");
	
					List<PaymentSource> l = customer.getSources().list(params).getData();				
					return new ResponseEntity<>(l,HttpStatus.OK);
			}catch (StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
				
	}
	
	@DeleteMapping("/{user}/card/{cardStripeId}")
	private ResponseEntity deleteStripeCard(@PathVariable String user, @PathVariable String cardStripeId) {
		User u = this.userServ.findByName(user);
		if(u!=null) {
			try {
					Customer customer =
					Customer.retrieve(u.getCustomerStripeId());
					Card card = (Card) customer.getSources().retrieve(cardStripeId);
					card.delete();
					return new ResponseEntity<>(HttpStatus.OK);
			}catch(StripeException e) {
				
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	//TODO de momento no se usa, lo dejo por si necesitamos la invoice para imprimir o lo que sea
	@GetMapping("/{user}/invoice/subs/{subscriptionId}")
	private ResponseEntity<String> getInvoicePreview(@PathVariable String user, @PathVariable String subscriptionId){
		System.out.println("---");
		User u = this.userServ.findByName(user);
		//User uLogged = this.userComponent.getLoggedUser();
		if(u!=null) {
			try {
				Map<String, Object> invoiceParams = new HashMap<>();
				invoiceParams.put("customer", u.getCustomerStripeId());
				invoiceParams.put("subscription", subscriptionId);
				Invoice in = Invoice.upcoming(invoiceParams);
				
				return new ResponseEntity<>(in.toJson(),HttpStatus.OK);
			}catch(StripeException e) {
				
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

}
