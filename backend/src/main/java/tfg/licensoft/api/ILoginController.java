package tfg.licensoft.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tfg.licensoft.users.User;

public interface ILoginController {
	
	@ApiOperation(value = "Log In")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User"),
            @ApiResponse(code = 401, message = "User Unauthorized")
    })
	ResponseEntity<User> logIn(HttpServletRequest req);


	@ApiOperation(value = "Log Out")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User"),
            @ApiResponse(code = 401, message = "User Unauthorized")
    })
	ResponseEntity<Boolean> logOut(HttpSession session);
	
	
	@ApiOperation(value = "Register")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User"),
            @ApiResponse(code = 409, message = "There is already a user with the same name or email")
    })
	ResponseEntity<User> register(Model model, @PathVariable String user, @PathVariable String pass1,
			@PathVariable String pass2, @PathVariable String email,HttpServletRequest request, HttpServletResponse httpServletResponse);
	
	
	
	@ApiOperation(value = "Returns all the users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Page<User>"),
    })
	ResponseEntity<Page<User>> getAllUsers(Pageable page);
	

	@ApiOperation(value = "Returns the actual user logged")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User"),
    })
	ResponseEntity<User> getUserLogged( HttpServletRequest request);

	


}
