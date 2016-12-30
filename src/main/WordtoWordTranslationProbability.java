//mdm
//mdm
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.uima.jcas.JCas;
import org.ima.whatworks.utils.wordalign.*;
import org.ima.whatworks.utils.wordalign.wordalign.Constants;
import org.ima.whatworks.utils.wordalign.wordalign.TrainedWord2WordAlignment;
import org.ima.whatworks.utils.wordalign.wordalign.Word;

public class WordtoWordTranslationProbability {

  private static List<String> S1;

  private static List<String> S2;
  
 private static  String QS=" ";
  private static String AS=" ";
  /*private JCas jCas; 
  public WordtoWordTranslationProbability(JCas jCas){
  this.jCas = jCas;
  
  }*/
  public WordtoWordTranslationProbability() {
    this.S1 = new ArrayList<String>();
    this.S2 = new ArrayList<String>();
    
    for (String question : S1)
      this.S1.add(question);
      
    for (String answer : S2)
      this.S2.add(answer);
  }

 
  
  // unknown if SPACE is being counted or full stop is being counted in corpus or not.
  // also unknown where to take unique count in denominator
  //we have taken Corpus count in denominator to be uniqe for calculating PmlWbyC 
  // but we have not taken U to be unique while calculating PwbyV.
  public static double calcW2Wsentencesimilarity(String question, String answer) {
	  	  
	  double Lamda = 2000;
	  double Beta = 0.2;
	  String[] answerwords=answer.split("\\s+");
	  String[] questionwords=question.split("\\s+");
	  int CountAwords = answerwords.length;
	  double PbUV = 0.0;
	  
	  for ( String  questionword: questionwords)
	  {
		//String QACorpus = GetQACorpus("file1","file2");
		  
		String QACorpus = GetQACorpus("/Tarun Malhotra/codechef/answers.txt","/Tarun Malhotra/codechef/questions.txt");
		  
		
		int CountQWordinCorpus=WordFrequencyInSentence(questionword,QACorpus);
		String trimmed = QACorpus.trim();
		int CorpusWordCount = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		
		double PbmlWbyV = WordFrequencyInSentence(questionword,answer)/answerwords.length;
		
		double PbmxWbyV = (1-Beta)*PbmlWbyV+Beta*CalculatedProbability
				(QACorpus,questionword,answer );
		
		double PbmlWbyC = CountQWordinCorpus/CorpusWordCount;
		double PbWbyV=    (CountAwords/(CountAwords+Lamda))*PbmxWbyV +
				(Lamda/(CountAwords+Lamda))*PbmlWbyC;

		 PbUV +=PbWbyV; 

	  }
	 
	return PbUV;
	}

  private static double CalculatedProbability(String QACorpus , String questionword,
		  String answer)
  {
	  
	String trimmed = QACorpus.trim();
	String[] Words = trimmed.split("\\s+");
	double W2WProb = 0.0;
	double PmlTbyV= 0.0;
	double calcProbability = 0.0;
		
		Word [] Corpus = getCorpusWords(Words);
		
		for (Word wordincorpus:Corpus)
		{  
			W2WProb = LookupWordtoWordTranslationProbability(questionword,wordincorpus.word);
			PmlTbyV = WordFrequencyInSentence(wordincorpus.word,answer);
			calcProbability+=W2WProb*PmlTbyV;
		}
		return calcProbability;	  
  }
  private static double LookupWordtoWordTranslationProbability(String translationto,String translationfrom)
  {
	  
		TrainedWord2WordAlignment word2word;
		word2word = new TrainedWord2WordAlignment(
				Constants.STR_GIZAPP_ALIGNMENT_RESULT_FILE, 
				Constants.STR_GIZAPP_SOURCE_VOCABULARY_FILE, 
				Constants.STR_GIZAPP_TARGET_VOCABULARY_FILE);
		
		String srcWord = translationfrom;
		String targetWord = translationto;		

		boolean isAligned = word2word.isAligned(srcWord, targetWord);
if (isAligned)
	  return word2word.getProbability(srcWord, targetWord);
else
	  return 0;

  }
  
  
  
  public double calculatePWS2(String W, String S2) {
    double bita = 0.2;
    double LastPW1W2 = 0.7;

    double CountPWS2 = (1 - bita) * PWS2("W1", "S2");
    double Totalvalue = LastPW1W2 * PtS2("QA", "S2");
    double CountPW1W2 = bita * Totalvalue;
    double TotalCount = CountPWS2 + CountPW1W2;
    return TotalCount;

  }

  private static int WordFrequencyInSentence(String W, String sentence) {
    String words[] = sentence.split(" ");
    Word[] frequency = getFrequentWords(words);
    for (Word w : frequency) {
      if (w.word == W)
        return w.count;

    }
    return 0;
  }

  private static  Word[] getFrequentWords(String words[]) {
    HashMap<String, Word> map = new HashMap<String, Word>();
    for (String s : words) {
      Word w = map.get(s);
      if (w == null)
        w = new Word(s, 1);
      else
        w.count++;
      map.put(s, w);
    }
    Word[] list = map.values().toArray(new Word[] {});
    Arrays.sort(list);
    return list;
  }

  private double PWS2(String W, String S2) {
    String words[] = S2.split(" ");
    int totalcount = words.length + 1;
    Word[] frequency = getFrequentWords(words);
    for (Word w : frequency) {
      if (w.word == W)
        return new Double(w.count) / new Double(totalcount);
    }
    return 0;
  }

  private int CountWordInQA(String W, String QA) {
    String words[] = QA.split(" ");
    Word[] frequency = getCountedWords(words);
    for (Word w : frequency) {
      if (w.word == W)
        return w.count;
    }
    return 0;
  }

  private Word[] getCountedWords(String words[]) {
    HashMap<String, Word> map = new HashMap<String, Word>();
    for (String s : words) {
      Word w = map.get(s);
      if (w == null)
        w = new Word(s, 1);
      else
        w.count++;
      map.put(s, w);
    }
    Word[] list = map.values().toArray(new Word[] {});
    Arrays.sort(list);
    return list;
  }

  private double PWQA(String W, String QA) {
    String words[] = QA.split(" ");
    int totalcount = words.length + 1;
    Word[] frequency = getCountedWords(words);
    for (Word w : frequency) {
      if (w.word == W)
        return new Double(w.count) / new Double(totalcount);
    }
    return 0;
  }

  private int CountCorpusWordInS2(String W, String S2) {
    String words[] = S2.split(" ");
    Word[] frequency = getCorpusWords(words);
    for (Word w : frequency) {
      if (w.word == W)
        return w.count;
    }
    return 0;
  }

  private static Word[] getCorpusWords(String words[]) {
    HashMap<String, Word> map = new HashMap<String, Word>();
    for (String QA : words) {
      Word w = map.get(QA);
      if (w == null)
        w = new Word(QA, 1);
      else
        w.count++;
      map.put(QA, w);
    }
    Word[] list = map.values().toArray(new Word[] {});
    Arrays.sort(list);
    return list;
  }

  private double PtS2(String QA, String S2) {
    String words[] = S2.split(" ");
    int totalcount = words.length + 1;
    Word[] frequency = getCorpusWords(words);
    for (Word w : frequency) {
      if (w.word == QA)
        return new Double(w.count) / new Double(totalcount);
    }
    return 0;
  }

  
	  
	  private static String GetQACorpus(String QuestionCorpusFilePath,String AnswerCorpusFilePath )
	  {
		  try {
	  
			  
	         String questionfilecontent = new Scanner(new File( QuestionCorpusFilePath)).useDelimiter("\\Z").next();
	         String answerfilecontent = new Scanner(new File( AnswerCorpusFilePath)).useDelimiter("\\Z").next();
	         String combinedcontent =  questionfilecontent+" "+answerfilecontent;
	          return combinedcontent;
		  }
		  catch(Exception e){}

	  return "";
  }
  
}
