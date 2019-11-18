import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { ProductComponent } from './product/product.component';
import { UserDashboardComponent } from './userDashboard/userDashboard.component';




const appRoutes = [
   { path: 'register', component: RegisterComponent, },
   { path: 'login', component: LoginComponent, },
   { path: 'admin/product/:name', component: ProductComponent, },
   { path: 'user/:userName', component: UserDashboardComponent, }

];


export const routing = RouterModule.forRoot(appRoutes);
