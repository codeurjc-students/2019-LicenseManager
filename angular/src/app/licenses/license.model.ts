import { Component } from '@angular/core';
import { ActiveDescendantKeyManager } from '@angular/cdk/a11y';
import { MatCalendar } from '@angular/material';

  export interface License {
      serial:string;
      active:boolean;
      type:string;
      product:string;
      startDate?:Date;
      endDate?:Date;
  }