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

public class VnSyllFeaCPGen {
	
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
		if (!name.equals("not_valid_vnsyll")) return "";		
		String cp = "", word = "";		
		for (int i = 0; i < posArr.length; ++ i){
			int pos = curPos + posArr[i];
			if (pos < 0 || pos >= os.length())
				return cp;
		
			word += os.getToken(column, curPos + posArr[i]) + " ";
		}		
		word = word.trim().toLowerCase();
		
		//parse this syllable to check if it is a valid Vietnamese syllable or not.
		VnSyllParser parser = new VnSyllParser(word);
		if (!parser.isValidVnSyllable()) cp = "nvs:" + word;		
		return cp;
	}

}
