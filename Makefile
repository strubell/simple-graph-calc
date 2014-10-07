# Put your javacup libraries here! (They are likely the same)
JAVACUP := /usr/share/javacup/lib/javacup-runtime.jar
JAVACUP_EXE := /usr/share/javacup/lib/javacup.jar

SOURCE_DIR := src
OUTPUT_DIR := bin
PACKAGE := hw02

# remove directories
RM := rm -r

CLASSPATH := $(OUTPUT_DIR):$(JAVACUP)
JFLAGS := -cp $(CLASSPATH) -d $(OUTPUT_DIR)

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $*.java

MAIN = \
	$(SOURCE_DIR)/$(PACKAGE)/Node.java \
	$(SOURCE_DIR)/$(PACKAGE)/Graph.java \
	$(SOURCE_DIR)/$(PACKAGE)/GraphCalculator.java \
	$(SOURCE_DIR)/$(PACKAGE)/GraphSym.java \
	$(SOURCE_DIR)/$(PACKAGE)/GraphLex.java \
	$(SOURCE_DIR)/$(PACKAGE)/GraphCup.java \
	$(SOURCE_DIR)/$(PACKAGE)/GraphCalc.java

default: cup main
	echo -e '#! /bin/bash\nif [ $$# = 2 ]; then\n\t$(JAVA_HOME)/bin/java -cp bin:$(JAVACUP) $(PACKAGE)/GraphCalc $$1 $$2\nelse\n\t$(JAVA_HOME)/bin/java -cp bin:$(JAVACUP) $(PACKAGE)/GraphCalc\nfi' > GraphCalc
	chmod a+x GraphCalc

main: $(MAIN:.java=.class)

cup:
	jflex -d $(SOURCE_DIR)/$(PACKAGE) $(SOURCE_DIR)/Graph.lex
	$(JAVA_HOME)/bin/java -jar $(JAVACUP_EXE) -parser GraphCup -symbols GraphSym $(SOURCE_DIR)/Graph.cup
	mv GraphCup.java $(SOURCE_DIR)/$(PACKAGE)/.
	mv GraphSym.java $(SOURCE_DIR)/$(PACKAGE)/.
	
clean:
	$(RM) $(OUTPUT_DIR)/* $(SOURCE_DIR)/$(PACKAGE)/GraphCup.java $(SOURCE_DIR)/$(PACKAGE)/GraphLex.java $(SOURCE_DIR)/$(PACKAGE)/GraphSym.java GraphCalc
