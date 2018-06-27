package edu.ucr.cs.sguar001.lucenesearcher;

public class Tweet {

    // Data fields that each tweet object will hold

    public String urlTitle;
    public String body;
    public String username;
    public String name;
    public String profilePic;
    public String place;
    public String[] coordinates;
    public String timeCreated;
    public String[] tweetStats;
    public String[] hashTags;
    public String source;
    

    public Tweet(){}

    // Tweet object constructor
    public Tweet(String title, String body, String username, String name, String profilePic, 
    		String place, String[] coordinates, String timeCreated, String[] tweetStats, 
    		String[] hashtags, String source) {
        this.urlTitle = title;
        this.body = body;
        this.username = username;
        this.name = name;
        this.profilePic = profilePic;
        this.place = place;
        this.coordinates = coordinates;
        this.timeCreated = timeCreated;
        this.tweetStats = tweetStats;
        this.hashTags = hashtags;
        this.source = source;
    }

    // Get urltitle of tweet objecct
    public String getTitle() {
        return urlTitle;
    }

    // set title of tweet object
    public void setTitle(String title) {
        this.urlTitle = title;
    }

    // Return text body of tweet object
    public String getBody() {
        return body;
    }

    // Set text body of tweet object
    public void setBody(String body) {
        this.body = body;
    }
    
    // Return username associated with tweet object
    public String getUsername() {
    	return username;
    }
    
    // Set username associated with tweet object
    public void setUsername(String username) {
    	this.username = username;
    }

    @Override
    public String toString() {
        return String.format("Tweet[title=%s, body=%s, username=%s]", urlTitle, body, username);
    }
}
