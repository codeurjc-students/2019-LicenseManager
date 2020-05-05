package tfg.licensoft.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tfg.licensoft.api.UserController.SimpleResponse;
import tfg.licensoft.licenses.License;
import tfg.licensoft.products.Product;

public interface IUserController {
	
	@ApiOperation(value = "Posts a PaymentMethod (credit card for Stripe), and links it to the Customer(Stripe)/User(Licensoft)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Id of the PaymentMethod(Stripe) created"),
            @ApiResponse(code = 401, message = "User to attach the Card does not exist"),
            @ApiResponse(code = 403, message = "User to attach the Card is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error")
    })
	ResponseEntity<SimpleResponse> addCardStripeElements(@PathVariable String userName,@PathVariable String tokenId);

	@ApiOperation(value = "Updates a User setting the given PaymentMethod (credit card for Stripe)='Default'")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "True"),
            @ApiResponse(code = 401, message = "User to be updated does not exist"),
            @ApiResponse(code = 403, message = "User to be updated is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error")
    })
	ResponseEntity<Boolean> setDefaultPaymentMethod(@PathVariable String userName, @PathVariable String paymentMethodId);

	@ApiOperation(value = "Gets the Default PaymentMethod for a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Id of the Default PaymentMethod(Stripe) of the User"),
            @ApiResponse(code = 401, message = "User to get the default PaymentMethod does not exist"),
            @ApiResponse(code = 403, message = "User to get the default PaymentMethod is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error")
    })
	ResponseEntity<SimpleResponse> getDefaultPaymentMethod(@PathVariable String userName);
	
	
	@ApiOperation(value = "Subscribes a given User to a given Product with a Trial Period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License"),
            @ApiResponse(code = 401, message = "User to get subscribed does not exist"),
            @ApiResponse(code = 403, message = "User to get subscribed is not the same that does the request"),
            @ApiResponse(code = 404, message = "Stripe Error"),
            @ApiResponse(code = 500, message = "Product to subscribe Not Found")
    })
	ResponseEntity<License> addTrial(@PathVariable String productName, @PathVariable String userName,  @PathVariable String token);


	@ApiOperation(value = "Subscribes a given User to a given Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License"),
            @ApiResponse(code = 401, message = "User to get subscribed does not exist"),
            @ApiResponse(code = 403, message = "User to get subscribed is not the same that does the request"),
            @ApiResponse(code = 428, message = "The user doesn't have attached a Payment Method"),
            @ApiResponse(code = 500, message = "Stripe Error"),
            @ApiResponse(code = 404, message = "Product to subscribe Not Found")
    })
	ResponseEntity<License> addSubscription(@PathVariable String productName, @PathVariable String typeSubs, @PathVariable String userName, @PathVariable boolean automaticRenewal, @PathVariable String pmId);

	
	@ApiOperation(value = "Returns the PaymentMethods attached to a given User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of PaymentMethods"),
            @ApiResponse(code = 401, message = "User to get the PaymentMethods does not exist"),
            @ApiResponse(code = 403, message = "User to get the PaymentMethods is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
	ResponseEntity<List<PaymentMethod>> getCardsFromUser(@PathVariable String user);
	
	
	@ApiOperation(value = "Deletes a given PaymentMethod of a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "True if the PaymentMethod has been deleted"),
            @ApiResponse(code = 401, message = "User to delete the PaymentMethod does not exist"),
            @ApiResponse(code = 403, message = "User to delete the PaymentMethod is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
	ResponseEntity<SimpleResponse> deleteStripeCard(@PathVariable String user, @PathVariable String paymentMethodId);

	
	
	@ApiOperation(value = "Creates a Payment Intent in Stripe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "(String) Payment Intent id in Stripe"),
            @ApiResponse(code = 401, message = "User to be linked to the Payment Intent does not exist"),
            @ApiResponse(code = 403, message = "User to be linked to the Payment Intent is not the same that does the request"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
    ResponseEntity<String> payment(@PathVariable String userName,@RequestBody Product product, @PathVariable String tokenId) throws StripeException;


	@ApiOperation(value = "Confirms a previusly created Payment Intent in Stripe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License (if type='RequiresAction', 3DSecure Confirmation is needed"),
            @ApiResponse(code = 401, message = "User to be linked to the Payment Intent does not exist"),
            @ApiResponse(code = 403, message = "User to be linked to the Payment Intent is not the same that does the request"),
            @ApiResponse(code = 404, message = "Payment Intent to confirm Not Found"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
    ResponseEntity<License> confirm(@PathVariable String id, @PathVariable String userName, @PathVariable String productName) throws StripeException;

	
	
	@ApiOperation(value = "3DSecure Confirmation for a previous confirmation of a Payment Intent")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License"),
            @ApiResponse(code = 412, message = "3DSecure Confirmation failed"),
            @ApiResponse(code = 401, message = "User to be linked to the Payment Intent does not exist"),
            @ApiResponse(code = 403, message = "User to be linked to the Payment Intent is not the same that does the request"),
            @ApiResponse(code = 404, message = "Payment Intent to confirm Not Found"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
	ResponseEntity<License> confirm3dsPaymentResponse(@PathVariable String paymentIntentId, @PathVariable String username, @PathVariable String product,  @RequestParam Optional<String> type, 
    		@RequestParam Optional<String> subscriptionId,  @RequestParam Optional<String> automaticRenewal);


	@ApiOperation(value = "Returns the PaymentMethod associated to a Subscription of a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "(String) Payment Method Id"),
            @ApiResponse(code = 401, message = "User linked to the Subscription does not exist"),
            @ApiResponse(code = 403, message = "User linked to the Subscription is not the same that does the request"),
            @ApiResponse(code = 404, message = "Subscription to get the PaymentMethod Not Found"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
	ResponseEntity<SimpleResponse> getPaymentMethodOfSubscription(@PathVariable String username, @PathVariable String subsId);

	
	@ApiOperation(value = "Associates a new PaymentMethod to a Subscription of a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "(String) New Payment Method Id"),
            @ApiResponse(code = 401, message = "User linked to the Subscription does not exist"),
            @ApiResponse(code = 403, message = "User linked to the Subscription is not the same that does the request"),
            @ApiResponse(code = 404, message = "Subscription to get the PaymentMethod Not Found"),
            @ApiResponse(code = 500, message = "Stripe Error"),
    })
	ResponseEntity<SimpleResponse> postPaymentMethodOfSubscription(@PathVariable String username, @PathVariable String subsId, @PathVariable String pmId);
}
