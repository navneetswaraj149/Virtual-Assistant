import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChatBotService {
  private apiUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  

  getIntents(responseId: number) {
    return this.http.post(`${this.apiUrl}/greet`, { responseId });
  }

  getActions(responseId: number, intentId: number) {
    return this.http.post(`${this.apiUrl}/greet`, { responseId, id: intentId });
  }
  getRules(responseId: number, intentId: number,rule: string) {
    return this.http.post(`${this.apiUrl}/greet`, { responseId, id: intentId ,intentId: rule});
  }
  getRules2(responseId: number, intentId: number,rule: string,rule2: string) {
    return this.http.post(`${this.apiUrl}/greet`, { responseId, id: intentId ,intentId: rule,intentId2: rule2});
  }
}



