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
import java.util.regex.*;

public class RegexCPGen {
//Regular Expression Pattern string
    private static String strNumberPattern = "[+-]?\\d+([,.]\\d+)*";
    private static String strShortDatePattern = "\\d+[/-:]\\d+";
    private static String strLongDatePattern = "\\d+[/-:]\\d+[/-:]\\d+";
    private static String strPercentagePattern = strNumberPattern + "%";
    private static String strCurrencyPattern = "\\p{Sc}" + strNumberPattern ;
    private static String strViCurrencyPattern = strNumberPattern + "\\p{Sc}";
    
//Regular Expression Pattern
    private static Pattern ptnNumber;
    private static Pattern ptnShortDate;
    private static Pattern ptnLongDate;
    private static Pattern ptnPercentage;
    private static Pattern ptnCurrency;
    private static Pattern ptnViCurrency;
    
//Methods
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
        String suffix = "", regex = "";
        String word = "";
        
        //get the context information from sequence
        for (int i = 0; i < posArr.length; ++ i){
            int pos = curPos + posArr[i];
            if (pos < 0 || pos >= os.length())
                return cp;
            
            suffix += posArr[i] + ":";
            word += os.getToken(column, curPos + posArr[i]) + " ";
        }
        word = word.trim().toLowerCase();
        suffix = suffix.substring(0, suffix.length() - 1);
        suffix = ":" + suffix;
        
        //Comparing against a specific pattern
        regex  = patternMatching(name, word);
        if (!regex.equals("")) {
            cp = "re" + suffix + regex;
        }
        return cp;
    }
    
    private static void patternCompile(){
        try{
            ptnNumber = Pattern.compile(strNumberPattern);
            ptnShortDate = Pattern.compile(strShortDatePattern);
            ptnLongDate = Pattern.compile(strLongDatePattern);
            ptnPercentage = Pattern.compile(strPercentagePattern);
            ptnCurrency = Pattern.compile(strCurrencyPattern);
            ptnViCurrency = Pattern.compile(strViCurrencyPattern);
        } catch(PatternSyntaxException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        
    }
    
    private static String patternMatching(String ptnName, String input){
        String suffix = "";
        if (ptnNumber == null)patternCompile();
        
        Matcher matcher;
        if (ptnName.equals("number")){
            matcher = ptnNumber.matcher(input);
            if (matcher.matches())
                suffix = ":number";
        } else if (ptnName.equals("short_date")){
            matcher = ptnShortDate.matcher(input);
            if (matcher.matches()) suffix = ":short-date";
        } else if (ptnName.equals("long_date")){
            matcher = ptnLongDate.matcher(input);
            if (matcher.matches()) suffix = ":long-date";
        } else if (ptnName.equals("percentage")){
            matcher = ptnPercentage.matcher(input);
            if (matcher.matches()) suffix = ":percentage";
        } else if (ptnName.equals("currency")){
            matcher = ptnCurrency.matcher(input);
            if (matcher.matches()) suffix = ":currency";
            else {
                matcher = ptnViCurrency.matcher(input);
                if (matcher.matches()){
                    suffix = ":currency";
                }
            }
        }
        return suffix;
    }
}
