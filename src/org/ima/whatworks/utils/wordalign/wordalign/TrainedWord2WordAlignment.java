//mdm

package org.ima.whatworks.utils.wordalign.wordalign;

import java.io.*;
import java.util.*;

/**
 * 
 * @author v131
 *
 */

public class TrainedWord2WordAlignment {

	@SuppressWarnings("rawtypes")
	Map e2f;
	TrainedVocabulary srcVcb;
	TrainedVocabulary trgtVcb;
	public static boolean isInTrainingProgress = false;

	public TrainedWord2WordAlignment(String alignmentFile, String srcVcbFile, String trgtVcbFile) {
		System.out.print("Loading WORD ALIGNMENT source VCB file...");
		srcVcb = new TrainedVocabulary(srcVcbFile);
		System.out.println("Done");

		System.out.print("Loading WORD ALIGNMENT target VCB file...");
		trgtVcb = new TrainedVocabulary(trgtVcbFile);
		System.out.println("Done");

		System.out.print("Loading WORD ALIGNMENT trained model...");
		e2f = load(alignmentFile);
		System.out.println("Done");
	}

	@SuppressWarnings("unchecked")
	public List<WordAlignment> getAlignmentsById(int id) {
		if (e2f.containsKey(id) == true) {
			return (List<WordAlignment>) e2f.get(id);
		}
		return null;
	}
	
	public int getSourceWord(String word){
		return srcVcb.getWordId(word);
	}
	
	public int getTargetWord(String word){
		return trgtVcb.getWordId(word);
	}
	
	
	public String getSourceWord(int id){
		return srcVcb.getWord(id);
	}

	public String getTargetWord(int id){
		return trgtVcb.getWord(id);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map load(String alignmentFile) {
		Map result = new HashMap();

		// Read all file
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(alignmentFile)));
			String str = "";
			int currentId = -1;
			List<WordAlignment> lstAlignedWords = new ArrayList<WordAlignment>();
			String[] arr;
			while (str != null) {
				str = reader.readLine();
				if (str != null && str != "") {
					arr = str.split(" ");
					int id = Integer.parseInt(new String(arr[0]).trim());
					WordAlignment alignedWord = new WordAlignment();
					alignedWord.setId(Integer.parseInt(new String(arr[1]).trim()));
					alignedWord.setProb(Double.parseDouble(new String(arr[2]).trim()));

					if (currentId == -1 || currentId != id) {
						// save previous work
						if (currentId != -1) {
							result.put(currentId, lstAlignedWords);
						}

						// Prepare an alignedWord list for new id
						lstAlignedWords = new ArrayList<WordAlignment>();
						currentId = id;
					}
					lstAlignedWords.add(alignedWord);
					
				}
			}
		}
		catch (IOException e) {
			System.out.println("Error in loading trainedWordAlignment " + e);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (Exception ignored) {
				}
			}
		}

		return result;
	}

	public boolean isAligned(String srcWord, String targetWord) {
		int srcId, targetId;
		srcId = srcVcb.getWordId(srcWord.trim());
		targetId = trgtVcb.getWordId(targetWord.trim());
		if (srcId == -1 || targetId == -1) {
			return false;
		}
		List<WordAlignment> alignments = getAlignmentsById(srcId);
		if (alignments != null) {
			for (WordAlignment alignment : alignments) {
				if (alignment.getId() == targetId) {
					return true;
				}
			}
		}
		return false;
	}
	public double getProbability(String srcWord, String targetWord) {
		int srcId, targetId;
		srcId = srcVcb.getWordId(srcWord.trim());
		targetId = trgtVcb.getWordId(targetWord.trim());
		if (srcId == -1 || targetId == -1) {
			return 0;
		}
		List<WordAlignment> alignments = getAlignmentsById(srcId);
		if (alignments != null) {
			for (WordAlignment alignment : alignments) {
				if (alignment.getId() == targetId) {
					return alignment.getProb();
				}
			}
		}
		return 0;
	}
}