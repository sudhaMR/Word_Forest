package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }
    private String con=null;
    public void add(String s) {
        HashMap<Character, TrieNode> C=children;
        for(int i=0;i<s.length();i++) {
            TrieNode t;
            char c= s.charAt(i);
            if(C.containsKey(c))
              t=C.get(c);
            else
            { t= new TrieNode();
              C.put(c,t);
            }
            C = t.children;

            if(i==s.length()-1)
                t.isWord=true;
            //else
              //  t.isWord=false;
        }
    }
    public TrieNode searchNode(String s) {
        HashMap<Character, TrieNode> C=children;
        TrieNode t = null;
        char c;
        for(int i=0;i<s.length();i++){
            c= s.charAt(i);
            if(C.containsKey(c)) {
                t = C.get(c);
                C=t.children;
            }
            else
                return null;
        }
        return t;
    }
    public boolean isWord(String s) {
       TrieNode t= searchNode(s);

       if(t!=null && t.isWord)
           return true;
       else
           return false;
    }

    public TrieNode findnext(HashMap<Character, TrieNode> C) {
        Random random = new Random();
        TrieNode t;
        int flag =0;
        Set<Character> key = C.keySet();
        int sz=key.size(),i,j;
        Iterator I = key.iterator();
        while(flag==0) {
             i = random.nextInt(sz)+1;
            for(j=1;j<i;j++) {
                I.next();
            }
            char c = (char)I.next();
            if( C.containsKey(c)) {
                t = C.get(c);
                String s= Character.toString(c);
                con=con+s;
                flag=1;
                Log.i("charsel",s);
                return t;
            }
        }


        return null;
    }
    public String getAnyWordStartingWith(String prefix) {
        TrieNode t= searchNode(prefix);
        HashMap<Character, TrieNode> C;
        con="";
        while(t!=null && !t.isWord) {
            C=t.children;
            t=findnext(C);
        }
        prefix=prefix+con;
        return prefix;
    }

    public String getGoodWordStartingWith(String prefix) {
        TrieNode t= searchNode(prefix),x;
        HashMap<Character, TrieNode> C;
        int i=0,count=0;
        con="";
        while(t!=null && !t.isWord) {
            C=t.children;
            i++;
            x=t;
            t=findnext(C);
            if(t.isWord && i==1 && count!=25 ){
                con="";
                t=x;
                i=0;
                count++;
            }

        }
        prefix=prefix+con;
        return prefix;
    }
}
