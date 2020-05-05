package tfg.licensoft.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tfg.licensoft.usedCards.UsedCard;

public interface IUsedCardController {
	
	
	
	@ApiOperation(value = "Checks if a given card has been used to subscribe to a product's trial period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "true/false -> used/not used")
    })
	ResponseEntity<Boolean> checkIfUsed(@PathVariable int last4, @PathVariable int expMonth, @PathVariable int expYear, @PathVariable String product);

	@ApiOperation(value = "Marks a given card used to subscribe to a product's trial period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "UsedCard")
    })
	ResponseEntity<UsedCard> postUsedCard(@PathVariable int last4, @PathVariable int expMonth, @PathVariable int expYear, @PathVariable String product);


}
