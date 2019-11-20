import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { ProductComponent } from './product/product.component';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';
import { UserProfileComponent } from './userProfile/userProfile.component';




const appRoutes = [
   { path: 'register', component: RegisterComponent, },
   { path: 'login', component: LoginComponent, },
   { path: 'admin/product/:name', component: ProductComponent, },
   { path: 'user/:userName/dashboard', component: UserDashboardComponent, },
   { path: 'user/profile', component: UserProfileComponent, },

];


export const routing = RouterModule.forRoot(appRoutes);
