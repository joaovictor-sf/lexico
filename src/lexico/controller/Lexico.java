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
        "|(?<numero>(\\d+(\\.\\d+)?([eE][+-]?\\d+)?))|" +
        "(?<simbolosReservados>%|\\(|\\)|,|:|:=|;|\\?|\\[|\\]|\\{|\\}|\\-|\\+|\\*|\\/|!=|#|<|<=|==|>|>=|\\d+)"
    );

    private Set<String> tabelaSimbolos = new HashSet<>();

    public List<Token> analisar(File arquivo) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;
        int numeroLinha = 0;
        boolean[] dentroComentarioBloco = {false}; // Usar array para alterar dentro do método
        String contextoAnterior = ""; // Rastreia o contexto para determinar o tipo do identificador

        while ((linha = reader.readLine()) != null) {
            numeroLinha++;
            
            //linha = removerComentarios(linha); // Remove comentários
            linha = removerComentarios(linha, dentroComentarioBloco); // Remove comentários
            if (linha.isEmpty()) continue; // Ignora linhas vazias após a remoção
            
            Matcher matcher = padraoTokens.matcher(linha);

            while (matcher.find()) {
                String codigoAtomico = null;
                Integer indiceTabelaSimbolos = null;

                if (matcher.group("palavrasReservadas") != null) {
                    String lexeme = matcher.group("palavrasReservadas");
                    contextoAnterior = lexeme; // Atualiza o contexto
                    codigoAtomico = getCodigoAtomico(lexeme);
                    tokens.add(new Token(lexeme, TipoToken.PALAVRAS_RESERVADAS, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("identificador") != null) {
                    String lexeme = truncarLexeme(matcher.group("identificador"));
                    codigoAtomico = identificarCodigoAtomico(lexeme, contextoAnterior, tokens);

                    if (!tabelaSimbolos.contains(lexeme)) {
                        tabelaSimbolos.add(lexeme);
                    }
                    indiceTabelaSimbolos = new ArrayList<>(tabelaSimbolos).indexOf(lexeme) + 1;
                    tokens.add(new Token(lexeme, TipoToken.IDENTIFICADOR, codigoAtomico, indiceTabelaSimbolos, numeroLinha));
                    /*codigoAtomico = "C07";
                    if (!tabelaSimbolos.contains(lexeme)) {
                        tabelaSimbolos.add(lexeme);
                    }
                    indiceTabelaSimbolos = new ArrayList<>(tabelaSimbolos).indexOf(lexeme) + 1;
                    tokens.add(new Token(lexeme, TipoToken.IDENTIFICADOR, codigoAtomico, indiceTabelaSimbolos, numeroLinha));*/
                } else if (matcher.group("numero") != null) {
                    String lexeme = matcher.group("numero");
                    
                    if (lexeme.contains("e") || lexeme.contains("E")) {
                        codigoAtomico = "C04"; // Número em notação exponencial é considerado consReal
                    } else if (lexeme.contains(".")) {
                        codigoAtomico = "C04"; // Números decimais
                    } else {
                        codigoAtomico = "C03"; // Números inteiros
                    }
                    
                    //codigoAtomico = lexeme.contains(".") ? "C04" : "C03";
                    
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

        reader.close();
        return tokens;
    }
    
    private String truncarLexeme(String lexeme) {
        return lexeme.length() > 30 ? lexeme.substring(0, 30) : lexeme;
    }
    
    private String removerComentarios(String linha, boolean[] dentroComentarioBloco) {
        // Se já estiver dentro de um comentário de bloco
        if (dentroComentarioBloco[0]) {
            if (linha.contains("*/")) {
                // Finaliza o comentário de bloco e processa o restante da linha
                linha = linha.substring(linha.indexOf("*/") + 2);
                dentroComentarioBloco[0] = false; // Finaliza o estado de comentário de bloco
            } else {
                return ""; // Ignora completamente a linha
            }
        }

        // Remove comentários de linha
        linha = linha.replaceAll("//.*", "");

        // Verifica se há início de comentário de bloco
        if (linha.contains("/*")) {
            if (linha.contains("*/")) {
                // Remove o trecho do comentário e mantém o restante da linha
                linha = linha.substring(0, linha.indexOf("/*")) +
                        linha.substring(linha.indexOf("*/") + 2);
            } else {
                // Marca que estamos dentro de um comentário de bloco
                linha = linha.substring(0, linha.indexOf("/*"));
                dentroComentarioBloco[0] = true;
            }
        }

        return linha.trim(); // Remove espaços em branco extras
    }
    
    private String identificarCodigoAtomico(String lexeme, String contextoAnterior, List<Token> tokens) {
        switch (contextoAnterior) {
            case "programa":
                return "C06"; // nomPrograma
            case "tipoFunc":
                // Verifica se é imediatamente seguido por um parêntese
                Token ultimoToken = tokens.get(tokens.size() - 1);
                if (ultimoToken.getLexeme().equals("(")) {
                    return "C05"; // nomFuncao
                }
                break;
            case "inteiro":
            case "real":
            case "cadeia":
            case "logico":
            case "caracter":
            case "vazio":
                return "C07"; // variavel
            default:
                return "C07"; // Assume variável por padrão
        }
        return "C07";
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

    /*public void gerarRelatorios(List<Token> tokens, File arquivoFonte) throws IOException {
        // Diretório e nome base do arquivo fonte
        String diretorioSaida = arquivoFonte.getParent();
        String nomeBase = arquivoFonte.getName().replace(".242", ""); // Remove a extensão .242

        // Define os arquivos de saída com as extensões corretas
        File relatorioTokens = new File(diretorioSaida, nomeBase + ".LEX"); // Arquivo para os tokens
        File relatorioTabelaSimbolos = new File(diretorioSaida, nomeBase + ".TAB"); // Arquivo para a tabela de símbolos

        // Gera o relatório da análise léxica
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(relatorioTokens))) {
            writer.write("Relatório da Análise Léxica:\n");
            for (Token token : tokens) {
                writer.write(token + "\n"); // Escreve cada token no arquivo
            }
        }

        // Gera o relatório da tabela de símbolos
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(relatorioTabelaSimbolos))) {
            writer.write("Tabela de Símbolos:\n");
            for (String simbolo : tabelaSimbolos) {
                writer.write(simbolo + "\n"); // Escreve cada símbolo no arquivo
            }
        }

        System.out.println("Relatórios gerados com sucesso:");
        System.out.println("- Relatório de Análise Léxica: " + relatorioTokens.getAbsolutePath());
        System.out.println("- Relatório da Tabela de Símbolos: " + relatorioTabelaSimbolos.getAbsolutePath());
    }*/
    
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
        