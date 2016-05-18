JAVAC=	javac
JAVA=	java

HOME_DIR=	../dist
SRC_DIR=	JVnSegmenter
MODEL_DIR=	../models
SAMPLES_DIR=	../samples

JAR_WORDSEGMENTER=	JVnSegmenter.jar

CLASSPATH=	.

all:
	$(JAVAC) -cp $(CLASSPATH) $(SRC_DIR)/JVnSegmenter.java
	jar -cf $(HOME_DIR)/$(JAR_WORDSEGMENTER) $(SRC_DIR)/*.class

test:
	$(JAVA) -mx512M -cp $(HOME_DIR)/$(JAR_WORDSEGMENTER) JVnSegmenter.JVnSegmenter -modeldir $(MODEL_DIR) -inputfile $(SAMPLES_DIR)/input.txt
	$(JAVA) -mx512M -cp $(HOME_DIR)/$(JAR_WORDSEGMENTER) JVnSegmenter.JVnSegmenter -modeldir $(MODEL_DIR) -inputdir $(SAMPLES_DIR)/inputdir 
	
clean:
	rm $(JAR_WORDSEGMENTER)
	rm $(SRC_DIR)/*.class
