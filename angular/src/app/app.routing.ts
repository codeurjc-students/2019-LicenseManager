import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { ProductComponent } from './product/product.component';




const appRoutes = [
   { path: 'register', component: RegisterComponent, },
   { path: 'login', component: LoginComponent, },
   { path: 'product/:name', component: ProductComponent, },


];


export const routing = RouterModule.forRoot(appRoutes);
