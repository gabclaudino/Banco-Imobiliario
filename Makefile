# Diretório do código-fonte
SRC_DIR = src
BIN_DIR = $(SRC_DIR)

# Entrypoint do programa
MAIN_CLASS = controle.Main

# Descobre todos os arquivos Java no diretório de origem
JAVA_FILES = $(shell find $(SRC_DIR) -name "*.java")

# Nome do executável (usaremos o .class principal como entrada)
EXECUTABLE = $(BIN_DIR)/$(MAIN_CLASS).class

# Regra padrão (compila e executa)
.PHONY: all
all: compile run

# Compila os arquivos .java
.PHONY: compile
compile:
	@echo "Compilando os arquivos..."
	javac -d $(BIN_DIR) $(JAVA_FILES)

# Executa o programa
.PHONY: run
run:
	@echo "Executando o programa..."
	java -cp $(BIN_DIR) $(MAIN_CLASS)

# Limpa os arquivos compilados
.PHONY: clean
clean:
	@echo "Limpando arquivos compilados..."
	find $(BIN_DIR) -name "*.class" -delete
