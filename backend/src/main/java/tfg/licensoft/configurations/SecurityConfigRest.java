package tfg.licensoft.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class SecurityConfigRest extends WebSecurityConfigurerAdapter{
   
    @Autowired
    protected UserRepositoryAuthProvider userAuthentication;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();

        http.antMatcher("/api/**");

		// User
	//	http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/logIn").hasAnyRole("USER","ADMIN");
      //  http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/login").permitAll();
     //   http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/register").permitAll();
        
       // http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/product/all").hasAnyRole("USER","ADMIN");
        

        http.authorizeRequests().anyRequest().permitAll();
        // Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> { });
		
		// Use HTTP basic authentication

        // Disable CSRF
        http.csrf().disable();

        http.logout().logoutUrl("/logout");
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthentication);
    }
    
}
