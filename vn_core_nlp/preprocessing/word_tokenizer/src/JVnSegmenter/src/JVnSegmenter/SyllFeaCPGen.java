/*
    Copyright (C) 2007 by Cam-Tu Nguyen
 
    Email:	ncamtu@gmail.com
 
    Department of Information System,
    College of Technology
    Hanoi National University, Vietnam
 */

package JVnSegmenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class SyllFeaCPGen {
    
    public static String doCnxtPreGen(OriginalSequence os, int curPos, String cpInfo){
        //Parse information of this context predicate
        //cpInfo : <col>:<order>:<name>:<position pattern>
        StringTokenizer colonTknr = new StringTokenizer(cpInfo, ":");
        
        int [] posArr = new int[colonTknr.countTokens() - 3];
        
        int column = Integer.parseInt(colonTknr.nextToken());
        int order = Integer.parseInt(colonTknr.nextToken());
        String name = colonTknr.nextToken();
        
        if (column > os.getNumOfColumn()) return "";
        for (int i = 0; i < posArr.length; i ++)
            posArr[i] = Integer.parseInt(colonTknr.nextToken());
        
        //generate context predicates
        String cp = "";
        String suffix = "";
        String word = "";
        
        for (int i = 0; i < posArr.length; ++ i){
            int pos = curPos + posArr[i];
            if (pos < 0 || pos >= os.length())
                return cp;
            
            suffix += os.getToken(column, curPos + posArr[i]) + ":";
            word += os.getToken(column, curPos + posArr[i]) + " ";
        }
        word = word.trim();
        suffix = suffix.substring(0, suffix.length() - 1);
        
        //System.out.println(word);
        if (name.equals("initial_cap")){
            if (isInitCap(word)) cp = "ic:" + suffix;
        } else if (name.equals("first_obsrv")){
            if (curPos + posArr[0] == 0 ) cp = "fi:" + posArr[0];
        } else if (name.equals("marks")){
            if (isMarks(word)) cp = "ma:" + suffix;
        } else if (name.equals("all_cap")){
            if (isAllCapCharAndDigit(word)) cp = "ac:" + suffix;
        }
        return cp;
    }
    
    private static boolean isInitCap(String word){
        try {
            if (isAllCapCharAndDigit(word)) return false;
            char initChar = word.charAt(0);
            return Character.isUpperCase(initChar);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    private static boolean isAllCapCharAndDigit(String word){
        boolean containChar = false;
        for (int i = 0; i < word.length(); ++i){
            char c = word.charAt(i);
            if (Character.isLetter(c)){
                if (!containChar) containChar = true;
                if (Character.isLowerCase(c)) return false;
            } else if (!Character.isDigit(c)) return false;
        }
        if (!containChar) return false;
        return true;
    }
    
    private static boolean isMixCharAndDigit(String word){
        boolean containChar = false;
        for (int i = 0; i < word.length(); ++i){
            char c = word.charAt(i);
            if (Character.isLetter(c))
                if (!containChar) containChar = true;
                else if (!Character.isDigit(c)) return false;
        }
        if (!containChar) return false;
        return true;
    }
    
    private static boolean isMarks(String word){
        for (int i = 0; i < word.length(); ++i){
            char c = word.charAt(i);
            if (Character.isLetterOrDigit(c)) return false;
        }
        return true;
    }
}
