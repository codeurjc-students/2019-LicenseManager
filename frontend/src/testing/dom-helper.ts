import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

export class DOMHelper<T>{

    private fixture: ComponentFixture<T>;

    constructor(fixture:ComponentFixture<T>){
        this.fixture=fixture;
    }


    checkExists(tagName: string):boolean{
        return this.fixture.debugElement.query(By.css(tagName))!=null;
    }

    getObject(tagName: string){
        return this.fixture.debugElement.query(By.css(tagName));
    }

    getText(tagName: string){
        return this.fixture.debugElement.query(By.css(tagName)).nativeElement.textContent;
    }

    clickButtonById(id:string){
        const buttonElement: HTMLButtonElement =this.fixture.debugElement.query(By.css(id)).nativeElement;
        
        buttonElement.click();
    }

    findAllButtonsWithName(buttonText:string){
        let buttons:HTMLButtonElement[] = [];
        this.fixture.debugElement.queryAll(By.css("button")).forEach(button=>{
            const buttonElement: HTMLButtonElement = button.nativeElement;
            if(buttonElement.textContent === buttonText){
                buttons.push(buttonElement)
            }
        });
        return buttons;

    }

}