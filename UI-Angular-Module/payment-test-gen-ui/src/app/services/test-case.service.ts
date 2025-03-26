import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TestCaseService {
  private apiUrl = 'http://localhost:8080/api/testcases/generate';

  constructor(private http: HttpClient) { }

  generateTestCases(paymentType: string, count: number, requirements: string = ''): Observable<string[]> {
    return this.http.get<string[]>(this.apiUrl, {
      params: {
        paymentType,
        count: count.toString(),
        requirements
      }
    });
  }
}
