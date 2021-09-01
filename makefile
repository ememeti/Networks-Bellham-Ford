# this flag generates all debugging information 
JFLAGS = -g

# defines the compiler that is used
JC = javac 

# defines the executable used to run the programs
JVM = java 

# builds .class files from .java files after cleaing default targets to build
.SUFFIXES: .java .class

# gets the target entry we want to create .class files from .java files
.java.class:    
	$(JC) $(JFLAGS) $*.java

# macro that 
CLASSES = \
    routing_GUI.java \
	Nodes.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
