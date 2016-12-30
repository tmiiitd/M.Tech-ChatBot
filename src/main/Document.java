//mdm
//mdm
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.tartarus.snowball.ext.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Document {

	HashMap<String, Integer> tf = new HashMap<>();

	double magnitude;
	String string;
	String nonStopString;
	double bm_25 = -10; //initialized as -10, will remain -10 until calculated
	int informationCount;
	int overallMatch_Words; //number of non-stop stemmed question words found in the answer
	
	// constructor for string keyphrase
    public Document(String keyphrase,int one){

    	this.string = keyphrase;
    	this.string = string.toLowerCase();
		String [] words = keyphrase.split("[,\\n,\\?,\\t, ,]");
		porterStemmer stemmer = new porterStemmer();


		for(int k=0;k<words.length;k++) {

			stemmer.setCurrent(words[k]);
			stemmer.stem();
			words[k] = stemmer.getCurrent();

			if(!tf.containsKey(words[k]))
				tf.put(words[k], new Integer(1));
			else
				tf.put(words[k], new Integer(tf.get(words[k]).intValue() +1));
		}

		for(Integer value: tf.values())
		{
			magnitude += value.intValue()*value.intValue();
		}

		magnitude = (double) Math.sqrt(magnitude);

		//System.out.println(this.string);
	}

    
    
    //constructor for path
	public Document(String path) throws IOException{

	   
		String sCurrentLine;

		BufferedReader br = new BufferedReader(new FileReader(path));



		this.string="";
		while ((sCurrentLine = br.readLine()) != null) {
            
			this.string = this.string+sCurrentLine+" ";
			String [] words = sCurrentLine.split("[,\\n,\\?,\\t, ,.,]");
			porterStemmer stemmer = new porterStemmer();


			for(int k=0;k<words.length;k++) {

				stemmer.setCurrent(words[k]);
				stemmer.stem();
				words[k] = stemmer.getCurrent();

				if(!tf.containsKey(words[k]))
					tf.put(words[k], new Integer(1));
				else
					tf.put(words[k], new Integer(tf.get(words[k]).intValue() +1));
			}

			for(Integer value: tf.values())
			{
				magnitude += value.intValue()*value.intValue();
			}

			magnitude = (double) Math.sqrt(magnitude);
		}
	    this.string = string.toLowerCase();
		//System.out.println(this.string);
	}


	
	
	public HashMap<String, Integer> getTF() {
		return tf;
	}

	public double getMagnitude(){
		return magnitude;

	}

	public void settf(HashMap<String, Integer> tf) {
		this.tf = tf;
	}

	
	
// Cosine similarity score

	public double Cosine(Document v){

		double score = 0.0;
		HashMap<String, Integer> u = v.getTF();

		for (Entry<String, Integer> entry : tf.entrySet()) {

		    String key = entry.getKey();

		    if(u.containsKey(key)){

		    	int tmp1 = entry.getValue().intValue();
		    	int tmp2 = u.get(key).intValue();
		    	score 	+= tmp1 * tmp2;
		    }
		}
		System.out.println(score/(v.getMagnitude() * getMagnitude()));
		return score/(v.getMagnitude() * getMagnitude());
	}
	
	
	
	

	// BM25 score

	double getBM25Score(Document Q, Corpus C) {

		HashMap<String, Integer> tokens = Q.getTF();

		double bm_25 	= 0.0;
		double b 		= 0.75;
		double k1		= 1.2;
		double k3 		= 1000;
		double k 		= k1* ((1-b) + b*tokens.size()*1.0/C.avg_doc_length);

		for(String token: tokens.keySet()) {
			//System.out.println("the token is "+token);
			if(tf.containsKey(token))  {

				double tmp 	= (k1+1)*tf.get(token) * (k3+1)* Q.tf.get(token);
					tmp 	= tmp*1.0 / k+tf.get(token)* k3 + Q.tf.get(token);
					bm_25 	+= tmp * C.getIDF(token);

			}
		}

		//System.out.println("you are here02"+Q.tf.entrySet());
		//System.out.println();
		System.out.println(bm_25);
		//System.out.println("you are here02");
		this.bm_25 = bm_25;
		return bm_25;
	}


	
	//Informativeness score
	int informativeScore(Document question,MaxentTagger tagger)
	{
		int informationCount = 0;
		String unmatchedString =""; //string containing the words in the answer which are not in the question
		KnuthMorrisPratt kmp = new KnuthMorrisPratt();
		String [] nonStopWords = this.nonStopString.split("[,\\n,\\?,\\t, ,.,]");
		for(String word:nonStopWords)
		{
			if(word.length()!=0){
				if(kmp.find(question.string, word)==0)
				{
					unmatchedString = unmatchedString + word+" ";
				}
			}
		}
		System.out.println(nonStopWords);
		String tagged = tagger.tagString(unmatchedString);
		System.out.println(tagged);
		
		informationCount = informationCount + kmp.find(tagged, "/JJ"); //add number of adjectives
		informationCount = informationCount + kmp.find(tagged, "/VB"); //add number of verbs
		informationCount = informationCount + kmp.find(tagged, "/NN"); //add number of nouns
		this.informationCount = informationCount;
		return informationCount;
	}
	
	//Overall Match(Words) score
	//The number of non stop stemmed question words found in the answer
	int overallMatch_Words(Document question)
	{
		int overallMatch = 0;
		KnuthMorrisPratt kmp = new KnuthMorrisPratt();
		String [] nonStopWords = question.nonStopString.split("[,\\n,\\?,\\t, ,]");
		porterStemmer stemmer = new porterStemmer();


		for(int k=0;k<nonStopWords.length;k++) {

			stemmer.setCurrent(nonStopWords[k]);
			stemmer.stem();
			nonStopWords[k] = stemmer.getCurrent();
		}
		for(String word : nonStopWords)
		{
			if(this.tf.containsKey(word)==true)
				overallMatch = overallMatch + 1;
		}
		
		return overallMatch;
	}
	
	

}

