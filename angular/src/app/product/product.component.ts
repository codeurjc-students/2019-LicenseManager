import { Component, OnInit } from '@angular/core';
import { License } from '../licenses/license.model';
import { ProductService } from './product.service';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { LoginService } from '../login/login.service';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
  })
  export class ProductComponent implements OnInit{

    name:string;
    licenses:License[];

    constructor(private router: Router, private activeRoute: ActivatedRoute,private productService:ProductService,  public loginService: LoginService){    }

    ngOnInit(): void {

      this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        this.name = this.activeRoute.snapshot.params.name;
        console.log(this.name);
      //  this.name = params.get('name');
        this.getLicenses();

      });


    }

    getLicenses(){
      this.productService.getLicensesByProduct(this.name).subscribe(
        lics => {this.licenses = lics.content, console.log(lics.content);},
        error => console.log(error)
      ); 
 
    }

    deleteLicense(){}

    startLicense(){}

    pauseLicense(){}


  }