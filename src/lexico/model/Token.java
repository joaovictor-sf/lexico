package lexico.model;

public class Token {
	String valor;
    TipoToken tipo;
    int linha;

    public Token(String valor, TipoToken tipo, int linha) {
        this.valor = valor;
        this.tipo = tipo;
        this.linha = linha;
    }

    public String getValor() {
        return valor;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "(" + tipo + ", " + valor + ", Linha: " + linha + ")";
    }
}
