package edu.ucr.cs.sguar001.lucenesearcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TweetController {
	
    static List<Tweet> tweets;
	
	public static ScoreDoc[] createTweets(String q) throws ParseException, IOException {

		
        //Use the standard analyzer provided by lucene
		Analyzer analyzer = new StandardAnalyzer();

        // To store an index on disk, use this instead:
		final String INDEX_DIRECTORY = "../../Lucene/src/edu/ucr/cs/sguar001/Index";
		
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
		
        // Create an instance of an indexreader to be used to read directory of JSON file
		DirectoryReader indexReader = DirectoryReader.open(directory);
        // Create indexSearcher to search newly created indexReader
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        
        // For these fields, give an extra boost in score
        String[] fields = {"username", "text", "place", "time", "hashtag", "url"};
        Map<String, Float> boosts = new HashMap<>();
        boosts.put(fields[0], 0.4f);
        boosts.put(fields[1], 0.5f);
        boosts.put(fields[2], 0.4f);
        boosts.put(fields[3], 0.5f);
        boosts.put(fields[4], 0.3f);
        boosts.put(fields[5], 0.3f);
        
        // Parse all the fields stored in fields[], using standard analyzer and providing given boosts
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);

        // Query the multifieldquery parser with the passed in query
        Query query = parser.parse(q);
        
        // Return only the top 100 hits 
        int topHitCount = 100;

        //Sort the hits by field score
        Sort sortType = new Sort(SortField.FIELD_SCORE);

        // Return the found documents that match the given query
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount, sortType).scoreDocs;
        
        // New tweets array list that will be used for returning matches
        tweets = new ArrayList<>();
        
        // Iterate through the results:
        for (int rank = 0; rank < hits.length; ++rank) {

            // Get document from hits array
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            
            // Get url from doc
            String urlTitle = hitDoc.get("url");

            // get tweet text from doc
            String body = hitDoc.get("text");

            // Get user username from doc
            String username = hitDoc.get("username");

            // Get users screen name from doc
            String name = hitDoc.get("name");

            // Get users profile pic url from doc
            String profilePic = hitDoc.get("profile_pic");

            // Get tweet's place of origin from the doc
            String place = hitDoc.get("place");

            // Get nearest coordinates of tweet from doc for maps api
            String[] coordinates = new String[] {hitDoc.get("latitude"), hitDoc.get("longitude")};

            // Get creation time of tweet from doc
            String timeCreated = hitDoc.get("time");

            // Get retweet/favorites count of tweet from doc
            String[] tweetStats = new String[] {hitDoc.get("retweets"), hitDoc.get("favorites")};

            // Get device source of tweet from doc
            String source = hitDoc.get("source");

            // Get hashtag of tweet from doc
            String hashtag = hitDoc.get("hashtag");
            
            // Add all fields to new tweet cocnstructor and add to tweet array
            tweets.add(new Tweet(urlTitle, body, username, name, profilePic, place, coordinates, 
            		timeCreated, tweetStats, hashtags, source));
        }

        // close the indexReader after done indexing
        indexReader.close();
        // close the directory
        directory.close();
        
        // return the array of hitDocs
		return hits;
	}
	
    // function to search tweets given a string query
    @GetMapping("/tweets")
    public List<Tweet> searchTweets(
        @RequestParam(required=false, defaultValue="") String query) {

        // if query is empty, clear tweets and return first 100 tweets that match wild card '*:*'
        if (query.isEmpty())
        {
        	tweets.clear();
        	
            try {
    			ScoreDoc[] hits = createTweets("*:*");
    		} catch (ParseException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        	
            // add tweets to matches array to be displayed in results
            List<Tweet> matches = new ArrayList<>();
            for (Tweet tweet: tweets) {
                    matches.add(tweet);
            }
            return matches;
        }
        	

        // if query is not empty, return first 100 tweets that match given query
        try {
			ScoreDoc[] hits = createTweets(query);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Return matching tweets to display in results
        List<Tweet> matches = new ArrayList<>();
        for (Tweet tweet: tweets) {
                matches.add(tweet);
        }
        return matches;
    }
    
}