import { Injectable } from '@angular/core';
import { Tweet } from './tweet';
// import { Headers, Http, Response } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class TweetService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getTweets(query: string="UCR"):  Observable<Tweet[]> {
    return this.http.get<Tweet[]>(this.baseUrl + '/api/tweets?query=' + query);
  }
}