/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camcompiler;

/**
 *
 * @author Mariano
 */
public class SA10 extends SemanticAction {
    public SA10(LexicAnalyzer lA) {
        super(lA);
    } 

    /**
     * setea string
     */
    @Override
    public void run(){        
        String newValue = lA.getString();
        newValue = newValue.substring(0, newValue.length()-1);
        lA.setString(newValue); 
        lA.setCode(277);
    }
}