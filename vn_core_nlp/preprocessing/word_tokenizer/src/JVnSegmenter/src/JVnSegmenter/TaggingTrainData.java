/*
    Copyright (C) 2007 by Cam-Tu Nguyen
	
    Email:	ncamtu@gmail.com
	
    Department of Information System,
    College of Technology	
    Hanoi National University, Vietnam	
*/ 

package JVnSegmenter;
import java.beans.FeatureDescriptor;
import java.util.*;
import java.net.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TaggingTrainData {
	
    private static ArrayList ConjCPTpltList = new ArrayList();
    private static ArrayList RegexCPTpltList = new ArrayList();
    private static ArrayList SyllFeaCPTpltList = new ArrayList();
    private static ArrayList ViSyllFeaCPTpltList = new ArrayList();
    private static ArrayList LexCPTpltList = new ArrayList();
	
    public static void main(String[] av) {    	
        if (av.length != 3) {
            System.err.println("Usage: VWTaggingTrainData [train file] [tagged file] [model dir]");
            return;
        }
        try{
        	//Read feature template file........
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	
                String modelDir = av[2];
        	InputStream feaTplStream = new FileInputStream(modelDir + File.separator + "featuretemplate.xml");
        	Document doc = builder.parse(feaTplStream);
        	
        	Element root = doc.getDocumentElement();
        	NodeList childrent = root.getChildNodes();
        	for (int i = 0; i < childrent.getLength(); i++)
        		if (childrent.item(i) instanceof Element){
        			Element child = (Element) childrent.item(i);
        			add2CPTpltList(child);
        		}     
        	
        	//Read Data file and generate a tagged file
        	//load all resources        	
        	BufferedReader in = new BufferedReader(new
					InputStreamReader(new
					FileInputStream(av[0]), "UTF-8"));
		BufferedWriter out = new BufferedWriter(new
					 OutputStreamWriter(new
					 FileOutputStream(av[1]), "UTF-8"));
			
		LexCPGen.loadVietnameseDict(new FileInputStream(modelDir + File.separator + "VNDic_UTF-8.txt"));
        	LexCPGen.loadViLocationList(new FileInputStream(modelDir + File.separator + "vnlocations.txt"));
        	LexCPGen.loadViPersonalNames(new FileInputStream(modelDir + File.separator + "vnpernames.txt"));        	
		String line, sequence = "";		
			
                int count = 0;
                while( (line = in.readLine()) != null ){				
                        if (line.trim().length() == 0)
                        {
                     /*           doCPGen(sequence, out);
                                count++;
                                if (count % 50 == 1) System.out.print("\n");
                                System.out.print(".");

                                sequence = "";
                                out.newLine();*/
                                continue;
                        }
                        doCPGen(line, out);
                        count++;
                        if (count % 50 == 1) System.out.print("\n");
                        System.out.print(".");

                        //sequence = "";
                        out.newLine();
                        //sequence += line + "\n";				
                }
                //if (!sequence.trim().equals("")) doCPGen(sequence, out);
                out.close();
        }
        catch(Exception ex){
        	System.err.println(ex.getMessage());
        }        
    }
   
    public static void add2CPTpltList(Element node){    	
		NodeList childrent = node.getChildNodes();
		String cpType = node.getAttribute("value");
		
		for (int i = 0; i < childrent.getLength(); ++i)
			if (childrent.item(i) instanceof Element)
			{
				Element child = (Element) childrent.item(i);
				if (cpType.equals("Conjunction"))
					ConjCPTpltList.add(child.getAttribute("value"));				
				else if (cpType.equals("Lexicon"))
					LexCPTpltList.add(child.getAttribute("value"));
				else if (cpType.equals("Regex"))
					RegexCPTpltList.add(child.getAttribute("value"));
				else if (cpType.equals("SyllableFeature"))
					SyllFeaCPTpltList.add(child.getAttribute("value"));
				else if (cpType.equals("ViSyllableFeature"))
					ViSyllFeaCPTpltList.add(child.getAttribute("value"));
			}
    	    	
    }
    
    private static void doCPGen (String sequence, BufferedWriter out) throws IOException {
    	sequence = sequence.trim();					
        //OriginalSequence os = new OriginalSequence(2, true);
        //os.readIOB2Seq(sequence);
        OriginalSequence os = new OriginalSequence(2, true);
        os.readSentence(sequence);        
        
        for (int i = 0; i < os.length(); ++ i){
                String cp = "";
                Iterator iter = ConjCPTpltList.iterator();
                while (iter.hasNext()){							
                        cp = cp.trim() + " " + ConjunctCPGen.doCnxtPreGen(os, i, (String)iter.next());							
                }


                iter = LexCPTpltList.iterator();
                while (iter.hasNext())
                        cp = cp.trim() + " " + LexCPGen.doCnxtPreGen(os, i, (String)iter.next());


                iter = SyllFeaCPTpltList.iterator();
                while (iter.hasNext())
                        cp = cp.trim() + " " + SyllFeaCPGen.doCnxtPreGen(os, i, (String)iter.next());

                iter = ViSyllFeaCPTpltList.iterator();
                while (iter.hasNext())
                        cp = cp.trim() + " " + VnSyllFeaCPGen.doCnxtPreGen(os, i, (String)iter.next());

                iter = RegexCPTpltList.iterator();
                while (iter.hasNext())
                        cp = cp.trim() + " " + RegexCPGen.doCnxtPreGen(os, i, (String)iter.next());

                out.write(cp);
                if (os.lable) out.write(" " + os.getToken(os.getNumOfColumn() -1 , i));
                out.newLine();
        }		
    }
}
