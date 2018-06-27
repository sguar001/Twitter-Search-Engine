package edu.ucr.cs.sguar001;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.json.JsonArray;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearcher {
	
    public static void main(String[] args) throws IOException, ParseException {

    	// use the standard analyzer to parse documents
    	Analyzer analyzer = new StandardAnalyzer();
    	
        // To store an index on disk, use this:
		final String INDEX_DIRECTORY = "Lucene/src/edu/ucr/cs/sguar001/Index";

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);


        // directory where all JSON files are stored to be indexed
        File dir = new File("Lucene/src/edu/ucr/cs/sguar001/JSON");


        // scan each file in directory and create a list of fields recquired for the document
        for (File file : dir.listFiles()) {
        	
        	  ReadJsonArray rjObject = new ReadJsonArray();
          
	          ArrayList<String> jsonUserNames = rjObject.getUserNames(file);
	
	          ArrayList<String> jsonNames = rjObject.getNames(file);
	
	          ArrayList<String> jsonProfilePics = rjObject.getProfilePics(file);
	          
	          ArrayList<String> jsonTweetText = rjObject.getTweetText(file);
	  
	          ArrayList<String> jsonTweetPlaces = rjObject.getPlaces(file);
	
	          ArrayList<Double[]> jsonTweetCoordinates = rjObject.getCoordinates(file);
	          
	          ArrayList<String> jsonTweetURLs = rjObject.getURLs(file);
	
	          ArrayList<String> jsonTweetSources = rjObject.getSources(file);
	  
	          ArrayList<String> jsonTweetTimeCreation = rjObject.getTimeCreated(file);
	          
	          ArrayList<Integer[]> jsonTweetStats = rjObject.getTweetStats(file);
	          
	          ArrayList<JsonArray> jsonTweetHashTags = rjObject.getHashTags(file);
	          
	          
	          // add every field to document and then add to index writer to be writtend to an index on disk
	          for(int i = 0; i < jsonUserNames.size(); i++) {
	          	Document doc = new Document();
	          	doc.add(new Field("username", jsonUserNames.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("name", jsonNames.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("profile_pic", jsonProfilePics.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("text", jsonTweetText.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("place", jsonTweetPlaces.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("latitude", jsonTweetCoordinates.get(i)[0].toString(), TextField.TYPE_STORED));
	          	doc.add(new Field("longitude", jsonTweetCoordinates.get(i)[1].toString(), TextField.TYPE_STORED));
	          	doc.add(new Field("source", jsonTweetSources.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("time", jsonTweetTimeCreation.get(i), TextField.TYPE_STORED));
	          	doc.add(new Field("retweets", jsonTweetStats.get(i)[0].toString(), TextField.TYPE_STORED));
	          	doc.add(new Field("favorites", jsonTweetStats.get(i)[1].toString(), TextField.TYPE_STORED));
	          	
	          	//add each hashtag included in tweet to document
	              for (int i1 = 0; i1 < jsonTweetHashTags.size(); i1++) {
	              	if(!jsonTweetHashTags.get(i1).isEmpty())
	              	{
	              		for(int j = 0; j < jsonTweetHashTags.get(i1).size(); j++) {
	                      	doc.add(new Field("hashtag", jsonTweetHashTags.get(i1).get(j).asJsonObject().getString("text"), TextField.TYPE_STORED));
	              		} 
	              	}
	              }
	              
	          	doc.add(new Field("url", jsonTweetURLs.get(i), TextField.TYPE_STORED));
	          	indexWriter.addDocument(doc);
	          }
        	
        }
        
        indexWriter.close();

	}
}