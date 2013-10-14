# Répertoires source et destination
SOURCE_DIR  := src
OUTPUT_DIR  := bin
INSTAL_DIR  := /usr/bin
NAME        := tache

# Outils UNIX
FIND        := /usr/bin/find
MKDIR       := mkdir -p
RM          := rm -rf
MV          := mv
CD          := cd
CHMOD       := chmod +x
SHELL       := /bin/bash

# Outils Java
JAVA_HOME   := /usr/lib/jvm/java-7-openjdk
JAVA        := $(JAVA_HOME)/bin/java
JAVAFLAGS   := -jar
JAVAC       := $(JAVA_HOME)/bin/javac
JFLAGS      := -sourcepath $(SOURCE_DIR)        \
               -d $(OUTPUT_DIR)
JAR         := $(JAVA_HOME)/bin/jar
CLASSDIR    := task
MAINCLASS   := $(CLASSDIR).Main
JARNAME     := $(NAME).jar
JARFLAGS    := -cfe $(JARNAME) $(MAINCLASS)
JAVADOC     := $(JAVA_HOME)/bin/javadoc
JDFLAGS     := -encoding utf8 -docencoding utf8 -charset utf8 \
               -sourcepath $(SOURCE_DIR)                      \
               -d $(OUTPUT_DIR)/javadoc                 

# Classpath Java
class_path := OUTPUT_DIR

# Espace blanc
space := $(empty) $(empty)

# $(call build-classpath, variable-list)
define build-classpath
$(strip                                         \
  $(patsubst :%,%,                              \
    $(subst : ,:,                               \
      $(strip                                   \
        $(foreach j,$1,$(call get-file,$j):)))))
endef

# $(call get-file, variable-name)
define get-file
  $(strip                                       \
    $($1)                                       \
    $(if $(call file-exists-eval,$1),,          \
      $(warning The file referenced by variable \
                '$1' ($($1)) cannot be found)))
endef

# $(call file-exists-eval, variable-name)
define file-exists-eval
  $(strip                                       \
    $(if $($1),,$(warning '$1' has no value))   \
    $(wildcard $($1)))
endef

# $(call file-exists, wildcard-pattern)
file-exists = $(wildcard $1)

# $(call check-file, file-list)
define check-file
  $(foreach f, $1,                              \
    $(if $(call file-exists, $($f)),,           \
      $(warning $f ($($f)) is missing)))
endef

# #(call make-temp-dir, root-opt)
define make-temp-dir
  mktemp -t $(if $1,$1,make).XXXXXXXXXX
endef

# $(call make-jar,jar-variable-prefix)
define make-jar
  .PHONY: $1 $$($1_name)
  $1: $($1_name)
  $$($1_name):
        cd $(OUTPUT_DIR); \
        $(JAR) $(JARFLAGS) $$(notdir $$@) $$($1_packages)
        $$(call add-manifest, $$@, $$($1_name), $$($1_manifest))
endef

# Set the CLASSPATH
export CLASSPATH := $(call build-classpath, $(class_path))

# make-directories - Ensure output directory exists.
make-directories := $(shell $(MKDIR) $(OUTPUT_DIR))

# all - Perform all tasks for a complete build
.PHONY: all
all: compile jar launcher

# all_javas - Temp file for holding source file list
all_javas := $(OUTPUT_DIR)/all.javas

# compile - Compile the source
.PHONY: compile
compile: $(all_javas)
	$(JAVAC) $(JFLAGS) @$<

# all_javas - Gather source file list
.INTERMEDIATE: $(all_javas)
$(all_javas):
	$(FIND) $(SOURCE_DIR) -name '*.java' > $@

# jar - Create jar
.PHONY: jar
jar: $(JARNAME)
$(JARNAME): compile
	cd $(OUTPUT_DIR); \
	$(JAR) $(JARFLAGS) $(CLASSDIR); \
	mv $(JARNAME) ../; \
	cd ../

# Lanceur
.PHONY: launcher
launcher: $(NAME)
$(NAME):
	@echo "#!/bin/bash" > $@
	@echo $(JAVA) $(JAVAFLAGS) $(INSTAL_DIR)/$(JARNAME) \$$\{@\} >> $@
	$(CHMOD) $@

# javadoc - Generate the Java doc from sources
.PHONY: javadoc
javadoc: $(all_javas)
	$(JAVADOC) $(JDFLAGS) @$<

# Nettoyage
.PHONY: clean
clean:
	$(RM) $(OUTPUT_DIR)
	$(RM) $(JARNAME)
	$(RM) $(NAME)

# Installation
.PHONY: install
install:
	cp $(JARNAME) $(NAME) $(INSTAL_DIR)

# Désintallation
.PHONY: uninstall
uninstall:
	$(RM) $(INSTAL_DIR)/$(JARNAME) $(INSTAL_DIR)/$(NAME)

.PHONY: classpath
classpath:
	@echo CLASSPATH='$(CLASSPATH)'

.PHONY: check-config
check-config:
	@echo Checking configuration...
	$(call check-file, $(class_path) JAVA_HOME)

.PHONY: print
print:
	$(foreach v, $(V), $(warning $v = $($v)))
