import { NgModule, TemplateRef } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JsonpModule, HttpModule } from '@angular/http';
import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatIconRegistry } from '@angular/material/icon';
import { CovalentFileModule } from '@covalent/core/file';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import {MatPaginatorModule} from '@angular/material/paginator';

import { NgxStripeModule } from 'ngx-stripe';
import { NgxPaginationModule} from 'ngx-pagination';
import { AppComponent } from './app.component';

import {
    MatChipsModule,
    MatButtonModule,
    MatListModule,
    MatIconModule,
    MatCardModule,
    MatMenuModule,
    MatInputModule,
    MatButtonToggleModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatDialogModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatTabsModule,
    MatSidenavModule,
    MatTooltipModule,
    MatRippleModule,
    MatRadioModule,
    MatGridListModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSliderModule,
    MatAutocompleteModule,
    MatSnackBar,
    MAT_DIALOG_DEFAULT_OPTIONS,
    MAT_CHECKBOX_CLICK_ACTION,
    MatCheckboxModule,
} from '@angular/material';

import {
    CovalentCommonModule,
    CovalentLayoutModule,
    CovalentMediaModule,
    CovalentExpansionPanelModule,
    CovalentStepsModule,
    CovalentLoadingModule,
    CovalentDialogsModule,
    CovalentSearchModule,
    CovalentPagingModule,
    CovalentNotificationsModule,
    CovalentMenuModule,
    CovalentVirtualScrollModule,
    CovalentDataTableModule,
    CovalentMessageModule,
    CovalentTabSelectModule,
} from '@covalent/core';
import { routing } from './app.routing';


import { NgxChartsModule } from '@swimlane/ngx-charts';
import { DomSanitizer } from '@angular/platform-browser';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { LocationStrategy, HashLocationStrategy, DatePipe } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { LoginService } from './login/login.service';
import { RegisterComponent } from './register/register.component';
import { RegisterService } from './register/register.service';
import { ProductService } from './product/product.service';
import { LicenseService } from './licenses/license.service';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';
import { UserProfileComponent } from './userProfile/userProfile.component';
import { UserProfileService } from './userProfile/userProfile.service';
import { BasicAuthInterceptor } from './login/auth.interceptor';
import { ErrorInterceptor } from './login/error.interceptor';
import { AdminDashboardComponent } from './adminDashboard/adminDashboard.component';
import { DialogAddProductComponent } from './dialogs/dialogAddProduct.component';
import { CardFormComponent } from './userProfile/card-form/card-form.component';
import { CatalogComponent } from './catalog/catalog.component';
import { CatalogProductComponent } from './catalog/catalog-product/catalog-product.component';
import { CustomFormComponentComponent } from './custom-form-component/custom-form-component.component';
import { ConfirmationDialogComponent } from './dialogs/confirmation-dialog/confirmation-dialog.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AppService } from './app.service';
import { DialogFreeTrial } from './dialogs/dialogFreeTrial.component';
import { UsedCardService } from './usedCard/usedCard.service';
import { LicenseStatistics } from './licenseStatistics/licenseStatistics.model';
import { LicenseStatisticsComponent } from './licenseStatistics/licenseStatistics.component';
import { PieChartComponent } from './charts/pie-chart.component';
import { BarChartComponent } from './charts/bar-chart.component';
import { AddCardDialogComponent } from './dialogs/add-card-dialog/add-card-dialog.component';
import { CardSubscriptionSelectDialog } from './dialogs/card-subscription-select-dialog/card-subscription-select-dialog.component';
import { CardSelectDialog } from './dialogs/card-select-dialog copy/card-select-dialog.component';
import { ApiDocsComponent } from './api-docs/api-docs.component';
import { ProductStatisticsComponent } from './productStatistics/product-statistics.component';

@NgModule({
    imports: [ 
        NgxStripeModule.forRoot(),
        NgxPaginationModule,
        MatCheckboxModule,
        HttpModule,
        ReactiveFormsModule,
        CovalentVirtualScrollModule,
        MatChipsModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        RouterModule.forRoot([]),
        HttpClientModule,
        JsonpModule,
        CovalentFileModule,
        MatSnackBarModule,
        CovalentTabSelectModule,
        /** Material Modules */
        MatPaginatorModule,
        MatButtonModule,
        MatListModule,
        MatIconModule,
        MatCardModule,
        MatMenuModule,
        MatInputModule,
        MatSelectModule,
        MatButtonToggleModule,
        MatSlideToggleModule,
        MatProgressSpinnerModule,
        MatDialogModule,
        MatSnackBarModule,
        MatToolbarModule,
        MatTabsModule,
        MatSidenavModule,
        MatTooltipModule,
        MatRippleModule,
        MatRadioModule,
        MatGridListModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatSliderModule,
        MatInputModule,
        MatAutocompleteModule,
        /** Covalent Modules */
        CovalentCommonModule,
        CovalentLayoutModule,
        CovalentMediaModule,
        CovalentExpansionPanelModule,
        CovalentStepsModule,
        CovalentDialogsModule,
        CovalentLoadingModule,
        CovalentSearchModule,
        CovalentPagingModule,
        CovalentNotificationsModule,
        CovalentMenuModule,
        CovalentDataTableModule,
        CovalentMessageModule,
        /** Additional **/
        NgxChartsModule,
        routing,
    ],
    declarations: [AppComponent,ProductStatisticsComponent,ApiDocsComponent,CardSubscriptionSelectDialog,CardSelectDialog,AddCardDialogComponent,BarChartComponent, PieChartComponent, LicenseStatisticsComponent,DialogAddProductComponent,UserProfileComponent,LoginComponent, RegisterComponent, UserDashboardComponent,NotFoundComponent, AdminDashboardComponent, CardFormComponent,CatalogComponent, CatalogProductComponent, CustomFormComponentComponent, ConfirmationDialogComponent, DialogFreeTrial],
    bootstrap: [AppComponent],
    entryComponents: [DialogAddProductComponent, ConfirmationDialogComponent,LoginComponent, DialogFreeTrial, AddCardDialogComponent,CardSelectDialog,CardSubscriptionSelectDialog,
    ],
    providers:[ LoginService, RegisterService, ProductService, LicenseService, UserProfileService,DatePipe,LoginComponent, AppService, UserProfileComponent, CardFormComponent, UsedCardService,
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        { provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        { provide: MAT_CHECKBOX_CLICK_ACTION, useValue: 'check'}

        ,]
})

export class AppModule {
    constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer, private appService:AppService ) {
        matIconRegistry.addSvgIconSet(domSanitizer.bypassSecurityTrustResourceUrl('/assets/symbol-defs.svg'));
    }
}
