import { User } from '../login/login.service';

export interface StripeCard {
    owner:string;
    cvv:string;
    exp_year:string;
    exp_month:string;
    number:string;
    user:User;

}