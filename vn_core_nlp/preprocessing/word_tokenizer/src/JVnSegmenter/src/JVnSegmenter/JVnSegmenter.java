/*
    Copyright (C) 2007 by Cam-Tu Nguyen and Xuan-Hieu Phan
 
    Email:	ncamtu@gmail.com
 
    Department of Information System,
    College of Technology
    Hanoi National University, Vietnam
 */
package JVnSegmenter;

import java.io.*;

public class JVnSegmenter {
    
    private String modelDir = "";
    private Maps taggerMaps = null;
    private Dictionary taggerDict = null;
    private FeatureGen taggerFGen = null;
    private Viterbi taggerVtb = null;
    private Model taggerModel = null;
    
    public boolean init(String modelDir){
        Option taggerOpt = new Option(modelDir);
        if (!taggerOpt.readOptions()) {
            return false;
        }
        
        taggerMaps = new Maps();
        taggerDict = new Dictionary();
        taggerFGen = new FeatureGen(taggerMaps, taggerDict);
        taggerVtb = new Viterbi();
        
        taggerModel = new Model(taggerOpt, taggerMaps, taggerDict, taggerFGen, taggerVtb);
        if (!taggerModel.init()) {
            System.out.println("Couldn't load the model");
            System.out.println("Check the <model directory> and the <model file> again");
            return false;
        }
        return true;
    }
    
    //do word segmentation and return string with tokens labeled
    //this is useful for further processing 
    public String wordSegment(String text){
        TaggingInputData taggerData = new TaggingInputData();
        if (!taggerData.init(modelDir)) return null;
        
        
        taggerData.readOriginalDataFromString(text);
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        // inference
        taggerModel.inferenceAll(taggerData.data);
        return taggerData.getLabledData(taggerMaps.lbInt2Str);
    }
    
    public String wordSegment(TaggingInputData taggerData){
        //generate context predicates
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        //inference
        taggerModel.inferenceAll(taggerData.data);
        return taggerData.getLabledData(taggerMaps.lbInt2Str);
    }    
    
    public void wordSegment(TaggingInputData taggerData, String outputFile){
        //generate context predicates
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        //inference
        taggerModel.inferenceAll(taggerData.data);
        taggerData.writeLabeledData(taggerMaps.lbInt2Str, outputFile);
    }
    
    //do the word segmentation and return string with word boundaries marked
    //this is useful for visual representation
    public String wordBoundaryMark(String text){
        TaggingInputData taggerData = new TaggingInputData();
        if (!taggerData.init(modelDir)) return null;
        
        
        taggerData.readOriginalDataFromString(text);
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        // inference
        taggerModel.inferenceAll(taggerData.data);
        return taggerData.getWordMarkedData(taggerMaps.lbInt2Str);
    }
    
    public String wordBoundaryMark(TaggingInputData taggerData){
        //generate context predicates
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        // inference
        taggerModel.inferenceAll(taggerData.data);
        return taggerData.getWordMarkedData(taggerMaps.lbInt2Str);
    }
    
    public void wordBoundaryMark(TaggingInputData taggerData, String outputFile){
        //generate context predicates
        taggerData.cpGen(taggerMaps.cpStr2Int);
        
        //inference
        taggerModel.inferenceAll(taggerData.data);
        taggerData.writeWordMarkedData(taggerMaps.lbInt2Str, outputFile);
        //taggerData.writeLabeledData(taggerMaps.lbInt2Str, outputFile);
    }
    
/*main method for using this tool from command line
 */        
    public static void main(String[] args){
        displayCopyright();
        
        if (!checkArgs(args)) {
            displayHelp();
            return;
        }
        
        String modelDir = args[1];
        boolean isInputFile = true;        
        if (args[2].compareToIgnoreCase("-inputfile") != 0) {
            isInputFile = false;
        }
        String inputFile = "";
        String inputDir = "";
        if (isInputFile) {
            inputFile = args[3];
        } else {
            inputDir = args[3];
        }
        
        Option taggerOpt = new Option(modelDir);
        if (!taggerOpt.readOptions()) {
            return;
        }
        
        JVnSegmenter ws = new JVnSegmenter();
        if (!ws.init(modelDir))
            return;
        
        TaggingInputData taggerData = new TaggingInputData();
        if (!taggerData.init(modelDir)) return;
          
        if (isInputFile) {
            taggerData.readOriginalDataFromFile(inputFile);
            ws.wordBoundaryMark(taggerData, inputFile + ".wseg");
        }
        
        if (!isInputFile) {
            if (inputDir.endsWith(File.separator)) {
                inputDir = inputDir.substring(0, inputDir.length() - 1);
            }
            
            File dir = new File(inputDir);
            String[] children = dir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.endsWith(".wseg");
                }
            });            
            
            for (int i = 0; i < children.length; i++) {
                String filename = inputDir + File.separator + children[i];
                if ((new File(filename)).isDirectory()) {
                    continue;
                }
                
                taggerData.readOriginalDataFromFile(filename);
                ws.wordBoundaryMark(taggerData, filename + ".wseg");
            }
        }
        
    } // end of the main method
    
    public static boolean checkArgs(String[] args) {
        // case 1: CRFChunker -modeldir <model directory> -inputfile <input data file>
        // case 2: CRFChunker -modeldir <model directory> -inputdir <input data directory>
        
        if (args.length < 4) {
            return false;
        }
        
        if (args[0].compareToIgnoreCase("-modeldir") != 0) {
            return false;
        }
        
        if (!(args[2].compareToIgnoreCase("-inputfile") == 0 ||
                args[2].compareToIgnoreCase("-inputdir") == 0)) {
            return false;
        }
        
        return true;
    }
    
    public static void displayCopyright() {
        System.out.println("Vietnamese Word Segmentation:");
        System.out.println("\tusing first-order Markov Conditional Random Fields");
        System.out.println("\ttesting on about 8000 sentences with the highest F1-measure of 94.09%");
        System.out.println("Copyright (C) by Cam-Tu Nguyen {1} and Xuan-Hieu Phan {2}");
        System.out.println("{1}: College of Technology, Hanoi National University");
        System.out.println("{2}: Graduate School of Information Sciences, Tohoku University");
        System.out.println("Email: {ncamtu@gmail.com ; hieuxuan@ecei.tohoku.ac.jp}");
        System.out.println();
    }
    
    public static void displayHelp() {
        System.out.println("Usage:");
        System.out.println("\tCase 1: JVnSegmenter -modeldir <model directory> -inputfile <input data file>");
        System.out.println("\tCase 2: JVnSegmenter -modeldir <model directory> -inputdir <input data directory>");
        System.out.println("Where:");
        System.out.println("\t<model directory> is the directory contain the model and option files");
        System.out.println("\t<input data file> is the file containing input sentences that need to");
        System.out.println("\tbe tagged (each sentence on a line)");
        System.out.println("\t<input data directory> is the directory containing multiple input data files");
        System.out.println();
    }
}
