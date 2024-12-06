package lexico.model;

public class Token {
	private String lexeme;
	private TipoToken tipo;
	private String codigoAtomico; // Código do átomo
	private Integer indiceTabelaSimbolos; // Índice na tabela de símbolos
	private int linha; // Linha em que o token foi encontrado

    public Token(String lexeme, TipoToken tipo, String codigoAtomico, Integer indiceTabelaSimbolos, int linha) {
        this.lexeme = lexeme;
        this.tipo = tipo;
        this.codigoAtomico = codigoAtomico;
        this.indiceTabelaSimbolos = indiceTabelaSimbolos;
        this.linha = linha;
    }

    public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public TipoToken getTipo() {
		return tipo;
	}

	public void setTipo(TipoToken tipo) {
		this.tipo = tipo;
	}

	public String getCodigoAtomico() {
		return codigoAtomico;
	}

	public void setCodigoAtomico(String codigoAtomico) {
		this.codigoAtomico = codigoAtomico;
	}

	public Integer getIndiceTabelaSimbolos() {
		return indiceTabelaSimbolos;
	}

	public void setIndiceTabelaSimbolos(Integer indiceTabelaSimbolos) {
		this.indiceTabelaSimbolos = indiceTabelaSimbolos;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	@Override
    public String toString() {
        return String.format("Lexeme: %s, Código: %s, ÍndiceTabSimb: %s, Linha %d", 
                              lexeme, codigoAtomico, 
                              indiceTabelaSimbolos == null ? "-" : indiceTabelaSimbolos, linha);
    }
}
