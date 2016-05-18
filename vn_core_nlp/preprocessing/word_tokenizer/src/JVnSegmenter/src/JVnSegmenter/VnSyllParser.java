/*
    Copyright (C) 2007 by Cam-Tu Nguyen
 
    Email:	ncamtu@gmail.com
 
    Department of Information System,
    College of Technology
    Hanoi National University, Vietnam
 */
package JVnSegmenter;

import java.util.*;
import java.io.*;
class TONE{
    public static final TONE NO_TONE = new TONE(0);
    public static final TONE ACUTE = new TONE(1);
    public static final TONE ACCENT = new TONE(2);
    public static final TONE QUESTION = new TONE(3);
    public static final TONE TILDE = new TONE(4);
    public static final TONE DOT = new TONE(5);
    
    public static TONE getTone(int v){
        switch (v){
            case 0: return NO_TONE;
            case 1: return ACUTE;
            case 2: return ACCENT;
            case 3: return QUESTION;
            case 4: return TILDE;
            case 5: return DOT;
            default: return NO_TONE;
        }
    }
    public int getValue(){return value;}
    
    private TONE(int v){value = v;}
    private int value;
}

/*
 * This class parse a vietnamese syllable
 * in UTF-8 encoding
 */
public class VnSyllParser {
//Member Data
    private static final String vnFirstConsonants = "ngh|ng|gh|ph|ch|tr|nh|kh|th|m|b|v|t|\u0111|n|x|s|l|h|r|d|gi|g|q|k|c";
    private static final String vnLastConsonants = "ng|nh|ch|p|t|c|m|n|u|o|y|i";
    private static final String vnMainVowels = "i\u00EA|y\u00EA|ia|ya|\u01B0\u01A1|\u01B0a|u\u00F4|ua|oo|\u00EA|e|a|\u01B0|\u0103|o|\u01A1|\u00E2|\u00F4|u|i|y|";
    private static final String vnSecondaryVowels = "o|u";
    public static final String ZERO = "";
    
    private static String vnVowels = "a\u00E1\u00E0\u1EA3\u00E3\u1EA1"
            + "\u0103\u1EAF\u1EB1\u1EB3\u1EB5\u1EB7"
            + "\u00E2\u1EA5\u1EA7\u1EA9\u1EAB\u1EAD"
            + "e\u00E9\u00E8\u1EBB\u1EBD\u1EB9"
            + "\u00EA\u1EBF\u1EC1\u1EC3\u1EC5\u1EC7"
            + "i\u00ED\u00EC\u1EC9\u0129\u1ECB"
            + "o\u00F3\u00F2\u1ECF\u00F5\u1ECD"
            + "\u00F4\u1ED1\u1ED3\u1ED5\u1ED7\u1ED9"
            + "\u01A1\u1EDB\u1EDD\u1EDF\u1EE1\u1EE3"
            + "u\u00FA\u00F9\u1EE7\u0169\u1EE5"
            + "\u01B0\u1EE9\u1EEB\u1EED\u1EEF\u1EF1"
            + "y\u00FD\u1EF3\u1EF7\u1EF9\u1EF5";
    
    private static ArrayList alFirstConsonants;
    private static ArrayList alLastConsonants;
    private static ArrayList alMainVowels;
    private static ArrayList alSecondaryVowels;
    
    private String strSyllable;
    private String strMainVowel;
    private String strSecondaryVowel;
    private String strFirstConsonant;
    private String strLastConsonant;
    private TONE tone = TONE.NO_TONE;
    private int iCurPos;
    private boolean validViSyll;
    //private boolean validSyll;
    
//Public Methods
    public	VnSyllParser(String syll){
        init();
        parseVnSyllable(syll);
    }
    
    public VnSyllParser() {
        init();
    }
    
    public void parseVnSyllable(String syll){
        strSyllable = syll;
        strMainVowel = "";
        strSecondaryVowel = "";
        strFirstConsonant = "";
        strLastConsonant = "";
        iCurPos = 0;
        validViSyll = true;
        
        parseFirstConsonant();
        parseSecondaryVowel();
        parseMainVowel();
        parseLastConsonant();
    }
    
    public String getFirstConsonant(){
        return strFirstConsonant;
    }
    
    public String getSecondVowel(){
        return strSecondaryVowel;
    }
    
    public String getMainVowel(){
        return strMainVowel;
    }
    
    public String getLastConsonant(){
        return strLastConsonant;
    }
    
    public TONE getTone(){
        return tone;
    }
    
    public boolean isValidVnSyllable(){return validViSyll;}
    
//Private Methods
    private void parseFirstConsonant(){
        //find first of (vnfirstconsonant)
        //if not found, first consonant = ZERO
        //else the found consonant
        Iterator iter = alFirstConsonants.iterator();
        while (iter.hasNext()){
            String strFirstCon = (String) iter.next();
            if (strSyllable.startsWith(strFirstCon, iCurPos)){
                strFirstConsonant = strFirstCon;
                iCurPos += strFirstCon.length();
                return;
            }
        }
        strFirstConsonant = ZERO;
    }
    
    private void parseSecondaryVowel(){
        if (!validViSyll) return;
        //get the current and next character in the syllable string
        char curChar, nextChar;
        if (iCurPos > strSyllable.length() - 1){
            validViSyll = false;
            return;
        }
        curChar = strSyllable.charAt(iCurPos);
        
        if (iCurPos == strSyllable.length() - 1) nextChar = '$';
        else nextChar = strSyllable.charAt(iCurPos + 1);
        
        //get the tone and the original vowel (without tone)
        TONE tone = TONE.NO_TONE;
        int idx1 = vnVowels.indexOf(curChar);
        int idx2 = vnVowels.indexOf(nextChar);
        
        if (idx1 == -1) return;//current char is not a vowel
        tone = TONE.getTone(idx1 % 6);
        curChar = vnVowels.charAt((idx1 / 6) * 6);
        
        if (idx2 == -1){ //next char is not a vowel
            strSecondaryVowel = ZERO;
            return;
        }
        nextChar = vnVowels.charAt( (idx2 / 6 ) *6);
        if (tone.getValue() == TONE.NO_TONE.getValue()) tone = TONE.getTone(idx2 % 6);
        
        //Check the secondary vowel
        if (curChar == 'o'){
            if (nextChar == 'a' || nextChar == 'e'){
                strSecondaryVowel += curChar;
                iCurPos++;
            } else strSecondaryVowel = ZERO; //oo
            return;
        } else if (curChar == 'u'){
            if (nextChar != 'i' && nextChar != '$'){
                strSecondaryVowel += curChar;
                iCurPos++;
            }else strSecondaryVowel = ZERO;
            return;
        }
    }
    
    private void parseMainVowel(){
        if (!validViSyll)return;
        if (iCurPos > strSyllable.length() - 1){
            validViSyll = false;
            return;
        }
        
        String strVowel = "";
        for (int i = iCurPos; i < strSyllable.length(); ++ i){
            int idx = vnVowels.indexOf(strSyllable.charAt(i));
            if (idx == -1) break;
            
            strVowel += vnVowels.charAt((idx/6)*6);
            if (tone.getValue() == TONE.NO_TONE.getValue()) tone = TONE.getTone(idx%6);
        }
        
        Iterator iter = alMainVowels.iterator();
        while (iter.hasNext()){
            String tempVowel = (String) iter.next();
            if (strVowel.startsWith(tempVowel)){
                strMainVowel = tempVowel;
                iCurPos += tempVowel.length();
                return;
            }
        }
        validViSyll = false;
        return;
    }
    
    private void parseLastConsonant(){
        if (!validViSyll) return;
        if (iCurPos > strSyllable.length()) strLastConsonant = ZERO;
        String strCon = strSyllable.substring(iCurPos, strSyllable.length());
        
        if (strCon.length() > 3){
            validViSyll = false;
            return;
        }
        
        Iterator iter = alLastConsonants.iterator();
        while (iter.hasNext()){
            String tempLastCon = (String) iter.next();
            if (strCon.equals(tempLastCon)){
                strLastConsonant = tempLastCon;
                iCurPos += strLastConsonant.length();
                return;
            }
        }
        strLastConsonant = ZERO;
        return;
    }
    
    private static void init(){
        if (alFirstConsonants == null){
            alFirstConsonants = new ArrayList();
            alLastConsonants = new ArrayList();
            alMainVowels = new ArrayList();
            alSecondaryVowels = new ArrayList();
            
            initArrayList(alFirstConsonants, vnFirstConsonants);
            initArrayList(alLastConsonants, vnLastConsonants);
            initArrayList(alMainVowels, vnMainVowels);
            initArrayList(alSecondaryVowels, vnSecondaryVowels);
        }
    }
    
    private static void initArrayList(ArrayList al, String str){
        StringTokenizer strTknr = new StringTokenizer(str, "|");
        while (strTknr.hasMoreTokens()){
            al.add(strTknr.nextToken());
        }
    }
}

