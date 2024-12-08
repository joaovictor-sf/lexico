package lexico.model;

public enum TipoToken {
    PALAVRAS_RESERVADAS("Palavra Reservada"),
    SIMBOLOS_RESERVADOS("Símbolo Reservado"),
    IDENTIFICADOR("Identificador"),
    NUMERO("Número"),
    OPERADOR("Operador"),
    SIMBOLO("Símbolo"),
    ERRO("Erro"),
    STRING("String");

    private final String value;

    private TipoToken(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

