import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

export class DOMHelper<T>{

    private fixture: ComponentFixture<T>;

    constructor(fixture:ComponentFixture<T>){
        this.fixture=fixture;
    }

    count(tagName: string):number{
        const elements = this.fixture.debugElement
        .queryAll(By.css(tagName));
        return elements.length;
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

    clickButton(buttonText:string){
        this.findAll('button').forEach(button =>{
            const buttonElement: HTMLButtonElement = button.nativeElement;
            if(buttonElement.textContent === buttonText){
                buttonElement.click();
            }
        });
    }

    findAll(tagName:string){
        return this.fixture.debugElement.queryAll(By.css(tagName));
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