package lexico.model;

public class Token {
	String lexeme;
    TipoToken tipo;
    String codigoAtomico; // Código do átomo
    Integer indiceTabelaSimbolos; // Índice na tabela de símbolos
    int linha; // Linha em que o token foi encontrado

    public Token(String lexeme, TipoToken tipo, String codigoAtomico, Integer indiceTabelaSimbolos, int linha) {
        this.lexeme = lexeme;
        this.tipo = tipo;
        this.codigoAtomico = codigoAtomico;
        this.indiceTabelaSimbolos = indiceTabelaSimbolos;
        this.linha = linha;
    }

    @Override
    public String toString() {
        return String.format("Lexeme: %s, Código: %s, ÍndiceTabSimb: %s, Linha %d", 
                              lexeme, codigoAtomico, 
                              indiceTabelaSimbolos == null ? "-" : indiceTabelaSimbolos, linha);
    }
}
