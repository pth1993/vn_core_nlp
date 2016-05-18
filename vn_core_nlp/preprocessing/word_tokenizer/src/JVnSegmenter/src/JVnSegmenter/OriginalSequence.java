/*
    Copyright (C) 2007 by Cam-Tu Nguyen
	
    Email:	ncamtu@gmail.com
	
    Department of Information System,
    College of Technology	
    Hanoi National University, Vietnam	
*/ 

package JVnSegmenter;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class OriginalSequence {
//Member Data
	private int iNumOfColumn;	
	private int row;	
	boolean lable;	
	String [][] seqArray = null;

//Methods
	public OriginalSequence(int col, boolean lbl){
		iNumOfColumn = col;		
		lable = lbl;		
	}

	public void readIOB2Seq(String seqStr){
		StringTokenizer lineTknr = new StringTokenizer(seqStr, "\n");
		seqArray = new String[lineTknr.countTokens()][iNumOfColumn];
		
		int obsrvNo = 0;
		while(lineTknr.hasMoreTokens()){
			String obsvr = lineTknr.nextToken();
			StringTokenizer spaceTknr = new StringTokenizer(obsvr, " \t");
			
			if (iNumOfColumn > spaceTknr.countTokens())
				iNumOfColumn = spaceTknr.countTokens() - 1;
			
			int colNo = 0;
			while (spaceTknr.hasMoreTokens()){
				seqArray[obsrvNo][colNo] = spaceTknr.nextToken();
				colNo++;
			}
			
			obsrvNo++;
		}
		row = obsrvNo;		
	}

	public void readSentence(String sentence){
                //System.out.println(sentence);
		StringTokenizer spaceTknr = new StringTokenizer(sentence, " ");
		seqArray = new String[spaceTknr.countTokens()][iNumOfColumn];
		
                int iObsvrNo = 0;
		while (spaceTknr.hasMoreTokens()){
                        String nextSyll = spaceTknr.nextToken();                        
                	StringTokenizer slashTknr = new StringTokenizer(nextSyll, Option.inputSeparator);
			if (iNumOfColumn != slashTknr.countTokens())
				continue;
			
			int iCol = 0;
			while (slashTknr.hasMoreTokens()){
                                String temp = slashTknr.nextToken();                                   
				seqArray[iObsvrNo][iCol++] = temp;                                                                				
			}                        
                        iObsvrNo++;
		}
                row = iObsvrNo;         
            
	}
	public int getNumOfColumn(){ return iNumOfColumn;}	

	public int length(){return row;}	
	
	public String getToken(int c,int pos){		
		if (c > iNumOfColumn) return "";
		if (pos > row) return "";
		return seqArray[pos][c];		
	}
	
	public void print(){
		for (int i = 0; i < row ; ++i){
			for (int j = 0; j < iNumOfColumn; ++j){
				System.out.print(seqArray[i][j]);
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
        
        public void print(BufferedWriter out){
            try{               
		for (int i = 0; i < row ; ++i){
			for (int j = 0; j < iNumOfColumn; ++j){
				out.write(seqArray[i][j]);
				out.write("\t");
			}
                        
			out.newLine();
		}
            } 
            catch (Exception e){
                System.out.println(e.getMessage());
                System.exit(1);
            }
	}
}
