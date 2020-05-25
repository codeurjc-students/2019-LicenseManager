import { Product } from '../product/product.model';
import { LicenseStatistics } from '../licenseStatistics/licenseStatistics.model';

  export interface License {
      id?:number;
      serial?:string;
      active?:boolean;
      type?:string;
      product?:Product;
      startDate?:Date;
      endDate?:Date;
      owner?:string;
      cancelAtEnd?:boolean;
      subscriptionId?:string;
      nUsage?:number;
      price?:number;  
      trial?:boolean;
      licenseStats?:LicenseStatistics[];
      licenseString?:string;
      period?:number;
  }