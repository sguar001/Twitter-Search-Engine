import { Component, OnInit } from '@angular/core';

import { TweetService } from './tweet.service';
import { Tweet } from './tweet';
import { Observable, Subject } from 'rxjs';

import { ViewChild } from '@angular/core';
import { } from '@types/googlemaps';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  // Required for google maps implementation
  @ViewChild('gmap') gmapElement: any;
  map: google.maps.Map;
  location: google.maps.LatLng;

  // hold returned tweets
  tweets: Tweet[];
  tempTweets: Tweet[];
  lastSearch: string;

  constructor(private tweetService: TweetService) { }

  // on intialize, create a default map with these default parameters.
  ngOnInit() {    
      var mapProp = {
      center: new google.maps.LatLng(18.5793, 73.8143),
      zoom: 10,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    this.map = new google.maps.Map(this.gmapElement.nativeElement, mapProp);
  }

  // function to plot a given array of tweets on the map
  plotMarker(tweets:Tweet[]) { 

      // needed to fix scope issue within callbacks
      var self = this;

      // ask for permission to receive geolocation
      if(navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(function(position) {
          //curernt long/lat
          var currentLat = position.coords.latitude;
          var currentLong = position.coords.longitude;
          self.location = new google.maps.LatLng(currentLat, currentLong);

          // center map on user's location
          var mapProp = {
            center: self.location,
            zoom: 10,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };

          // create new map instance
          self.map = new google.maps.Map(self.gmapElement.nativeElement, mapProp);

          // display the map
          document.getElementById("gmap").style.display="block";

          // plot each tweet with marker on map
          for(var i = 0; i < tweets.length; i++) {
            var obj = JSON.parse(JSON.stringify(tweets[i]));
            var coordinates = obj.coordinates;
            var latLng = new google.maps.LatLng(coordinates[1], coordinates[0]);
            var marker = new google.maps.Marker({
              position: latLng,
              map: self.map
            });
          }
        });
      }

    }   

  // function to return a result of tweets that match given query
  search(query: string) {

    // hide map
    document.getElementById("gmap").style.display="none";

    this.lastSearch = query;

    this.tweetService.getTweets(query)
        .subscribe(tweets => this.tweets = tweets);
  }

  // function to display tweets nearest to current location of user
  displayNearestTweets() {

      var self = this;

      if(navigator.geolocation) {

        var position =navigator.geolocation.getCurrentPosition(function(position){
          var currentLat = position.coords.latitude;
          var currentLong = position.coords.longitude;
          self.location = new google.maps.LatLng(currentLat, currentLong);

          var mapProp = {
            center: self.location,
            zoom: 10,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };

          self.map = new google.maps.Map(self.gmapElement.nativeElement, mapProp);

          document.getElementById("gmap").style.display="block";

          self.tweetService.getTweets("*:*")
            .subscribe(tempTweets => {

              self.tempTweets = tempTweets;

              for(var i = 0; i < self.tempTweets.length; i++) {
                var obj = JSON.parse(JSON.stringify(self.tempTweets[i]));
                var coordinates = obj.coordinates;
                var latLng = new google.maps.LatLng(coordinates[1], coordinates[0]);

                // compute distance between user's location and location of tweet 
                var distance = google.maps.geometry.spherical.computeDistanceBetween(self.location, latLng);
                var miles = distance * 0.00062137;

                // if distance (in miles) is less than 100, plot tweet on map with a marker
                if(miles <= 100) {
                  var marker = new google.maps.Marker({
                    position: latLng,
                    map: self.map
                  });
                }
              }

            });

        });

      }
  }

}