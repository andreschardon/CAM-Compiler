package camcompiler;

public class SA8 extends SemanticAction{
    //Accion Semantica 8: SETEA TOKEN DE OPERADOR 
    public SA8(LexicAnalyzer lA){
        super(lA);
    } 

    public void run(){
        String word = lA.getString();
        switch(word) {
            case "+" : { lA.setCode(278); break;}
            case "*" : { lA.setCode(280); break;}
            case "=" : { lA.setCode(282); break;}
            case "==": { lA.setCode(290); break;}
            case "/" : { lA.setCode(281); break;}
            case "," : { lA.setCode(283); break;}
            case ";" : { lA.setCode(276); break;}
            case "-" : { lA.setCode(279); break;}
            case "(" : { lA.setCode(284); break;}
            case ")" : { lA.setCode(285); break;}
            case ">" : { lA.setCode(286); break;}
            case ">=": { lA.setCode(291); break;}
            case "<" : { lA.setCode(287); break;}
            case "<=": { lA.setCode(292); break;}
            case "{" : { lA.setCode(288); break;}
            case "}" : { lA.setCode(289); break;}
        }
    }
}
