package lexico.main;

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

public class Lexico {
    enum TipoToken {
        PALAVRA_CHAVE,
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
    }

    // Padrão único para todos os tokens
    private static final Pattern padraoTokens = Pattern.compile(
        "\\s*(?<palavraChave>if|else|while|return)|" +
        "(?<identificador>[a-zA-Z_][a-zA-Z0-9_]*)" +
        "|(?<numero>\\d+)|" +
        "(?<operador>==|<=|>=|!=|\\+|\\-|\\*|\\/|=|<|>)|" +
        "(?<simbolo>[(){};,])"
    );

    private Set<String> tabelaSimbolos = new HashSet<>();

    public List<Token> analisar(File arquivo) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;

        while ((linha = reader.readLine()) != null) {
            Matcher matcher = padraoTokens.matcher(linha);
            while (matcher.find()) {
                if (matcher.group("palavraChave") != null) {
                    tokens.add(new Token(matcher.group("palavraChave"), TipoToken.PALAVRA_CHAVE));
                } else if (matcher.group("identificador") != null) {
                    String valor = matcher.group("identificador");
                    tokens.add(new Token(valor, TipoToken.IDENTIFICADOR));
                    tabelaSimbolos.add(valor);
                } else if (matcher.group("numero") != null) {
                    tokens.add(new Token(matcher.group("numero"), TipoToken.NUMERO));
                } else if (matcher.group("operador") != null) {
                    tokens.add(new Token(matcher.group("operador"), TipoToken.OPERADOR));
                } else if (matcher.group("simbolo") != null) {
                    tokens.add(new Token(matcher.group("simbolo"), TipoToken.SIMBOLO));
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
        