import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { ProductComponent } from './product/product.component';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';
import { UserProfileComponent } from './userProfile/userProfile.component';
import { AdminDashboardComponent } from './adminDashboard/adminDashboard.component';
import { CatalogComponent } from './catalog/catalog.component';
import { CatalogProductComponent } from './catalog/catalog-product/catalog-product.component';
import { CustomFormComponentComponent } from './custom-form-component/custom-form-component.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LicenseStatisticsComponent } from './licenseStatistics/licenseStatistics.component';
import { Component } from '@angular/core';
import { ApiDocsComponent } from './api-docs/api-docs.component';
import { ProductStatisticsComponent } from './productStatistics/product-statistics.component';




const appRoutes = [
   { path: '', redirectTo: 'products', pathMatch: 'full' },
   { path: 'register', component: RegisterComponent, },
   { path: 'login', component: LoginComponent, },
   { path: 'admin/products/:name/statistics', component: ProductStatisticsComponent, },
   { path: 'admin/dashboard', component: AdminDashboardComponent, },
   { path: 'user/dashboard', component: UserDashboardComponent, },
   { path: 'user/profile', component: UserProfileComponent, },
   { path: 'products', component: CatalogComponent, },
   { path: 'products/:name', component: CatalogProductComponent, },
   { path: 'user/products/:name/statistics/:serial', component: LicenseStatisticsComponent, },
   { path: 'development/api', component: ApiDocsComponent, },
   { path: '404', component: NotFoundComponent},
   { path: '**', redirectTo: '/404'}
];


export const routing = RouterModule.forRoot(appRoutes);
 