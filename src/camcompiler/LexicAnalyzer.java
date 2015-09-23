/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camcompiler;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Andres
 */


public class LexicAnalyzer {
    
    private Logger errors = new ErrorLex();
    private Logger warnings = new Warning();
    private Reader reader;
    private int currentLine = 0;
    private String currentString;
    private int currentCode;
    private String currentChar;
    private SymbolsTable st;
    private int currentState;
    
    private final int[][] next_state = {
                {2,2,2,2,3,1,-1,-1,-1,10,7,6,6,0,12,-1,-1,8,-1,-1,0,0,0,0,-1},
                {2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1},
                {2,2,2,2,2,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {0,0,0,0,3,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,-1},
                {0,0,5,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1},
                {0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,-1,8,8,8,8,9,8,-1},
                {0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,11,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,0,11,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,12,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}    
    };
    
    SemanticAction Sa0 = new SA0(this);
    SemanticAction Sa1 = new SA1(this);
    SemanticAction Sa2 = new SA2(this);
    SemanticAction Sa3 = new SA3(this);
    SemanticAction Sa4 = new SA4(this);
    SemanticAction Sa5 = new SA5(this);
    SemanticAction Sa6 = new SA6(this); //line++
    SemanticAction Sa7 = new SA7(this);
    SemanticAction Sa8 = new SA8(this);
    SemanticAction Sa9 = new SA9(this);
    SemanticAction Sa10 = new SA10(this);
    SemanticAction Sa11 = new SA11(this);
    SemanticAction Sa12 = new SA12(this);
    SemanticAction Sa13 = new SA13(this);
    
    
        
        private SemanticAction[][] sem_action = {
            //  "0    1     2    3    4   5     6    7    8    9   10   11      12    13  14   15   16   17    18  19   20   21   22   23  24"
         /*0*/{Sa12,Sa12,Sa12,Sa12,Sa12,Sa12, Sa8, Sa8, Sa8,Sa12, Sa8, Sa8,    Sa8, Sa7,Sa12, Sa8, Sa8,Sa10, Sa8, Sa8, Sa7,Sa13, Sa6,Sa13,Sa12},
         /*1*/{Sa12,Sa12,Sa12,Sa12, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7,    Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7},
         /*2*/{Sa12,Sa12,Sa12,Sa12,Sa12,Sa12, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1,    Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1, Sa1},
         /*3*/{ Sa7, Sa7, Sa7, Sa7,Sa12, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7,    Sa7,Sa12, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7},
         /*4*/{ Sa7, Sa7,Sa12, Sa2, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7,    Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7},
         /*5*/{ Sa7, Sa3, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7,    Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7},
         /*6*/{ Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4,   Sa12, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4},
         /*7*/{ Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4,Sa12,   Sa12, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4, Sa4},    
         /*8*/{Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,   Sa12,Sa12,Sa12,Sa12,Sa12,Sa10,Sa12,Sa12,Sa12,Sa12, Sa6,Sa12, Sa7},
         /*9*/{ Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7,Sa10, Sa7, Sa7,    Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7, Sa7},
        /*10*/{Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa12,Sa11,Sa11,   Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11,Sa11},
        /*11*/{Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,   Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12,Sa12, Sa9,Sa12,Sa12},
        /*12*/{ Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5,    Sa5, Sa5,Sa12, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5, Sa5}};;
    
    public LexicAnalyzer(String fileName, SymbolsTable st) throws IOException {
        this.reader = new Reader(fileName);
        this.st = st;
        
    }
    
    private int getColumn() {
        /*if (this.currentChar.equals("   "))
            return 21;
        else if (this.currentChar.equals("/n"))
            return 22;
        else {*/ 
        switch (currentChar) {
            case "a":  return 0; case "b":  return 0; case "c":  return 0; case "d":  return 0;
            case "e":  return 0; case "f":  return 0; case "g":  return 0; case "h":  return 0;
            case "i":  return 3; case "j":  return 0; case "k":  return 0; case "l":  return 1;
            case "m":  return 0; case "n":  return 0; case "o":  return 0; case "p":  return 0;
            case "q":  return 0; case "r":  return 0; case "s":  return 0; case "t":  return 0;
            case "u":  return 2; case "v":  return 0; case "w":  return 0; case "x":  return 0;
            case "y":  return 0; case "z":  return 0;
                
            case "A": return 14; case "B": return 14; case "C": return 14; case "D": return 14;
            case "E": return 14; case "F": return 14; case "G": return 14; case "H": return 14;
            case "I": return 14; case "J": return 14; case "K": return 14; case "L": return 14;
            case "M": return 14; case "N": return 14; case "O": return 14; case "P": return 14;
            case "Q": return 14; case "R": return 14; case "S": return 14; case "T": return 14;
            case "U": return 14; case "V": return 14; case "W": return 14; case "X": return 14;
            case "Y": return 14; case "Z": return 14; 
            
            case "0": return 4;  case "1": return 4;  case "2": return 4;  case "3": return 4;
            case "4": return 4;  case "5": return 4;  case "6": return 4;  case "7": return 4;
            case "8": return 4;  case "9": return 4;  
                
            case "@": return 5;  case "+": return 6;  case "/": return 7;  case "*": return 8;
            case "-": return 9;  case "<": return 10; case ">": return 11; case "=": return 12;
            case "_": return 13; case "(": return 15; case ")": return 16; case "\"":return 17;
            case ",": return 18; case ";": return 19; case "\t": return 21; case "/n": return 22;
            case " ": return 23; case "$": return 24; 
                
            default: return 20;
        }
        //}
    }
    
    public int getToken() throws IOException {
        this.currentString = new String();        
        currentState = 0;
        this.currentCode = 0;
        while (currentState != -1) {
            this.currentChar = reader.getChar();
            int column = this.getColumn();
            Sa0.run();
            sem_action[currentState][column].run();
            column = this.getColumn();
            currentState = next_state[currentState][column];
        }
        Token t = new Token(this.currentCode, this.currentString);
        int me = st.request(t);
        return me;
    }

    public ErrorLex getError() {
        return (ErrorLex)this.errors;
    }
    public Warning getWarning() {
        return (Warning)this.warnings;
    }
    public int getLine() {
        return this.currentLine;
    }
    
    public void increaseLines() {
        this.currentLine++;
    }
    
    public Reader getReader() {
        return this.reader;
    }
    public String getChar() {
        return this.currentChar;
    }    
    
    public String getString() {
        return this.currentString;
    }
    public void setString (String s) {
        this.currentString = s;
    }

    public void setCode(int i) {
        this.currentCode = i;
    }
    
    public void setCurrentState(int i) {
        this.currentState = i;
    }
    
    /**
     *
     * @return
     */
    public SymbolsTable getST(){return st;}

    void setCurrentChar(String string) {
        this.currentChar = string;
    }
}
       

