package tfg.licensoft.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tfg.licensoft.products.Product;

public interface IProductController {
	
	@ApiOperation(value = "Returns all the Products (opt. matching a search input)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Licenses of the Product"),
            @ApiResponse(code = 404, message = "Product Not Found")
    })
	public ResponseEntity<List<Product>> getProducts(HttpServletRequest req,@RequestParam Optional<String> search);

	
	@ApiOperation(value = "Returns the Product with the given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product of given name"),
            @ApiResponse(code = 404, message = "Product Not Found")
    })
	public ResponseEntity<Product> getProduct(@PathVariable String productName);



	@ApiOperation(value = "Posts a given Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product posted"),
            @ApiResponse(code = 409, message = "Product with the same name already exists"),
            @ApiResponse(code = 500, message = "Stripe Error")
    })
	public ResponseEntity<Product> postProduct(@RequestBody Product product);

	@ApiOperation(value = "Updates a given Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated"),
            @ApiResponse(code = 404, message = "Product to be updated does NOT exist"),
            @ApiResponse(code = 500, message = "Stripe Error")
    })
	ResponseEntity<Product> editProduct(@RequestBody Product product);

	
	@ApiOperation(value = "Deletes the Product with given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product deleted"),
            @ApiResponse(code = 404, message = "Product to be deleted does NOT exist"),
            @ApiResponse(code = 500, message = "Stripe Error")

    })
	ResponseEntity<Product> deleteProduct(@PathVariable String productName,HttpServletRequest request);

	
	@ApiOperation(value = "Returns the image of the given Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product deleted"),
            @ApiResponse(code = 404, message = "Product Not Found")
    })
	ResponseEntity<byte[]> getImage(@PathVariable String productName)throws IOException;
	
	@ApiOperation(value = "Posts the image for the given Product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product updated with the image"),
            @ApiResponse(code = 404, message = "Product Not Found"),
            @ApiResponse(code = 400, message = "File Not Found")

    })
	ResponseEntity<byte[]> postImage(@RequestBody MultipartFile file, @PathVariable String productName) throws Exception;

}
