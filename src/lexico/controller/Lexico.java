package lexico.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexico.model.TipoToken;
import lexico.model.Token;

public class Lexico {
    /*enum TipoToken {
        PALAVRAS_RESERVADAS,
        SIMBOLOS_RESERVADOS,
        IDENTIFICADOR,
        NUMERO,
        OPERADOR,
        SIMBOLO,
        ERRO
    }

    static class Token {
        String valor;
        TipoToken tipo;

        Token(String valor, TipoToken tipo) {
            this.valor = valor;
            this.tipo = tipo;
        }

        @Override
        public String toString() {
            return "(" + tipo + ", " + valor + ")";
        }
    }*/
	

    // Padrão único para todos os tokens
    private static final Pattern padraoTokens = Pattern.compile(
        "\\s*(?<palavrasReservadas>cadeia|caracter|declaracoes|enquanto|false|fimDeclaracoes|fimEnquanto|fimFuncoes|fimFunc|fimPrograma|fimSe|funcoes|imprime|inteiro|logico|pausa|programa|real|retorna|se|senao|tipoFunc|tipoParam|tipoVar|true|vazio)|" +
        "(?<identificador>[a-zA-Z_][a-zA-Z0-9_]*)" +
        "|(?<numero>\\d+)|" +
        "(?<simbolosReservados>%|\\(|\\)|,|:|:=|;|\\?|\\[|\\]|\\{|\\}|\\-|\\+|\\*|\\/|!=|#|<|<=|==|>|>=|\\d+)"
    );

    private Set<String> tabelaSimbolos = new HashSet<>();

    public List<Token> analisar(File arquivo) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;

        while ((linha = reader.readLine()) != null) {
            Matcher matcher = padraoTokens.matcher(linha);
            while (matcher.find()) {
                if (matcher.group("palavrasReservadas") != null) {
                    tokens.add(new Token(matcher.group("palavrasReservadas"), TipoToken.PALAVRAS_RESERVADAS));
                } else if (matcher.group("identificador") != null) {
                    String valor = matcher.group("identificador");
                    tokens.add(new Token(valor, TipoToken.IDENTIFICADOR));
                    tabelaSimbolos.add(valor);
                } else if (matcher.group("numero") != null) {
                    tokens.add(new Token(matcher.group("numero"), TipoToken.NUMERO));
                } else if (matcher.group("simbolosReservados") != null) {
                	tokens.add(new Token(matcher.group("simbolosReservados"), TipoToken.SIMBOLOS_RESERVADOS));
                }
            }
        }

        reader.close();
        return tokens;
    }

    public void exibirTabelaSimbolos() {
        System.out.println("Tabela de Símbolos:");
        for (String simbolo : tabelaSimbolos) {
            System.out.println(simbolo);
        }
    }

    public void exibirTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public void escreverTokensEmArquivo(List<Token> tokens, String nomeArquivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo ));
        writer.write("Tokens:\n");
        for (Token token : tokens) {
            writer.write(token + "\n");
        }
        writer.write("\n");

        writer.write("Tabela de Símbolos:\n");
        for (String simbolo : tabelaSimbolos) {
            writer.write(simbolo + "\n");
        }

        writer.close();
    }
}
        