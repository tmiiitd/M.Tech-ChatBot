//mdm

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


 class Corpus {

	ArrayList<Document> collection;
	HashMap<String, Integer> tokens;
	double avg_doc_length;

	public Corpus() throws IOException {

		
		collection 		= new ArrayList<Document>();
		tokens 			= new HashMap<String, Integer>();
		avg_doc_length 	= 0.0;
        
	}
	
	
	
	// total documents
	
	int  getTotalDocCount() {
		return collection.size();
	}
	
	
	
// add document in corpus collection

	void addDocument(Document D) {

		HashMap<String, Integer> tf = D.getTF();
	
		for(String key: tf.keySet())
		{
			if(tokens.containsKey(key))
				tokens.put(key, tokens.get(key) +1);
			else
				tokens.put(key, 1);
		}
		
		collection.add(D);
		avg_doc_length = (avg_doc_length + D.getTF().size()) /2;
		//this needs to be changed
		
	}
	

	
	//IDF needs to be changed
	double getIDF(String key,int one) {
		//System.out.println("idf of "+key+" is "+Math.log10(collection.size()* 1.0 / tokens.get(key)));
		
		return Math.log10(collection.size()* 1.0 / tokens.get(key));
	}
	double getIDF(String key) {
		//System.out.println("idf of "+key+" is "+Math.log10(collection.size()* 1.0 / tokens.get(key)));
		return Math.log10(collection.size()* 1.0 / tokens.get(key));
	}
 }
	
	
 
 
