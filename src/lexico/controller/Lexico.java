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
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexico.model.TipoToken;
import lexico.model.Token;

public class Lexico {

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
        int numeroLinha = 0;

        while ((linha = reader.readLine()) != null) {
            numeroLinha++;
            Matcher matcher = padraoTokens.matcher(linha);

            while (matcher.find()) {
                String codigoAtomico = null;
                Integer indiceTabelaSimbolos = null;

                if (matcher.group("palavrasReservadas") != null) {
                    String lexeme = matcher.group("palavrasReservadas");
                    codigoAtomico = getCodigoAtomico(lexeme);
                    tokens.add(new Token(lexeme, TipoToken.PALAVRAS_RESERVADAS, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("identificador") != null) {
                    String lexeme = truncarLexeme(matcher.group("identificador"));
                    codigoAtomico = "C07";
                    if (!tabelaSimbolos.contains(lexeme)) {
                        tabelaSimbolos.add(lexeme);
                    }
                    indiceTabelaSimbolos = new ArrayList<>(tabelaSimbolos).indexOf(lexeme) + 1;
                    tokens.add(new Token(lexeme, TipoToken.IDENTIFICADOR, codigoAtomico, indiceTabelaSimbolos, numeroLinha));
                } else if (matcher.group("numero") != null) {
                    String lexeme = matcher.group("numero");
                    codigoAtomico = lexeme.contains(".") ? "C04" : "C03";
                    tokens.add(new Token(lexeme, TipoToken.NUMERO, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("simbolosReservados") != null) {
                    String lexeme = matcher.group("simbolosReservados");
                    codigoAtomico = getCodigoAtomico(lexeme);
                    tokens.add(new Token(lexeme, TipoToken.SIMBOLOS_RESERVADOS, codigoAtomico, null, numeroLinha));
                } else {
                    String lexeme = matcher.group();
                    tokens.add(new Token(lexeme, TipoToken.ERRO, "ERROR", null, numeroLinha));
                }
            }
        }
        
     // Exibe os tokens diretamente no console
        /*System.out.println("RELATÓRIO DA ANÁLISE LÉXICA:");
        for (Token token : tokens) {
            System.out.println(token);
        }*/

        reader.close();
        return tokens;
    }
    
    private String truncarLexeme(String lexeme) {
        return lexeme.length() > 30 ? lexeme.substring(0, 30) : lexeme;
    }
        
    private String getCodigoAtomico(String lexeme) {
        Map<String, String> codigosAtomicos = Map.ofEntries(
            Map.entry("cadeia", "A01"),
            Map.entry("caracter", "A02"),
            Map.entry("declaracoes", "A03"),
            Map.entry("enquanto", "A04"),
            Map.entry("false", "A05"),
            Map.entry("fimDeclaracoes", "A06"),
            Map.entry("fimEnquanto", "A07"),
            Map.entry("fimFunc", "A08"),
            Map.entry("fimFuncoes", "A09"),
            Map.entry("fimPrograma", "A10"),
            Map.entry("fimSe", "A11"),
            Map.entry("funcoes", "A12"),
            Map.entry("imprime", "A13"),
            Map.entry("inteiro", "A14"),
            Map.entry("logico", "A15"),
            Map.entry("pausa", "A16"),
            Map.entry("programa", "A17"),
            Map.entry("real", "A18"),
            Map.entry("retorna", "A19"),
            Map.entry("se", "A20"),
            Map.entry("senao", "A21"),
            Map.entry("tipoFunc", "A22"),
            Map.entry("tipoParam", "A23"),
            Map.entry("tipoVar", "A24"),
            Map.entry("true", "A25"),
            Map.entry("vazio", "A26"),
            Map.entry("%", "B01"),
            Map.entry("(", "B02"),
            Map.entry(")", "B03"),
            Map.entry(",", "B04"),
            Map.entry(":", "B05"),
            Map.entry(":=", "B06"),
            Map.entry(";", "B07"),
            Map.entry("?", "B08"),
            Map.entry("[", "B09"),
            Map.entry("]", "B10"),
            Map.entry("{", "B11"),
            Map.entry("}", "B12"),
            Map.entry("-", "B13"),
            Map.entry("*", "B14"),
            Map.entry("/", "B15"),
            Map.entry("+", "B16"),
            Map.entry("!=", "B17"),
            Map.entry("#", "B18"),
            Map.entry("<", "B19"),
            Map.entry("<=", "B20"),
            Map.entry("==", "B21"),
            Map.entry(">", "B22"),
            Map.entry(">=", "B22"),
            Map.entry("consCadeia", "C01"),
            Map.entry("consCaracter", "C02"),
            Map.entry("consInteiro", "C03"),
            Map.entry("consReal", "C04"),
            Map.entry("nomFuncao", "C05"),
            Map.entry("nomPrograma", "C06"),
            Map.entry("variavel", "C07")
        );
        return codigosAtomicos.getOrDefault(lexeme, "UNKNOWN");
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
        