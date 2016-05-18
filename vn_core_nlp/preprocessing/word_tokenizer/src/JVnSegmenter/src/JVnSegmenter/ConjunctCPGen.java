/*
    Copyright (C) 2007 by Cam-Tu Nguyen
	
    Email:	ncamtu@gmail.com
	
    Department of Information System,
    College of Technology	
    Hanoi National University, Vietnam	
*/ 

package JVnSegmenter;

import java.io.*;
import java.util.Collections;
import java.util.StringTokenizer;

public class ConjunctCPGen {
	
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
		String prefix = "s:";
		
		for (int i = 0; i < posArr.length; ++ i){
			int pos = curPos + posArr[i];
			if (pos < 0 || pos >= os.length())
				return cp;
			
			prefix += posArr[i] + ":"; 
		}
		
		String suffix = "";
		for (int i = 0; i < posArr.length - 1; ++i){
			int pos = curPos + posArr[i];
			suffix += os.getToken(column, pos) + ":"; 
		}
		suffix += os.getToken(column, curPos + posArr[posArr.length - 1]);
		
		cp = prefix + suffix;		
		return cp;
	}
}
