all: java test

NAVIDOCCLASSDIR=../navidoc/CLASSES
STORMCLASSDIR=../storm/CLASSES

DEPENDS=../depends

CLASSPATH=$(CLASSDIR):$(NAVIDOCCLASSDIR):$(STORMCLASSDIR):$(DEPENDS)/jython.jar:$(DEPENDS)/collections-kaffe.jar:$(DEPENDS)/xerces.jar:$(DEPENDS)/xmlParserAPIs-2.4.0.jar:$(DEPENDS)/cryptix-jce-provider.jar:$(DEPENDS)/gisp.jar:$(DEPENDS)/dom4j.jar:$(DEPENDS)/log4j.jar:$(shell echo $$CLASSPATH)
export CLASSPATH

RAWSRC = `find org/ -name "*.java"` 
CLASSDIR=CLASSES/
JAVAC?=javac
JAVA?=java

TEST=.


ifeq (,$(JYTHONPATH))
 JYTHONPATH=.:$(DEPENDS)/jythonlib.jar:$(DEPENDS)/pythonlib.jar
endif

ifeq (,$(JYTHON))
# python.verbose can be: "error", "warning", "message", "comment", "debug"
 JYTHON=$(JAVA) $(JVMFLAGS) -Dpython.cachedir=. -Dpython.path=$(JYTHONPATH) -Dpython.verbose=message $(EDITOR_OPTION) org.python.util.jython
endif

clean:
	@echo "Removing everything found in .cvsignores"
	find . -name ".cvsignore"|while read ign; do (cd `dirname $$ign` && cat .cvsignore|while read files; do rm -Rf $$files; done); done
	find . -name "*.pyc" | xargs rm -f
	find . -name "*.class" | xargs rm -f

.PHONY: test

java:
	mkdir -p CLASSES
	$(JAVAC) $(DEBUG) -d $(CLASSDIR) $(RAWSRC) 

test:
	$(JYTHON) test.py $(TEST)

runjython:
	$(JYTHON) 

tags::
	ctags -R

copyrighted::
	python ../fenfire/metacode/copyrighter.py Alph

##########################################################################
# General documentation targets
docs:   java-doc navidoc navilink

DOCPKGS= -subpackages org
#DOCPKGS= org.nongnu.alph\
#	 org.nongnu.alph.util\
#	 org.nongnu.alph.xml\
#	 org.nongnu.alph.impl

JAVADOCOPTS=-use -version -author -windowtitle "Alph Java API"
java-doc:
	find . -name '*.class' | xargs rm -f # Don't let javadoc see these
	rm -Rf doc/javadoc
	mkdir -p doc/javadoc
	javadoc $(JAVADOCOPTS) -d doc/javadoc -sourcepath . $(DOCPKGS)
##########################################################################
# Navidoc documentation targets
navidoc: # Compiles reST into HTML
	make -C "../navidoc/" html DBG="$(DBG)" RST="../alph/doc/"

navilink: # Bi-directional linking using imagemaps
	make -C "../navidoc/" imagemap HTML="../alph/doc/"

naviloop: # Compiles, links, loops
	make -C "../navidoc/" html-loop DBG="--imagemap $(DBG)" RST="../alph/$(RST)"

peg: # Creates a new PEG, uses python for quick use
	make -C "../navidoc/" new-peg PEGDIR="../alph/doc/pegboard"

pegs:   # Compiles only pegboard
	make -C "../navidoc/" html DBG="$(DBG)" RST="../alph/doc/pegboard/"

html: # Compiles reST into HTML, directories are processed recursively
	make -C "../navidoc/" html DBG="$(DBG)" RST="../alph/$(RST)"

html-loop: # Loop version for quick recompiling
	make -C "../navidoc/" html-loop DBG="$(DBG)" RST="../alph/$(RST)"

latex: # Compiles reST into LaTeX, directories are processed recursively
	make -C "../navidoc/" latex DBG="$(DBG)" RST="../alph/$(RST)"

latex-loop: # Loop version for quick recompiling
	make -C "../navidoc/" latex-loop DBG="$(DBG)" RST="../alph/$(RST)"
