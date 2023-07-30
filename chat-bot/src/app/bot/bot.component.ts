

import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core';




interface ChatMessage {
  content: string;
  isUser: boolean;
}

@Component({
  selector: 'app-bot',
  templateUrl: './bot.component.html',
  styleUrls: ['./bot.component.css']
})

export class BotComponent implements OnInit {
  greeting: string = '';
  name: string = '';
  name1: string = '';
  responses: any[] = [];
  selectedIntentId: number = 0;
  selectedResponseId: number = 0;
  selectedId1: number = 0;
  selectedId2: number = 0;
  rules: string = '';
  rules2: string = '';
  isHoveredResponse: number | null = null;
  isHoveredIntent: number | null = null;
  showActionContainer: boolean = false;

  intents: any[] = [];
  messages: ChatMessage[] = [];
  userInput: string = '';
  userInput1: string = '';
  chatForm: any;
  actions: any[] = [];
  showIntentContainer: boolean;

  constructor(
    private http: HttpClient,
    private formBuilder: FormBuilder,
    private sanitizer: DomSanitizer
  ) {
    this.showIntentContainer = false;
  }
  
  
  
  
  
  
  
  ngOnInit() {
    this.getGreeting();
  }
  
  convertNewLines(text: string): string {
    return text.replace(/\n/g, '<br>');
  }
  
  getGreeting() {
    const requestBody = {};
    this.http.post<any>('http://localhost:8080/greet', requestBody).subscribe(
      response => {
        this.greeting = response.greeting;
        this.responses = response.responses;
      },
      error => {
        console.log(error);
      }
    );
  }

  getResponseIntents(responseId: number) {
    this.selectedId1 = responseId;
    const requestBody = { responseId: this.selectedId1 };

    this.http.post<any>('http://localhost:8080/greet', requestBody).subscribe(
      intents => {
        this.intents = intents;
        this.processIntents();
      },
      error => {
        console.log(error);
      }
    );
  }

  getResponseActions(responseId: number, intentId: number) {
    this.selectedId2 = intentId;
    const requestBody = { responseId: this.selectedId1, id: this.selectedId2 };
    this.http.post<any>('http://localhost:8080/greet', requestBody).subscribe(
      actions => {
        this.actions = actions;
      },
      error => {
        console.log(error);
      }
    );
  }

  getResponseRules(responseId: number, intentId: number, rule: string) {
    const requestBody = { responseId: this.selectedId1, id: this.selectedId2, intentId: this.rules };
    this.http.post<any>('http://localhost:8080/greet', requestBody).subscribe(
      finalResponse => {
        this.name = finalResponse;
      },
      error => {
        console.log(error);
      }
    );
  }

  getResponseRules2(responseId: number, intentId: number, rule: string, rule2: string) {
    const requestBody = { responseId: this.selectedId1, id: this.selectedId2, intentId: this.rules, intentId2: this.rules2 };
    this.http.post<any>('http://localhost:8080/greet', requestBody).subscribe(
      finalResponse2 => {
        this.name1 = finalResponse2.replace(/[{}[\]]/g, ''); // Remove brackets from name1 value using regular expression
      },
      error => {
        console.log(error);
      }
    );
  }
  
  
  sanitizeLink(linkText: string): string {
    const pattern = /(https?:\/\/\S+)/g;
    const sanitizedText = linkText.replace(pattern, '<a href="$&" target="_blank">$&</a>');
    return sanitizedText;
  }
  
  processIntents() {
    for (const intent of this.intents) {
      const content = intent.name;
      const pattern = /(https?:\/\/\S+)/g;
      const links = content.match(pattern);
      if (links) {
        for (const link of links) {
          const safeLink: SafeUrl = this.sanitizer.bypassSecurityTrustUrl(link);
          intent.link = safeLink;
        }
      }
    }
  }
  
  
  
  checkFirstUserInput() {
    this.showIntentContainer = this.userInput.trim() !== '';
  }

  sendMessage() {
    if (this.rules === '') {
      this.rules = this.userInput;
    } else if (this.rules2 === '') {
      this.rules2 = this.userInput;
    }
    this.showActionContainer = true;
    this.messages.push({ content: this.userInput, isUser: true });
    this.userInput = '';
  }
}

















