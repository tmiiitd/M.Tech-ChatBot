//mdm

package org.ima.whatworks.utils.wordalign.wordalign;

import java.io.*;
import java.util.*;
/**
 * 
 * @author v131
 *
 */
class TrainedVocabulary {
	public static final int NULL_WORD_ID = 0;

	@SuppressWarnings("rawtypes")
	Map word2id;
	List<String> lstWords;
	int count;

	@SuppressWarnings("rawtypes")
	public TrainedVocabulary(String vcbFile) {
		word2id = new HashMap();
		lstWords = new ArrayList<String>();
		load(vcbFile);
	}

	@SuppressWarnings("unchecked")
	private void load(String vcbFile) {
		word2id.clear();

		// Read all file
		count = 0;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(vcbFile), "UTF-8"));
			String str = "";
			while (str != null) {
				str = reader.readLine();
				if (str != null && str != "") {
					String[] arr = str.split(" ");
					int id = Integer.parseInt(new String(arr[0]).trim());
					String word = new String(arr[1]);
					word2id.put(word, id);
					lstWords.add(word);
					count++;
				}
			}
		}
		catch (IOException e) {

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

	}

	public String getWord(int id) {
		if (id < 2 || id >= lstWords.size() + 2){
			
		}
		return lstWords.get(id - 2);
	}

	public int getWordId(String word) {
		if (word2id.containsKey(word) == true) {
			return (Integer) word2id.get(word);
		}
		return -1;
	}
}