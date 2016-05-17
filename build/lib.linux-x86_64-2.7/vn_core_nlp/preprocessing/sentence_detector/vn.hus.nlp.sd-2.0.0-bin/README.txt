
I. GENERAL INFORMATION

	vnSentDetector 2.0 is a tool for automatic detecting sentences of Vietnamese texts. 
	The tool is developed in the Java programming language. The binary 
	release of the tool is distributed as a ready-to-run jar file.
	
II. REQUIREMENT
 
	You need a JRE version 6.0 or above installed on your system. You may 
	download a JRE from the Java website of Sun Microsystems (http://java.sun.com/).
	
	
III. HOW TO RUN

	- Under Unix/Linux, use the provided script "vnSentDetector.sh" 
		to run the program.
	
	- Under Microsoft Windows, use the provided script "vnSentDetector.bat" 
		to run the program.
	
	- This program is a core sentence detector of Vietnamese texts, it has no graphical
		 user interface (GUI). You should provide two arguments for the program:
		   - a text file to be tokenized following -i option (a UTF-8 encoding file)
		   - a text file containing results of the program following -o option.
		   
		 For example:
		  
		      vnSentDetector.sh -i samples/test0.txt  -o samples/test0.sd.txt
		 

IV. HOW TO USE THE API

	- You can integrate vnSentDetector to your application. 
		
	-  The main entry class for a client to use is vn.hus.nlp.sd.SentenceDetector. This class provides several 
		client methods: 
		
	1) public String[] detectSentences(Reader reader) throws IOException
		
		Detects sentences of a (text) reader, returns an array of detected sentences. 
		
		
	2)  public String[] detectSentences(String inputFile) throws IOException
	
		Detects sentences of a text file, returns an array of detected sentences.
		
	3) public void detectSentences(String inputFile, String outputFile)
	
		Detects sentences of a text file, write detected sentences to an ouput file.		
    			
V. LICENSE
  
  		See the LICENSE file.
  		
===========================================
(C) Le Hong Phuong, phuonglh@gmail.com
Vietnam National University, Hanoi, Vietnam
  