//mdm
//mdm


/*
package main;

import java.io.IOException;


public class Main {
	
	public static void main(String[] args) throws IOException {

		Corpus corpus = new Corpus();

		corpus.addDocument(new Document("solr is an complete search engine"));
		corpus.addDocument(new Document("solr is an open source search plateform"));
		corpus.addDocument(new Document("solr is an search library"));
		corpus.addDocument(new Document("It is a popular search platform for Web sites because it can index and search multiple sites and return recommendations for related content based on the search queryâ€™s taxonomy"));

		System.out.println("you are here");
		System.out.println(corpus.getIDF("solr"));
		System.out.println("you are here");
		Document Q = new Document("what is solr source");
		
		for(Document A:corpus.collection) {
			System.out.println(A.words[0]);
			Q.getBM25Score(A, corpus);
		}
		
	}
	
	
	
	
}*/
package main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.nlp.mt.GizaFormatException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;



public class Main {
	
	//@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, GizaFormatException {

		Corpus corpus = new Corpus();


		corpus.addDocument(new Document("/Tarun Malhotra/codechef/temp.txt"));
		corpus.addDocument(new Document("weight can be loosed by doing yoga",1));
		corpus.addDocument(new Document("weight can be reduced by doing yoga ",1));
		corpus.addDocument(new Document("solring is an complete search engine",1));
		corpus.addDocument(new Document("solr is an open source search plateform",1));
		corpus.addDocument(new Document("solr is an search library",1));
		corpus.addDocument(new Document("It is a popular search platform for Web sites because it can index and search multiple sites and return recommendations for related content based on the search query’s taxonomy",1));
		
		Document Q = new Document("how can we loose weight,",1);
		
		for(Document A:corpus.collection) {
			
			Q.getBM25Score(A, corpus);
		}
		
		System.out.println(corpus.tokens.entrySet());
	
		//System.out.println(Q.tf.entrySet());
		System.out.println("-------------------------------------------------");
		Stopwords stop = new Stopwords();
		
		
		// create strings without stop words
      for(Document A:corpus.collection) {
			
		  A.nonStopString = stop.removeStopWords(A.string);
          System.out.println(A.string);
          System.out.println(A.nonStopString);
		}
      
      
      
      /*
      String one = "how can you weight loose weight, it is very important please weight";
      String two = "weight";
		KnuthMorrisPratt kmp = new KnuthMorrisPratt();
		
		System.out.println(kmp.find(one,two));
	    */
	    
	    
		// Initialize the tagger
		 
		MaxentTagger tagger = new MaxentTagger("D:/IIITD/Monsoon Sem'15/AI/Haiderali work/stanford-postagger-2011-04-20/stanford-postagger-2011-04-20/stanford-postagger-2011-04-20/models/left3words-wsj-0-18.tagger");
		//MaxentTagger tagger = new MaxentTagger("taggers/left3words-distsim-wsj-0-18.tagger");
		/*
		// The sample string
		 
		String sample = "This is a sample text.He was playing.He played very well.Hence you can say that he is a very good player.";
		 
		// The tagged string
		 
		String tagged = tagger.tagString(sample);
		 
		// Output the result
		 
		System.out.println(tagged);
		*/
		
		System.out.println("-------------------------------------");
	
		//calculate informativeness for all answers in the corpus
      for(Document A:corpus.collection) {
			
			System.out.println(A.informativeScore(Q, tagger));
		}
		
      System.out.println("-------------------------------------");
      
      // Find non-stop words in the question
      Q.nonStopString = stop.removeStopWords(Q.string);
      System.out.println(Q.nonStopString);
      
      //calculate the number of non-stop stemmed question words found in the answer "OVERALL MATCH(W)"
      for(Document A:corpus.collection) {
			
			System.out.println(A.overallMatch_Words(Q));
		}
      
		
		//GizaAlignmentReader giza = new GizaAlignmentReader("D:/Tarun Malhotra/codechef/gizaTemp.txt");
		
		//
		//giza.getAllAlignments();
		
		WordtoWordTranslationProbability wd = new WordtoWordTranslationProbability();
		double val = wd.calcW2Wsentencesimilarity("How is he playing","This is a sample text.He was playing.He played very well.Hence you can say that he is a very good player.");
		System.out.println(val);

		//Stopwords stop = new Stopwords();
		
	
		//System.out.println(stop.removeStopWords("Job in a software factory. Work with Agile, Spring, Hibernate, GWT, etc."));
		/*
		for(Document A:corpus.collection) {
			String output[]=stop.removeStopWords(A.string);
			System.out.println(output);
		}
		*/
		
	}
	
	
	
	
}