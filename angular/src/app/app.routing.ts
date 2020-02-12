import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { ProductComponent } from './product/product.component';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';
import { UserProfileComponent } from './userProfile/userProfile.component';
import { AdminDashboardComponent } from './adminDashboard/adminDashboard.component';
import { CatalogComponent } from './catalog/catalog.component';
import { CatalogProductComponent } from './catalog/catalog-product/catalog-product.component';
import { CustomFormComponentComponent } from './custom-form-component/custom-form-component.component';




const appRoutes = [
   { path: 'register', component: RegisterComponent, },
   { path: 'login', component: LoginComponent, },
   { path: 'admin/product/:name', component: ProductComponent, },
   { path: 'admin/dashboard', component: AdminDashboardComponent, },
   { path: 'user/dashboard', component: UserDashboardComponent, },
   { path: 'user/profile', component: UserProfileComponent, },
  // { path: 'catalog', component: CatalogComponent, },
   { path: '', component: CatalogComponent, },
   { path: 'product/:name', component: CatalogProductComponent, },
   { path: 'try', component: CustomFormComponentComponent, },
   
];


export const routing = RouterModule.forRoot(appRoutes);
 