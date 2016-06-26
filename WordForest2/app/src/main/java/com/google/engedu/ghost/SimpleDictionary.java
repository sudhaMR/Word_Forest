package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int n=prefix.length();

        if(n==0) {
            Random random = new Random();
            int I = random.nextInt(words.size());
            return words.get(I);
        }
        else
        {   int beg =0,end=words.size();
            int mid;
            while (beg<=end) {
                mid=(beg+end)/2;
                String word = words.get(mid);

                while(word.length() < n){
                    mid++;
                    word = words.get(mid);
                }

                if( word.substring(0,n).equals(prefix))
                    return word;
                if(prefix.compareTo(word.substring(0,n))<0)
                    end =mid-1;
                else
                    beg=mid+1;
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
