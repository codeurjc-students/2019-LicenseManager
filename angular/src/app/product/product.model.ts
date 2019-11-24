import { License } from '../licenses/license.model';

export interface Product{
    name:string;
    licenses:License[];
    typeSubs:string[];

    constructor();
}