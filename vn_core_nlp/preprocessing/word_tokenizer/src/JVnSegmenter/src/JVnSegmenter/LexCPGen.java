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

public class LexCPGen {
    
//Static Member Data
    private static HashSet hsVietnameseDict;
    private static HashSet hsViFamilyNames;
    private static HashSet hsViMiddleNames;
    private static HashSet hsViLastNames;
    private static HashSet hsViLocations;
    
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
        
//		generate context predicates
        String cp = "";
        String suffix = "";
        String word = "";
        
        for (int i = 0; i < posArr.length; ++ i){
            int pos = curPos + posArr[i];
            if (pos < 0 || pos >= os.length())
                return cp;
            
            suffix += posArr[i] + ":";
            word += os.getToken(column, curPos + posArr[i]) + " ";
        }
        word = word.trim().toLowerCase();
        suffix = suffix.substring(0, suffix.length() - 1);
        
        if (name.equals("vietnamese_dict")){
            if (inVietnameseDict(word)) cp = "d:" + suffix;
        } else if (name.equals("family_name")){
            if (inViFamilyNameList(word)) cp = "fam:" + suffix;
        } else if (name.equals("middle_name")){
            if (inViMiddleNameList(word)) cp = "mdl:" + suffix;
        } else if (name.equals("last_name")){
            if (inViLastNameList(word)) cp = "lst:" + suffix;
        } else if (name.equals("location")){
            if (inViLocations(word)) cp = "loc:" + suffix;
        }
        
        return cp;
    }
    
    public static boolean inVietnameseDict(String word){
        return hsVietnameseDict.contains(word);
    }
    
    public static boolean inViFamilyNameList(String word){
        return hsViFamilyNames.contains(word);
    }
    
    public static boolean inViMiddleNameList(String word){
        return hsViMiddleNames.contains(word);
    }
    
    public static boolean inViLastNameList(String word){
        return hsViLastNames.contains(word);
    }
    
    public static boolean inViLocations(String word){
        return hsViLocations.contains(word);
    }
    
    public static void loadVietnameseDict(InputStream in){
        try{
            if (hsVietnameseDict == null){
                hsVietnameseDict = new HashSet();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(in, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null){
                    if (line.substring(0,2).equals("##")){
                        String word = line.substring(2);
                        word = word.toLowerCase();
                        hsVietnameseDict.add(word);
                    }
                }
            }
            //Print lacviet_dict into lacviet.dict file
        } catch(Exception e){
            System.err.print(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void loadViPersonalNames(InputStream in){
        try{
            if (hsViFamilyNames == null){
                
                hsViFamilyNames = new HashSet();
                hsViLastNames = new HashSet();
                hsViMiddleNames = new HashSet();
                
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(in, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null){
                    line = line.trim();
                    if (line.equals("")) continue;
                    
                    int idxSpace = line.indexOf(' ');
                    int lastIdxSpace = line.lastIndexOf(' ');
                    
                    if (idxSpace != -1) {
                        String strFamilyName = line.substring(0, idxSpace);
                        hsViFamilyNames.add(strFamilyName);
                    }
                    
                    if ((idxSpace != -1) && (lastIdxSpace > idxSpace + 1)){
                        String strMiddleName = line.substring(idxSpace + 1, lastIdxSpace - 1);
                        hsViMiddleNames.add(strMiddleName);
                    }
                    
                    if (lastIdxSpace != -1){
                        String strLastName = line.substring(lastIdxSpace + 1, line.length() - 1);
                        hsViLastNames.add(strLastName);
                    }
                }
                in.close();
            }
        } catch(Exception e){
            e.printStackTrace();
            System.err.print(e.getMessage());
        }
    }
    
    public static void loadViLocationList(InputStream in){
        try{
            if (hsViLocations == null){
                hsViLocations = new HashSet();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(in, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null){
                    String word = line.trim();
                    word = word.toLowerCase();
                    hsViLocations.add(word);
                }
            }
        } catch(Exception e){
            System.err.print(e.getMessage());
        }
    }
}
