import { License } from '../licenses/license.model';

export interface Product{
    name:string;
    licenses:License[];
    typeSubs:string[];
    photoAvailable:boolean;
    description:string;
    webLink:string;
    photoSrc:string;
    //plansPrices:Map<String,Number>;
    plansPrices:{ [name: string]: number };
    sku:string;
    active:boolean;
    trialDays:number;
    mode:string;

}