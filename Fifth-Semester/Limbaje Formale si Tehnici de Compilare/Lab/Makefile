all: translator

parser_lab6.tab.c parser_lab6.tab.h: parser_lab6.y
	bison -d -o parser_lab6.tab.c parser_lab6.y

lex.yy.c: scanner_lab6.l parser_lab6.tab.h
	flex -o lex.yy.c scanner_lab6.l

translator: parser_lab6.tab.c lex.yy.c
	g++ -O2 -o translator parser_lab6.tab.c lex.yy.c

# Generate assembly from a source file:
# make asm SRC=prog1 OUT=out.s
asm: translator
	./translator $(SRC) > $(OUT)

# Build executable from generated assembly (Linux x86-64, gcc links libc for scanf/printf)
# make run OUT=out.s EXE=out
run:
	gcc -no-pie -o $(EXE) $(OUT)
	./$(EXE)

clean:
	rm -f translator parser_lab6.tab.c parser_lab6.tab.h lex.yy.c out.s out
