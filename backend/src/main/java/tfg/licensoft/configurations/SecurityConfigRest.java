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

        http.antMatcher("/api/**");

		// User
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/user/**/card/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/login").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/register/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/**/addCard/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**/cards").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/**/buy/**/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/**/**/**/addSubscription/renewal/**").hasAnyRole("ADMIN","USER"); 
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/**/setDefault/**").hasAnyRole("ADMIN","USER"); 
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**/getDefault").hasAnyRole("ADMIN","USER"); 
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/**/**/addTrial/card/**").hasAnyRole("ADMIN","USER"); 
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/**/paymentIntent/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/**/confirm/**/product/**").hasAnyRole("ADMIN","USER");

        //Product
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/product/**/image").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/product/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/product/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/product/**/license/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/product/**/image").hasRole("ADMIN");

        //License  
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/licenses/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/licenses/product/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/licenses/**/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/licenses/product/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/licenses/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/licenses/cancelAtEnd/**/**").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/licenses/update/").hasAnyRole("ADMIN","USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/licenses/user/**/product/**").hasAnyRole("ADMIN","USER");
        
        //UsedCard
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/usedcard/checkUsed/***/***/***/product/***").hasAnyRole("ADMIN","USER");


        // Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> { });
		
		// Use HTTP basic authentication
		http.httpBasic();

        // Disable CSRF
        http.csrf().disable();

        http.logout().logoutUrl("/logout");
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthentication);
    }
    
}
