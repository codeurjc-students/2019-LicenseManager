package tfg.licensoft.api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.tools.EmailSenderTool;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;

/**
 * This class is used to provide REST endpoints to logIn and logOut to the
 * service. These endpoints are used by Angular SPA client application.
 * 
 * NOTE: This class is not intended to be modified by app developer.
 */
@CrossOrigin 
@RestController
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);


	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private StripeServices stripeServ;
	
	@Autowired
	private EmailSenderTool emailSender;
	
	@Autowired 
	private GeneralController generalController;
	
	
	@RequestMapping("/api/logIn")
	public ResponseEntity<User> logIn(HttpServletRequest req) {
		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			User loggedUser = userComponent.getLoggedUser();
			log.info("Logged as " + loggedUser.getName());
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		}
	}

	@RequestMapping("/api/logOut")
	public ResponseEntity<Boolean> logOut(HttpSession session) {
		if (!userComponent.isLoggedUser()) {
			log.info("No user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();

			log.info("Logged out");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/api/register/{user}/{pass1}/{pass2}/{email}", method= RequestMethod.POST)
	public ResponseEntity<User> register(Model model, @PathVariable String user, @PathVariable String pass1,
			@PathVariable String pass2, @PathVariable String email,HttpServletRequest request, HttpServletResponse httpServletResponse) {
		User newUser =userServ.findByName(user);
		User newUserEmail =userServ.findByEmail(email);

		if ((pass1.equals(pass2)) && (newUser == null) && (newUserEmail == null)) {
			Map<String,Object> customerParameter = new HashMap<String,Object>();
			customerParameter.put("name", user);
			customerParameter.put("email",email);
			try {
				Customer customer = this.stripeServ.createCustomer(customerParameter);
				userServ.save(new User(email,customer.getId(),user, pass1,"ROLE_USER"));
			} catch (StripeException e) {
				e.printStackTrace();
			}
			try {
				request.login(user, pass1);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			String appName = this.generalController.appName;
			String sub = "New account created - " + appName;
			/*String msg = "Welcome to a Licensoft-Web! \n"
					+ "These are your credentials: \n" 
					+ "Username: " + user + "\n"
							+ "Password: "+ pass1;*/
			String htmlCodeEmail= 
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
					"<html style=\"width:100%;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\">\r\n" + 
					"\r\n" + 
					"<head>\r\n" + 
					"    <meta charset=\"UTF-8\">\r\n" + 
					"    <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\r\n" + 
					"    <meta name=\"x-apple-disable-message-reformatting\">\r\n" + 
					"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
					"    <meta content=\"telephone=no\" name=\"format-detection\">\r\n" + 
					"    <title>Nueva plantilla de correo electrónico 2020-04-16</title>\r\n" + 
					"    <!--[if (mso 16)]><style type=\"text/css\">    a {text-decoration: none;}    </style><![endif]-->\r\n" + 
					"    <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->\r\n" + 
					"    <!--[if !mso]><!-- -->\r\n" + 
					"    <link href=\"https://fonts.googleapis.com/css?family=Roboto:400,400i,700,700i\" rel=\"stylesheet\">\r\n" + 
					"    <link href=\"https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,400i,700,700i\" rel=\"stylesheet\">\r\n" + 
					"    <!--<![endif]-->\r\n" + 
					"    <style type=\"text/css\">\r\n" + 
					"        @media only screen and (max-width:600px) {\r\n" + 
					"            .st-br {\r\n" + 
					"                padding-left: 10px !important;\r\n" + 
					"                padding-right: 10px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            p,\r\n" + 
					"            ul li,\r\n" + 
					"            ol li,\r\n" + 
					"            a {\r\n" + 
					"                font-size: 16px !important;\r\n" + 
					"                line-height: 150% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h1 {\r\n" + 
					"                font-size: 30px !important;\r\n" + 
					"                text-align: center;\r\n" + 
					"                line-height: 120% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h2 {\r\n" + 
					"                font-size: 26px !important;\r\n" + 
					"                text-align: center;\r\n" + 
					"                line-height: 120% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h3 {\r\n" + 
					"                font-size: 20px !important;\r\n" + 
					"                text-align: center;\r\n" + 
					"                line-height: 120% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h1 a {\r\n" + 
					"                font-size: 30px !important;\r\n" + 
					"                text-align: center\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h2 a {\r\n" + 
					"                font-size: 26px !important;\r\n" + 
					"                text-align: center\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            h3 a {\r\n" + 
					"                font-size: 20px !important;\r\n" + 
					"                text-align: center\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-menu td a {\r\n" + 
					"                font-size: 14px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-header-body p,\r\n" + 
					"            .es-header-body ul li,\r\n" + 
					"            .es-header-body ol li,\r\n" + 
					"            .es-header-body a {\r\n" + 
					"                font-size: 16px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-footer-body p,\r\n" + 
					"            .es-footer-body ul li,\r\n" + 
					"            .es-footer-body ol li,\r\n" + 
					"            .es-footer-body a {\r\n" + 
					"                font-size: 14px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-infoblock p,\r\n" + 
					"            .es-infoblock ul li,\r\n" + 
					"            .es-infoblock ol li,\r\n" + 
					"            .es-infoblock a {\r\n" + 
					"                font-size: 12px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            *[class=\"gmail-fix\"] {\r\n" + 
					"                display: none !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-txt-c,\r\n" + 
					"            .es-m-txt-c h1,\r\n" + 
					"            .es-m-txt-c h2,\r\n" + 
					"            .es-m-txt-c h3 {\r\n" + 
					"                text-align: center !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-txt-r,\r\n" + 
					"            .es-m-txt-r h1,\r\n" + 
					"            .es-m-txt-r h2,\r\n" + 
					"            .es-m-txt-r h3 {\r\n" + 
					"                text-align: right !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-txt-l,\r\n" + 
					"            .es-m-txt-l h1,\r\n" + 
					"            .es-m-txt-l h2,\r\n" + 
					"            .es-m-txt-l h3 {\r\n" + 
					"                text-align: left !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-txt-r img,\r\n" + 
					"            .es-m-txt-c img,\r\n" + 
					"            .es-m-txt-l img {\r\n" + 
					"                display: inline !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-button-border {\r\n" + 
					"                display: block !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            a.es-button {\r\n" + 
					"                font-size: 16px !important;\r\n" + 
					"                display: block !important;\r\n" + 
					"                border-left-width: 0px !important;\r\n" + 
					"                border-right-width: 0px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-btn-fw {\r\n" + 
					"                border-width: 10px 0px !important;\r\n" + 
					"                text-align: center !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-adaptive table,\r\n" + 
					"            .es-btn-fw,\r\n" + 
					"            .es-btn-fw-brdr,\r\n" + 
					"            .es-left,\r\n" + 
					"            .es-right {\r\n" + 
					"                width: 100% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-content table,\r\n" + 
					"            .es-header table,\r\n" + 
					"            .es-footer table,\r\n" + 
					"            .es-content,\r\n" + 
					"            .es-footer,\r\n" + 
					"            .es-header {\r\n" + 
					"                width: 100% !important;\r\n" + 
					"                max-width: 600px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-adapt-td {\r\n" + 
					"                display: block !important;\r\n" + 
					"                width: 100% !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .adapt-img {\r\n" + 
					"                width: 100% !important;\r\n" + 
					"                height: auto !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p0 {\r\n" + 
					"                padding: 0px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p0r {\r\n" + 
					"                padding-right: 0px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p0l {\r\n" + 
					"                padding-left: 0px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p0t {\r\n" + 
					"                padding-top: 0px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p0b {\r\n" + 
					"                padding-bottom: 0 !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-m-p20b {\r\n" + 
					"                padding-bottom: 20px !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-mobile-hidden,\r\n" + 
					"            .es-hidden {\r\n" + 
					"                display: none !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-desk-hidden {\r\n" + 
					"                display: table-row !important;\r\n" + 
					"                width: auto !important;\r\n" + 
					"                overflow: visible !important;\r\n" + 
					"                float: none !important;\r\n" + 
					"                max-height: inherit !important;\r\n" + 
					"                line-height: inherit !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            .es-desk-menu-hidden {\r\n" + 
					"                display: table-cell !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            table.es-table-not-adapt,\r\n" + 
					"            .esd-block-html table {\r\n" + 
					"                width: auto !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            table.es-social {\r\n" + 
					"                display: inline-block !important\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"            table.es-social td {\r\n" + 
					"                display: inline-block !important\r\n" + 
					"            }\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .rollover:hover .rollover-first {\r\n" + 
					"            max-height: 0px !important;\r\n" + 
					"            display: none !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .rollover:hover .rollover-second {\r\n" + 
					"            max-height: none !important;\r\n" + 
					"            display: block !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        #outlook a {\r\n" + 
					"            padding: 0;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .ExternalClass {\r\n" + 
					"            width: 100%;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .ExternalClass,\r\n" + 
					"        .ExternalClass p,\r\n" + 
					"        .ExternalClass span,\r\n" + 
					"        .ExternalClass font,\r\n" + 
					"        .ExternalClass td,\r\n" + 
					"        .ExternalClass div {\r\n" + 
					"            line-height: 100%;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .es-button {\r\n" + 
					"            mso-style-priority: 100 !important;\r\n" + 
					"            text-decoration: none !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        a[x-apple-data-detectors] {\r\n" + 
					"            color: inherit !important;\r\n" + 
					"            text-decoration: none !important;\r\n" + 
					"            font-size: inherit !important;\r\n" + 
					"            font-family: inherit !important;\r\n" + 
					"            font-weight: inherit !important;\r\n" + 
					"            line-height: inherit !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .es-desk-hidden {\r\n" + 
					"            display: none;\r\n" + 
					"            float: left;\r\n" + 
					"            overflow: hidden;\r\n" + 
					"            width: 0;\r\n" + 
					"            max-height: 0;\r\n" + 
					"            line-height: 0;\r\n" + 
					"            mso-hide: all;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .es-button-border:hover {\r\n" + 
					"            border-style: solid solid solid solid !important;\r\n" + 
					"            background: #d6a700 !important;\r\n" + 
					"            border-color: #42d159 #42d159 #42d159 #42d159 !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"        .es-button-border:hover a.es-button {\r\n" + 
					"            background: #d6a700 !important;\r\n" + 
					"            border-color: #d6a700 !important;\r\n" + 
					"        }\r\n" + 
					"\r\n" + 
					"    </style>\r\n" + 
					"</head>\r\n" + 
					"\r\n" + 
					"<body style=\"width:100%;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\">\r\n" + 
					"    <div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6;\">\r\n" + 
					"        <!--[if gte mso 9]><v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\"> <v:fill type=\"tile\" color=\"#f6f6f6\"></v:fill> </v:background><![endif]-->\r\n" + 
					"        <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;\">\r\n" + 
					"            <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                <td class=\"st-br\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                    <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top;\">\r\n" + 
					"                        <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                            <td align=\"center\" style=\"padding:0;Margin:0;background-image:url(https://fxgfij.stripocdn.email/content/guids/CABINET_0185ec3caf0610f4b9817aa1405149a0/images/20841560930387653.jpg);background-color:transparent;background-position:center bottom;background-repeat:no-repeat;\" bgcolor=\"transparent\" background=\"https://fxgfij.stripocdn.email/content/guids/CABINET_0185ec3caf0610f4b9817aa1405149a0/images/20841560930387653.jpg\">\r\n" + 
					"                                <!--[if gte mso 9]><v:rect xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"true\" stroke=\"false\" style=\"mso-width-percent:1000;height:204px;\">\r\n" + 
					"<v:fill type=\"tile\" src=\"https://pics.esputnik.com/repository/home/17278/common/images/1546958148946.jpg\" color=\"#343434\" origin=\"0.5, 0\" position=\"0.5,0\"></v:fill><v:textbox inset=\"0,0,0,0\"><![endif]-->\r\n" + 
					"                                <div>\r\n" + 
					"                                    <table bgcolor=\"transparent\" class=\"es-header-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n" + 
					"                                        <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                            <td align=\"left\" bgcolor=\"transparent\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;background-color:transparent;\">\r\n" + 
					"                                                <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n" + 
					"                                                    <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                        <td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n" + 
					"                                                                <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                    <td align=\"left\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                                                                        <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:104px;font-family:'Summertime', 'Summertime', helvetica, arial, sans-serif;line-height:81px;color:#FFFFFF;\">&nbsp; &nbsp; &nbsp;"+appName+"</p>\r\n" + 
					"                                                                    </td>\r\n" + 
					"                                                                </tr>\r\n" + 
					"                                                                <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                    <td align=\"center\" height=\"63\" style=\"padding:0;Margin:0;\"></td>\r\n" + 
					"                                                                </tr>\r\n" + 
					"                                                            </table>\r\n" + 
					"                                                        </td>\r\n" + 
					"                                                    </tr>\r\n" + 
					"                                                </table>\r\n" + 
					"                                            </td>\r\n" + 
					"                                        </tr>\r\n" + 
					"                                    </table>\r\n" + 
					"                                </div>\r\n" + 
					"                                <!--[if gte mso 9]></v:textbox></v:rect><![endif]-->\r\n" + 
					"                            </td>\r\n" + 
					"                        </tr>\r\n" + 
					"                    </table>\r\n" + 
					"                    <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\r\n" + 
					"                        <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                            <td align=\"center\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                                <table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n" + 
					"                                    <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                        <td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;background-position:center center;\">\r\n" + 
					"                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n" + 
					"                                                <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                    <td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n" + 
					"                                                            <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                <td align=\"left\" class=\"es-m-txt-c\" style=\"padding:0;Margin:0;\">\r\n" + 
					"                                                                    <h1 style=\"Margin:0;line-height:36px;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;font-size:30px;font-style:normal;font-weight:bold;color:#212121;text-align:left;\">Welcome to "+appName+"!</h1>\r\n" + 
					"                                                                </td>\r\n" + 
					"                                                            </tr>\r\n" + 
					"                                                            <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                <td align=\"left\" class=\"es-m-txt-c\" style=\"padding:0;Margin:0;padding-top:20px;\">\r\n" + 
					"                                                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:18px;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#999999;\">\r\n" + 
					"                                                                        These are your Login Credentials:</p>\r\n" + 
					"                                                                </td>\r\n" + 
					"                                                            </tr>\r\n" + 
					"                                                            <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                <td align=\"left\" class=\"es-m-txt-c\" style=\"padding:0;Margin:0;padding-top:20px;\">\r\n" + 
					"                                                                    <p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:20px;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:30px;color:#131313;\"><span style=\"color:#D3D3D3;\">Your username:&nbsp;</span><strong>"+user+"</strong><br><span style=\"color:#D3D3D3;\">Your password:</span><strong>"+pass1+"</strong></p>\r\n" + 
					"                                                                </td>\r\n" + 
					"                                                            </tr>\r\n" + 
					"                                                            <tr style=\"border-collapse:collapse;\">\r\n" + 
					"                                                                <td align=\"left\" class=\"es-m-txt-c\" style=\"padding:0;Margin:0;padding-top:20px;padding-bottom:20px;\"><span class=\"es-button-border\" style=\"border-style:solid;border-color:#2CB543;background:#FFC80A;border-width:0px;display:inline-block;border-radius:3px;width:auto;\">\r\n" + 
					"                                                                        <a href=\"http://localhost:80\" class=\"es-button\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;font-size:18px;color:#FFFFFF;border-style:solid;border-color:#FFC80A;border-width:15px 30px;display:inline-block;background:#FFC80A;border-radius:3px;font-weight:normal;font-style:normal;line-height:22px;width:auto;text-align:center;\">Go to web</a></span></td>\r\n" + 
					"                                                            </tr>\r\n" + 
					"                                                        </table>\r\n" + 
					"                                                    </td>\r\n" + 
					"                                                </tr>\r\n" + 
					"                                            </table>\r\n" + 
					"                                        </td>\r\n" + 
					"                                    </tr>\r\n" + 
					"                                </table>\r\n" + 
					"                            </td>\r\n" + 
					"                        </tr>\r\n" + 
					"                    </table>\r\n" + 
					"                </td>\r\n" + 
					"            </tr>\r\n" + 
					"        </table>\r\n" + 
					"    </div>\r\n" + 
					"</body>\r\n" + 
					"\r\n" + 
					"</html>\r\n" + 
					"";
			this.emailSender.sendHtmlEmail(sub, htmlCodeEmail, email);
			return new ResponseEntity<User>(newUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(newUser, HttpStatus.CONFLICT);
		}
		
		
	}
	
	

	
	
	@RequestMapping(value="/api/users", method=RequestMethod.GET)
	public ResponseEntity<Page<User>> getAllUsers(Pageable page){
		Page<User> users= userServ.findAll(page);
		return new ResponseEntity<Page<User>>(users,HttpStatus.OK);
	}
	
	@GetMapping("/api/getUserLogged")
	public ResponseEntity<User> getUserLogged( HttpServletRequest request) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		System.out.println("??? ->  "+authorities + "  " + SecurityContextHolder.getContext().getAuthentication());
		return new ResponseEntity<User>(this.userComponent.getLoggedUser(),HttpStatus.OK);

	}
	
}
