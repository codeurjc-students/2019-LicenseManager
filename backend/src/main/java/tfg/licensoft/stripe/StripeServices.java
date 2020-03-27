package tfg.licensoft.stripe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Plan;
import com.stripe.model.Product;
import com.stripe.model.SetupIntent;
import com.stripe.model.Sku;
import com.stripe.model.Subscription;
import com.stripe.model.UsageRecord;
import com.stripe.net.RequestOptions;
import com.stripe.param.PlanCreateParams;
import com.stripe.param.SubscriptionCreateParams;

@Service
public class StripeServices {
	
	public Product retrieveProduct(String productId) throws StripeException {
		return Product.retrieve(productId);
	}
	
	public Customer createCustomer(Map<String,Object> customerParameter) throws StripeException {
		return Customer.create(customerParameter);
	}
	
	public Customer retrieveCustomer(String customerId) throws StripeException{
		return Customer.retrieve(customerId);
	}
	
	public Customer updateCustomer(Customer c, Map<String, Object> params) throws StripeException{
		return 	c.update(params);
	}
	
	public void updateSubscription(Subscription s,Map<String, Object> params) throws StripeException {
		s.update(params);
	}
	
	public Product createProduct(Map<String, Object> params) throws StripeException {
		return com.stripe.model.Product.create(params);
	}
	
	public Product updateProduct(Product p,Map<String, Object> params) throws StripeException{
		return p.update(params);
	}
	
	public Plan createPlan(Map<String, Object> params) throws StripeException{
		return Plan.create(params);
	}

	public Plan createPlan(PlanCreateParams params) throws StripeException{
		return Plan.create(params);
	}
	
	public Sku createSku(Map<String, Object> params) throws StripeException{
		return Sku.create(params);
	}
	
	public SetupIntent createSetupIntent(Map<String, Object> params) throws StripeException{
		return 	SetupIntent.create(params);
	}
	
	public PaymentMethod createPaymentMethod(Map<String, Object> params) throws StripeException{
		return PaymentMethod.create(params);
	}
	
	public PaymentMethod attachPaymentMethod (PaymentMethod paymentMethod, Map<String, Object> params) throws StripeException{
		return paymentMethod.attach(params);
	}
	
	public Subscription createSubscription(SubscriptionCreateParams params) throws StripeException {
		return Subscription.create(params);
	}
	
	public Subscription createSubscription(Map<String, Object> params) throws StripeException {
		return Subscription.create(params);
	}
	
	public Subscription retrieveSubscription(String subscriptionId) throws StripeException {
		return Subscription.retrieve(subscriptionId);
	}
	
	public PaymentMethodCollection getPaymentMethodCollection(Map<String, Object> params) throws StripeException {
		return PaymentMethod.list(params);
	}
	
	public PaymentMethod retrievePaymentMethod(String paymentMethodId) throws StripeException {
		return PaymentMethod.retrieve(paymentMethodId);
	}
	
	public PaymentMethod detachPaymentMethod(PaymentMethod paymentMethod) throws StripeException {
		return 	paymentMethod.detach();
	}
	
	public PaymentIntent createPaymentIntent(Map<String, Object> params) throws StripeException {
		return PaymentIntent.create(params);
	}
	
	public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
		return  PaymentIntent.retrieve(paymentIntentId);
	}
	
	public PaymentIntent confirmPaymentIntent(PaymentIntent paymentIntent, Map<String, Object> params  ) throws StripeException{
		return  paymentIntent.confirm(params);
	}
	
	public UsageRecord createOnSubscriptionItem(String subsItemId,Map<String, Object> usageRecordParams,RequestOptions options ) throws StripeException {
		return UsageRecord.createOnSubscriptionItem(subsItemId, usageRecordParams, options);
	}
}
