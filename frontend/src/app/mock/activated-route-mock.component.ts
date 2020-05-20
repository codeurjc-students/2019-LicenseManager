import { convertToParamMap } from '@angular/router';
import { Observable } from 'rxjs/Observable';

export class ActivatedRouteMock {
    public paramMap = Observable.of(convertToParamMap({ 
        name: 'Prod1',
    }));
}