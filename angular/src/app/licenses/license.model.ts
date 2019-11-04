import { Component } from '@angular/core';
import { ActiveDescendantKeyManager } from '@angular/cdk/a11y';
import { MatCalendar } from '@angular/material';
import { Product } from '../product/product.model';

  export interface License {
      serial:string;
      active:boolean;
      type:string;
      product:Product;
      startDate?:Date;
      endDate?:Date;
      owner:string;
  }