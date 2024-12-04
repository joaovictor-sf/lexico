package lexico.model;

public class Token {
	String valor;
    TipoToken tipo;

    public Token(String valor, TipoToken tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "(" + tipo + ", " + valor + ")";
    }
}
