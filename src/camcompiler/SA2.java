/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camcompiler;


/**
 *
 * @author Andres
 */
public class SA2 extends SemanticAction{
    //CHEQUEA RANGO DE CTES  -> -2^15 -1<n< 2^15 -1 Y SACA _i
    public SA2(LexicAnalyzer lA) {
        super(lA);
    }

    /**
     *
     */
    @Override
    public void run (){
        ErrorLex e = lA.getError();
        String s = lA.getString();
        int line = lA.getLine();
        //REMOVES _i
        char[] dst = new char[s.length()-2];                
        s.getChars(0, s.length()-2, dst, 0);
        s= new String();
        for (char ch: dst){
            s+=ch;
        }
        int value= Integer.valueOf(s);                
        short maxShort = Short.MAX_VALUE;
        short minShort = Short.MIN_VALUE;
        if ((value >= minShort) &&(value <= 100))
            lA.setString(Integer.toString(value));
        else
        {
            e.addLog("Constant out of range", line);
            lA.setString(new String());
            lA.setCode(0);
            lA.setCurrentState(0);
            lA.setCurrentChar(new String());
        } 
        lA.setCode(258);
    }
}
