import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient,  HttpHeaders,  HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { options } from '../utils/httpUtils';

// httpOptions for set header options
const httpOptions = {
  headers: new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }),
};

const path = '';
const pathConsulta = '';

@Injectable({
  providedIn: 'root'
})

export class MemoryService {

  private endpointapi:string | undefined;
  constructor(private http: HttpClient) {
    this.endpointapi=" http://localhost:8081/drive";
  }


  get(endpoint: string): Observable<any>
  {
    console.log(endpoint);
    return this.http.get(path + endpoint,options).pipe(
      catchError(this.handleError)
    );
  }

  getMemory(): Observable<any>
  {
    return this.get(this.endpointapi+"/memory/info");
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${JSON.stringify(error.error)}`);
    }
    // return an observable with a user-facing error message
    return throwError(error.error);
  }

}
