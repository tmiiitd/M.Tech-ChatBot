//mdm

package org.ima.whatworks.utils.wordalign.wordalign;

//package org.ima.whatworks.utils;

import java.util.Arrays;
import java.util.HashMap;

public class Word  implements Comparable<Word>{
    public String word;
    public int count;
    public Word(String word, int count) {
        this.word = word;
        this.count = count;
    }
    public int compareTo(Word otherWord) {
        if(this.count==otherWord.count){
            return this.word.compareTo(otherWord.word);
        }
        return otherWord.count-this.count;
    }


}