import { NgModule, TemplateRef } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JsonpModule, HttpModule } from '@angular/http';
import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatIconRegistry } from '@angular/material/icon';
import { CovalentFileModule } from '@covalent/core/file';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';


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
import { IndexComponent } from './index/index.component';
import { ProductComponent } from './product/product.component';
import { ProductService } from './product/product.service';
import { LicenseService } from './licenses/license.service';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';
import { LicenseComponent } from './licenses/license.component';
import { CardLicenseComponent } from './cards/cardLicense.component';
import { DialogDeleteComponent } from './dialogs/dialogDelete.component';
import { DialogEditComponent } from './dialogs/dialogEdit.component';
import { UserProfileComponent } from './userProfile/userProfile.component';
import { UserProfileService } from './userProfile/userProfile.service';
import { BasicAuthInterceptor } from 'src/auth.interceptor';
import { ErrorInterceptor } from 'src/error.interceptor';
import { AdminDashboardComponent } from './adminDashboard/adminDashboard.component';
import { DialogAddProductComponent } from './dialogs/dialogAddProduct.component';
import { CardFormComponent } from './userProfile/card-form/card-form.component';
import { CatalogComponent } from './catalog/catalog.component';
import { CatalogProductComponent } from './catalog/catalog-product/catalog-product.component';
import { CustomFormComponentComponent } from './custom-form-component/custom-form-component.component';
import { ConfirmationDialogComponent } from './dialogs/confirmation-dialog/confirmation-dialog.component';

@NgModule({
    imports: [
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
    declarations: [AppComponent,DialogAddProductComponent,UserProfileComponent,LoginComponent, RegisterComponent, ProductComponent, UserDashboardComponent,LicenseComponent,CardLicenseComponent, DialogDeleteComponent, DialogEditComponent, AdminDashboardComponent, CardFormComponent,CatalogComponent, CatalogProductComponent, CustomFormComponentComponent, ConfirmationDialogComponent],
    bootstrap: [AppComponent],
    entryComponents: [DialogDeleteComponent, DialogEditComponent,DialogAddProductComponent, ConfirmationDialogComponent
    ],
    providers:[ LoginService, RegisterService, ProductService, LicenseService, UserProfileService,DatePipe,
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        { provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        { provide: MAT_CHECKBOX_CLICK_ACTION, useValue: 'check'}

        ,]
})
export class AppModule {
    constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer) {
        matIconRegistry.addSvgIconSet(domSanitizer.bypassSecurityTrustResourceUrl('/assets/symbol-defs.svg'));
    }
}
