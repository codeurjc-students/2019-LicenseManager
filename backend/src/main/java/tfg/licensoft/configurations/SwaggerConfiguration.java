package tfg.licensoft.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import com.stripe.model.*;
import com.stripe.model.Account.Capabilities;
import com.stripe.model.Account.Company;
import com.stripe.model.Account.Company.Verification;
import com.stripe.model.Account.Company.Verification.VerificationDocument;
import com.stripe.model.Account.DeclineChargeOn;
import com.stripe.model.Account.PayoutSchedule;
import com.stripe.model.Account.Requirements;
import com.stripe.model.Account.Requirements.Errors;
import com.stripe.model.Account.Settings;
import com.stripe.model.Account.SettingsBranding;
import com.stripe.model.Account.SettingsCardPayments;
import com.stripe.model.Account.SettingsDashboard;
import com.stripe.model.Account.SettingsPayments;
import com.stripe.model.Account.SettingsPayouts;
import com.stripe.model.Account.TosAcceptance;
import com.stripe.model.BalanceTransaction.Fee;
import com.stripe.model.Charge.AlternateStatementDescriptors;
import com.stripe.model.Charge.FraudDetails;
import com.stripe.model.Charge.Level3;
import com.stripe.model.Charge.Level3.LineItem;
import com.stripe.model.Charge.Outcome;
import com.stripe.model.Charge.PaymentMethodDetails;
import com.stripe.model.Charge.PaymentMethodDetails.AchCreditTransfer;
import com.stripe.model.Charge.PaymentMethodDetails.AcssDebit;
import com.stripe.model.Charge.PaymentMethodDetails.Alipay;
import com.stripe.model.Charge.PaymentMethodDetails.Bancontact;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Checks;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Installments;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Wallet;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Wallet.GooglePay;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Wallet.Masterpass;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Wallet.SamsungPay;
import com.stripe.model.Charge.PaymentMethodDetails.Card.Wallet.VisaCheckout;
import com.stripe.model.Charge.PaymentMethodDetails.CardPresent;
import com.stripe.model.Charge.PaymentMethodDetails.CardPresent.Receipt;
import com.stripe.model.Charge.PaymentMethodDetails.Eps;
import com.stripe.model.Charge.PaymentMethodDetails.Fpx;
import com.stripe.model.Charge.PaymentMethodDetails.Giropay;
import com.stripe.model.Charge.PaymentMethodDetails.Ideal;
import com.stripe.model.Charge.PaymentMethodDetails.Klarna;
import com.stripe.model.Charge.PaymentMethodDetails.Multibanco;
import com.stripe.model.Charge.PaymentMethodDetails.P24;
import com.stripe.model.Charge.PaymentMethodDetails.SepaCreditTransfer;
import com.stripe.model.Charge.PaymentMethodDetails.SepaDebit;
import com.stripe.model.Charge.PaymentMethodDetails.Sofort;
import com.stripe.model.Charge.PaymentMethodDetails.StripeAccount;
import com.stripe.model.Charge.PaymentMethodDetails.Wechat;
import com.stripe.model.Charge.TransferData;
import com.stripe.model.CreditNote.TaxAmount;
import com.stripe.model.Customer.InvoiceSettings;
import com.stripe.model.Dispute.Evidence;
import com.stripe.model.Dispute.EvidenceDetails;
import com.stripe.model.Invoice.CustomField;
import com.stripe.model.Invoice.CustomerTaxId;
import com.stripe.model.Invoice.StatusTransitions;
import com.stripe.model.Invoice.ThresholdItemReason;
import com.stripe.model.Invoice.ThresholdReason;
import com.stripe.model.Mandate.CustomerAcceptance;
import com.stripe.model.Mandate.CustomerAcceptance.Offline;
import com.stripe.model.Mandate.CustomerAcceptance.Online;
import com.stripe.model.Mandate.MultiUse;
import com.stripe.model.Mandate.SingleUse;
import com.stripe.model.Order.ShippingMethod;
import com.stripe.model.Order.ShippingMethod.DeliveryEstimate;
import com.stripe.model.PaymentIntent.NextAction;
import com.stripe.model.PaymentIntent.NextActionRedirectToUrl;
import com.stripe.model.PaymentIntent.PaymentMethodOptions;
import com.stripe.model.PaymentMethod.AuBecsDebit;
import com.stripe.model.PaymentMethod.BillingDetails;
import com.stripe.model.PaymentMethod.Card.ThreeDSecureUsage;
import com.stripe.model.PaymentMethod.Card.Wallet.AmexExpressCheckout;
import com.stripe.model.PaymentMethod.Card.Wallet.ApplePay;
import com.stripe.model.Person.JapanAddress;
import com.stripe.model.Person.Relationship;
import com.stripe.model.Plan.Tier;
import com.stripe.model.Plan.TransformUsage;
import com.stripe.model.Review.Location;
import com.stripe.model.Sku.Inventory;
import com.stripe.model.Source.AchDebit;
import com.stripe.model.Subscription.BillingThresholds;
import com.stripe.model.Subscription.PauseCollection;
import com.stripe.model.SubscriptionSchedule.CurrentPhase;
import com.stripe.model.SubscriptionSchedule.DefaultSettings;
import com.stripe.model.SubscriptionSchedule.RenewalInterval;
import com.stripe.model.issuing.Cardholder.Individual.DateOfBirth;
import com.stripe.model.radar.Rule;
import com.stripe.net.RequestOptions;
import com.stripe.net.StripeResponse;
import com.sun.mail.iap.Response;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

import java.awt.print.Pageable;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;

import javax.annotation.Resource;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("tfg.licensoft.api"))
                .paths(regex("/api/.*"))
                .build()
                .ignoredParameterTypes(
                		Account.class,
                		AchCreditTransfer.class,
                		AchDebit.class,
                		AcssDebit.class,
                		Address.class,
                		Alipay.class,
                		AlternateStatementDescriptors.class,
                		AmexExpressCheckout.class,
                		ApplePay.class,
                		Application.class,
                		ApplicationFee.class,
                		AuBecsDebit.class,
                		BalanceTransaction.class,
                		BalanceTransactionSource.class,
                		Bancontact.class,
                		BillingDetails.class,
                		BillingThresholds.class,
                		Capabilities.class,
                		Card.class,
                		CardPresent.class,
                		Charge.class,
                		ChargeCollection.class,
                		Checks.class,
                		Company.class,
                		Coupon.class,
                		CurrentPhase.class,
                		CustomField.class,
                		Customer.class,
                		CustomerAcceptance.class,
                		CustomerTaxId.class,
                		DateOfBirth.class,
                		DeclineChargeOn.class,
                		DefaultSettings.class,
                		DeliveryEstimate.class,
                		Discount.class,
                		Dispute.class,
                		Eps.class,
                		Errors.class,
                		Evidence.class,
                		EvidenceDetails.class,
                		ExternalAccount.class,
                		ExternalAccountCollection.class,
                		Fee.class,
                		FeeRefund.class,
                		FeeRefundCollection.class,
                		File.class,
                		FileLink.class,
                		FileLinkCollection.class,
                		Fpx.class,
                		FraudDetails.class,
                		Giropay.class,
                		GooglePay.class,
                		Ideal.class,
                		InputStream.class,
                		Installments.class,
                		Inventory.class,
                		Invoice.class,
                		InvoiceLineItem.class,
                		InvoiceLineItemCollection.class,
                		InvoiceLineItemPeriod.class,
                		InvoiceSettings.class,
                		JapanAddress.class,
                		Klarna.class,
                		Level3.class,
                		LineItem.class,
                		Location.class,
                		Mandate.class,
                		Masterpass.class,
                		MultiUse.class,
                		Multibanco.class,
                		NextAction.class,
                		NextActionRedirectToUrl.class,
                		Number.class,
                		Offline.class,
                		Online.class,
                		Order.class,
                		OrderItem.class,
                		OrderReturn.class,
                		OrderReturnCollection.class,
                		Outcome.class,
                		P24.class,
                		PackageDimensions.class,
                		Pageable.class,
                		Page.class,
                		PauseCollection.class,
                		PaymentIntent.class,
                		PaymentMethod.class,
                		PaymentMethodDetails.class,
                		PaymentMethodOptions.class,
                		PaymentSource.class,
                		PaymentSourceCollection.class,
                		PayoutSchedule.class,
                		Person.class,
                		Plan.class,
                		Product.class,
                		Receipt.class,
                		Refund.class,
                		RefundCollection.class,
                		Relationship.class,
                		RenewalInterval.class,
                		RequestOptions.class,
                		Requirements.class,
                		Resource.class,
                		Response.class,
                		Review.class,
                		Rule.class,
                		SamsungPay.class,
                		SepaCreditTransfer.class,
                		SepaDebit.class,
                		Settings.class,
                		SettingsBranding.class,
                		SettingsCardPayments.class,
                		SettingsDashboard.class,
                		SettingsPayments.class,
                		SettingsPayouts.class,
                		SetupIntent.class,
                		ShippingDetails.class,
                		ShippingMethod.class,
                		SingleUse.class,
                		Sku.class,
                		Sofort.class,
                		StatusTransitions.class,
                		StripeAccount.class,
                		StripeError.class,
                		StripeResponse.class,
                		Subscription.class,
                		SubscriptionCollection.class,
                		SubscriptionItem.class,
                		SubscriptionItemCollection.class,
                		SubscriptionSchedule.class,
                		TaxAmount.class,
                		TaxId.class,
                		TaxIdCollection.class,
                		TaxRate.class,
                		ThreeDSecure.class,
                		ThreeDSecureUsage.class,
                		ThresholdItemReason.class,
                		ThresholdReason.class,
                		Tier.class,
                		Transfer.class,
                		TransferData.class,
                		TransferReversal.class,
                		TransferReversalCollection.class,
                		TransformUsage.class,
                		URI.class,
                		URL.class,
                		URLStreamHandler.class,
                		Verification.class,
                		VerificationDocument.class,
                		VisaCheckout.class,
                		Wallet.class,
                		Wechat.class,
                		File.class,
                		java.io.File.class,
                		Resource.class
                		);
    }
    
}