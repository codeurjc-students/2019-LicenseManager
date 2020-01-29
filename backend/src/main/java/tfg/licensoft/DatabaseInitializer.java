package tfg.licensoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@Component
public class DatabaseInitializer {



	@Autowired
	UserService userServ;

	@Autowired
	LicenseService licenseServ;
	
	@Autowired
	ProductService productServ;

	@PostConstruct
	public void init() throws StripeException {


	 
		// users
		Customer c1 = Customer.retrieve("cus_GCfRnHS286gdq9");
		userServ.save(new User(c1.getId(),"customer", "c", "ROLE_USER"));
	

		Customer c2 = Customer.retrieve("cus_GCfRldTX850sXN");
		userServ.save(new User(c2.getId(),"user", "u", "ROLE_USER"));
		

		Customer c3 = Customer.retrieve("cus_GCfRu2cBr7Xkkh");
		userServ.save(new User(c3.getId(),"C1", "c1", "ROLE_USER"));

		

		Customer c4 = Customer.retrieve("cus_GCfRVJhDGwsjpi");
		userServ.save(new User(c4.getId(),"admin", "a", "ROLE_USER", "ROLE_ADMIN"));	
		
		
		//products
		
		Product p1 =new Product("sw");
		Map<String, Object> params = new HashMap<String, Object>();
		/*params.put("name", "sw");
		params.put("type", "service");
		com.stripe.model.Product productStripe = com.stripe.model.Product.create(params);*/
		
		p1.setProductStripeId("prod_GEsqozxWp3psL6");
		p1.setPhotoAvailable(true);
		p1.setPhotoSrc("assets/logo.jpg");
		p1.setDescription("Al contrario del pensamiento popular, el texto de Lorem Ipsum no es simplemente texto aleatorio. Tiene sus raices en una pieza clásica de la literatura del Latin, que data del año 45 antes de Cristo, haciendo que este adquiera mas de 2000 años de antiguedad. Richard McClintock, un profesor de Latin de la Universidad de Hampden-Sydney en Virginia, encontró una de las palabras más oscuras de la lengua del latín, \"consecteur\", en un pasaje de Lorem Ipsum, y al seguir leyendo distintos textos del latín, descubrió la fuente indudable. Lorem Ipsum viene de las secciones 1.10.32 y 1.10.33 de \"de Finnibus Bonorum et Malorum\" (Los Extremos del Bien y El Mal) por Cicero, escrito en el año 45 antes de Cristo. Este libro es un tratado de teoría de éticas, muy popular durante el Renacimiento. La primera linea del Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", viene de una linea en la sección 1.10.32\r\n" + 
				"\r\n" + 
				"El trozo de texto estándar de Lorem Ipsum usado desde el año 1500 es reproducido debajo para aquellos interesados. Las secciones 1.10.32 y 1.10.33 de \"de Finibus Bonorum et Malorum\" por Cicero son también reproducidas en su forma original exacta, acompañadas por versiones en Inglés de la traducción realizada en 1914 por H. Rackham.");

		/*params.put("currency", "eur");
		params.put("interval", "year");
		params.put("product", "prod_GEsqozxWp3psL6");
		params.put("nickname", "Anual Plan");
		params.put("amount", 150000);
		Plan plan1Y = Plan.create(params);*/
		p1.getPlans().put("A","plan_GEtKTGQZSjlPWa");
		
	/*	params.put("currency", "eur");
		params.put("interval", "day");
		params.put("product", "prod_GEsqozxWp3psL6");
		params.put("nickname", "Day Plan");
		params.put("amount", 63);
		Plan plan1D = Plan.create(params);*/
		p1.getPlans().put("D","plan_GEtKMzSCG3jEbp");
		
	/*	params.put("currency", "eur");
		params.put("interval", "month");
		params.put("product", "prod_GEsqozxWp3psL6");
		params.put("nickname", "Anual Plan");
		params.put("amount", 15000);
		Plan plan1M = Plan.create(params);*/
		p1.getPlans().put("M","plan_GEtKkz0b1Mv4al");
	
		p1.getTypeSubs().add("A");
		p1.getTypeSubs().add("M");
		p1.getTypeSubs().add("D");
		
		p1.getPlansPrices().put("A", 1500.00);
		p1.getPlansPrices().put("M", 150.00);
		p1.getPlansPrices().put("D",  .63);

		p1.setWebLink("www.linkSW.com");
		this.productServ.save(p1);

		
		
		
		Product p2 = new Product("hw");
		/*params.put("name", "hw");
		params.put("type", "service");
		com.stripe.model.Product productStripe2 = com.stripe.model.Product.create(params);
		*/
		
		p2.setProductStripeId("prod_GEsq8txRlM9Ng2");
		p2.setDescription("THIS IS A DESCRIPTION ---------------------------------------------------------------------------------.");

		
		/*params.put("currency", "eur");
		params.put("interval", "year");
		params.put("product", "prod_GEsq8txRlM9Ng2");
		params.put("nickname", "Anual Plan");
		params.put("amount", 150000);
		Plan plan2Y = Plan.create(params);*/
		p2.getPlans().put("A","plan_GEtKIyghW6lQL7");
		
		/*params.put("currency", "eur");
		params.put("interval", "day");
		params.put("product", "prod_GEsq8txRlM9Ng2");
		params.put("nickname", "Day Plan");
		params.put("amount", 63);
		Plan plan2D = Plan.create(params);*/
		p2.getPlans().put("D","plan_GEtKeuzPNyTjUl");

		/*params.put("currency", "eur");
		params.put("interval", "month");
		params.put("product", "prod_GEsq8txRlM9Ng2");
		params.put("nickname", "Month Plan");
		params.put("amount", 15000);
		Plan plan2M = Plan.create(params);*/
		p2.getPlans().put("M","plan_GEtKBmPeXeqX7e");
		
		p2.getTypeSubs().add("A");
		p2.getTypeSubs().add("M");
		p2.getTypeSubs().add("D");


		this.productServ.save(p2);

		
		//licenses
		licenseServ.save(new License("XX-X1",false,"A",p1,null));
		
		licenseServ.save(new License("XX-X2",true,"A",p1,"Customer"));
		licenseServ.save(new License("XX-X3",true,"M",p1, "C1"));
		licenseServ.save(new License("XX-X4",true,"L",p1,"C1"));
		licenseServ.save(new License("XX-X1",false,"A",p2,null));
		licenseServ.save(new License("XX-X2",true,"M",p2,"C1"));
		licenseServ.save(new License("XX-X3",false,"A",p2,null));

		
		
		
		
	}

}
