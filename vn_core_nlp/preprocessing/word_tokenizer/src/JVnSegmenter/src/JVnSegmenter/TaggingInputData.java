/*
    Copyright (C) 2007 by Cam-Tu Nguyen and Xuan-Hieu Phan
	
    Email:	ncamtu@gmail.com
	
    Department of Information System,
    College of Technology	
    Hanoi National University, Vietnam	
*/

package JVnSegmenter;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TaggingInputData {
    
//Data Members
    List data = null;
    List originalData = null; //list of raw sequence    
    private static List ConjCPTpltList = new ArrayList();
    private static List RegexCPTpltList = new ArrayList();
    private static List SyllFeaCPTpltList = new ArrayList();
    private static List ViSyllFeaCPTpltList = new ArrayList();
    private static List LexCPTpltList = new ArrayList();
    private boolean isInitialized = false;
    
//Function Members    
   //initialize current VnWordTaggingInputData
    public boolean init(String modelDir){
        try {
            isInitialized = false;
            
            //Read feature template file........
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream feaTplStream = new FileInputStream(modelDir + File.separator + "featuretemplate.xml");
            Document doc = builder.parse(feaTplStream);

            Element root = doc.getDocumentElement();
            NodeList childrent = root.getChildNodes();
            for (int i = 0; i < childrent.getLength(); i++)
                    if (childrent.item(i) instanceof Element){
                            Element child = (Element) childrent.item(i);
                            add2CPTpltList(child);
                    }     


            System.out.println("Loading resorces....");
            LexCPGen.loadVietnameseDict(new FileInputStream(modelDir + File.separator + "VNDic_UTF-8.txt"));
            LexCPGen.loadViLocationList(new FileInputStream(modelDir + File.separator + "vnlocations.txt"));
            LexCPGen.loadViPersonalNames(new FileInputStream(modelDir + File.separator + "vnpernames.txt"));        	
                
            isInitialized = true;
            return true;
        }
        catch (SAXException se){
            System.out.println(se.getMessage());
            return false;
        }
        catch (ParserConfigurationException pce){
            System.out.println("Error in parsing feature template file");
            return false;
        }        
        catch (IOException io){
            System.out.println("Couldn't open one of resouces in " + modelDir);
            return false;
        }        
    }

    //add all context predicate templates into the template list
    private void add2CPTpltList(Element node){    	
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
    	    	
    //read original data from a file
    public void readOriginalDataFromFile(String dataFile){
        try{
            BufferedReader fin = new BufferedReader (new InputStreamReader
                                     (new FileInputStream(dataFile), "UTF-8"));
             
            String line = null, text = "";            
            while ((line = fin.readLine()) != null ){
                text += line + "\n";
            }
            text = text.trim();
            
            readOriginalDataFromString(text);
            
        } catch (IOException io){
            System.out.println("Couldn't open data file" + dataFile);
            return;
        }
    }
    
    //read original data from a string
    public void readOriginalDataFromString(String text){
        //initialize rawData list
        if (originalData != null){
            originalData.clear();
        } else {
            originalData = new ArrayList();
        }
        
        //tokenize text into lines
        StringTokenizer txtTokenizer = new StringTokenizer(text, "\n\r");
        while (txtTokenizer.hasMoreTokens())
        {
            String line = txtTokenizer.nextToken();
            
             //create new original sequence
            OriginalSequence os = new OriginalSequence(1, false);
            os.readSentence(line);                
            originalData.add(os); 
            
        }
    }
    
    //generate context predicate for the data
    public void cpGen(Map cpStr2Int){
        if (data != null){
            data.clear();
        } else {
            data = new ArrayList();
        }       
        
        try{
        //go through each original sequence and generate the approciate sequence of observations
        System.out.println("Generating cps ...");        
        for (int i = 0; i < originalData.size(); i++){
            OriginalSequence os = (OriginalSequence)originalData.get(i);                                            
            List sequence = new ArrayList();
            
            for (int j = 0; j < os.length(); ++j){
                //genearate context predicate 
                if (!isInitialized){
                    System.out.println("Neccesary resources haven't been loaded yet!");
                    return;
                }
                
                List tempCps = new ArrayList();
                String cp = "";
                Iterator iter = ConjCPTpltList.iterator();
                while (iter.hasNext())							
                      tempCps.add(ConjunctCPGen.doCnxtPreGen(os, j, (String)iter.next()));							                

                iter = LexCPTpltList.iterator();
                while (iter.hasNext()){
                       cp =  LexCPGen.doCnxtPreGen(os, j, (String)iter.next());
                       if (!cp.equals("")) tempCps.add(cp);
                }

                iter = SyllFeaCPTpltList.iterator();
                while (iter.hasNext()){
                        cp = SyllFeaCPGen.doCnxtPreGen(os, j, (String)iter.next());
                        if (!cp.equals("")) tempCps.add(cp);
                }

                iter = ViSyllFeaCPTpltList.iterator();
                while (iter.hasNext()){
                        cp = VnSyllFeaCPGen.doCnxtPreGen(os, j, (String)iter.next());
                        if (!cp.equals("")) tempCps.add(cp);
                }

                iter = RegexCPTpltList.iterator();
                while (iter.hasNext()){
                        cp = RegexCPGen.doCnxtPreGen(os, j, (String)iter.next());
                        if (!cp.equals("")) tempCps.add(cp);
                }
                
                
                List tempCpsInt = new ArrayList();
                
                for (int k = 0; k < tempCps.size(); k++) {
                    Integer cpInt = (Integer)cpStr2Int.get(tempCps.get(k));                               
                    if (cpInt == null) {
                        continue;
                    }
                    tempCpsInt.add(cpInt);                  
                }                
                //create a new obvervation
                Observation obsr = new Observation();
                
                for (int k = 0; k < os.getNumOfColumn(); ++k)
                    obsr.originalData += os.getToken(k, j) + Option.inputSeparator;
                if (obsr.originalData.endsWith(Option.inputSeparator))
                    obsr.originalData = obsr.originalData.substring(0, obsr.originalData.length() - 1);
                
                obsr.cps = new int[tempCpsInt.size()];
	
                for (int k = 0; k < tempCpsInt.size(); k++) 
                    obsr.cps[k] = ((Integer)tempCpsInt.get(k)).intValue();
                
                //add this overvation to sequence                
                sequence.add(obsr);
            }
            //add sequence to data list            
            data.add(sequence);
        }
        originalData.clear();        
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
   
    //get data in the format <original data>| label
    public String getLabledData(Map lbInt2Str){
        if (data == null)
            return null;
        
        String result = "";
        //main loop for getting labled data
        for (int i = 0; i < data.size(); ++i){
            List seq = (List) data.get(i);
            for (int j = 0; j < seq.size(); j++){
                Observation obsr = (Observation) seq.get(j);
                result += obsr.toString(lbInt2Str) + " ";
            }
            
            result += "\n";
        }
        return result.trim();
    }
    
    //get the data with words surrounded by [ and ]
    public String getWordMarkedData(Map lbInt2Str){
        if (data == null)
            return null;
        
        String result = "";
        for (int i = 0; i < data.size(); ++i){
            List seq = (List) data.get(i);
            for (int j = 0;  j < seq.size(); j++){
                Observation obsr = (Observation) seq.get(j);
                
                //get the next observation in this sequence if available
                Observation nxtObsr = null;
                if (j < seq.size() -1 ) nxtObsr= (Observation) seq.get(j + 1);
                
                
                //get the label of current obsr
                String curLabelStr = (String)lbInt2Str.get(new Integer(obsr.modelLabel));
                
                //get the label of next obsr if the next observation is available
                String nxtLabelStr = null;
                if (nxtObsr != null){
                    nxtLabelStr = (String)lbInt2Str.get(new Integer(nxtObsr.modelLabel));
                }
                
                //if the label of the current observation is b-w
                //insert one [ before it
                if (curLabelStr.equalsIgnoreCase("b-w"))
                        result += "[";
                
                result += obsr.originalData;
                
                //if the label of the current observation is i-w
                //and the label of the next one is b-w or o 
                //or the current observation is the last one in this sequence
                //insert ] after it
                if (curLabelStr.equalsIgnoreCase("i-w") || curLabelStr.equalsIgnoreCase("b-w")){
                    if (nxtLabelStr == null)
                        result += "]";
                    else if (nxtLabelStr.equalsIgnoreCase("b-w") || nxtLabelStr.equalsIgnoreCase("o")){
                        result += "]";
                    }
                }                
                result += " ";
            } //end of current sequence        
            result += "\n";
        }
        return result.trim();
    }
    
   public void writeLabeledData(Map lbInt2Str, String outputFile) {
        //Writing data in the format <token>|<label>
	if (data == null) {
	    return;
	}
	
	BufferedWriter fout = null;
	
	try { 
	    fout = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), "UTF-8"));
            
            fout.write(getLabledData(lbInt2Str));
            
	    fout.close();
	    
	} catch(IOException e) {
	    System.out.println("Couldn't create file: " + outputFile);
	    return;
	}
    }
   
   public void writeWordMarkedData(Map lbInt2Str, String outputFile){
        //Writing data in the format <token>|<label>
	if (data == null) {
	    return;
	}
	
	BufferedWriter fout = null;
	
	try { 
	    fout = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), "UTF-8"));
            
            fout.write(getWordMarkedData(lbInt2Str));
            
	    fout.close();
	    
	} catch(IOException e) {
	    System.out.println("Couldn't create file: " + outputFile);
	    return;
	}
   }
   
}
