package tfg.licensoft.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import tfg.licensoft.licenses.License;

public interface ILicenseController {
	
	@ApiOperation(value = "Returns the licenses of a Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Licenses of the Product"),
            @ApiResponse(code = 404, message = "Product Not Found")
    })
	ResponseEntity<List<License>> getLicensesOfProduct(@PathVariable String product);
	
	
	@ApiOperation(value = "Returns the License of a Product with a specific serial")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License of the Product with the given serial"),
            @ApiResponse(code = 404, message = "License Not Found")
    })
	ResponseEntity<License> getLicenseBySerialAndProduct(@PathVariable String serial, @PathVariable String product);

	
	
	@ApiOperation(value = "Returns Licenses of a User.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Licenses of a User"),
            @ApiResponse(code = 404, message = "User Not Found")
    })
	ResponseEntity<List<License>> getLicensesOfUser(@PathVariable String userName);

	
	@ApiOperation(value = "Updates a License of a Product to able/disable automatic renewal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "License of the Product with the given serial"),
            @ApiResponse(code = 500, message = "Stripe error"),
            @ApiResponse(code = 404, message = "License or Product Not Found")
    })
	ResponseEntity<License> cancelAtEndLicense(@PathVariable String serial, @PathVariable String product);

	
	@ApiOperation(value = "Returns Licenses of a Product that a User owns.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Licenses of the Product, that the User owns"),
            @ApiResponse(code = 404, message = "User or Product Not Found")
    })
	ResponseEntity<List<License>> getLicensesOfUserAndProduct(@PathVariable String productName, @PathVariable String userName);


}
