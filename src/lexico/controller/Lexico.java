package lexico.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lexico.model.Simbolo;
import lexico.model.TipoToken;
import lexico.model.Token;

public class Lexico {

    // Padrão único para todos os tokens
	private static final Pattern padraoTokens = Pattern.compile(
		    "\\s*(?<palavrasReservadas>cadeia|caracter|declaracoes|enquanto|false|fimDeclaracoes|fimEnquanto|fimFuncoes|fimFunc|fimPrograma|fimSe|funcoes|imprime|inteiro|logico|pausa|programa|real|retorna|se|senao|tipoFunc|tipoParam|tipoVar|true|vazio)|" +
		    "(?<consCadeia>\"[^\"]*\")|" + // Strings entre aspas duplas
		    "(?<consCaracter>'[^']')|" +  // Caracteres entre aspas simples
		    "(?<numero>\\d+(\\.\\d+)?([eE][+-]?\\d+)?)|" + // Números inteiros, decimais e exponenciais
		    "(?<identificador>[a-zA-Z_][a-zA-Z0-9_]*)" + // Identificadores
		    "|(?<simbolosReservados>%|\\(|\\)|,|:|:=|;|\\?|\\[|\\]|\\{|\\}|\\-|\\+|\\*|\\/|!=|#|<|<=|==|>|>=)" +
		    "(?<caracterInvalido>[^\\w\\s])" // Caracteres inválidos
		);
    //private Set<String> tabelaSimbolos = new HashSet<>();
	private Map<String, Simbolo> tabelaSimbolos = new LinkedHashMap<>();
    private Map<String, Simbolo> tabelaSimbolosDetalhada = new HashMap<>();

    public List<Token> analisar(File arquivo) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;
        int numeroLinha = 0;
        boolean[] dentroComentarioBloco = {false}; // Usar array para alterar dentro do método
        String contextoAnterior = ""; // Rastreia o contexto para determinar o tipo do identificador
        boolean primeiro = false;
        
        //Map<String, Integer> ultimaLinhaPorLexeme = new HashMap<>(); // Rastrear última linha registrada por lexeme

        while ((linha = reader.readLine()) != null) {
            numeroLinha++;
            
            //linha = removerComentarios(linha); // Remove comentários
            linha = removerComentarios(linha, dentroComentarioBloco); // Remove comentários
            if (linha.isEmpty()) continue; // Ignora linhas vazias após a remoção
            
            Matcher matcher = padraoTokens.matcher(linha);
            
            //Map<String, Integer> ultimaLinhaPorLexeme = new HashMap<>(); // Rastrear última linha registrada por lexeme
            Map<String, Integer> ultimaLinhaPorLexeme = new HashMap<>();

            while (matcher.find()) {
                String codigoAtomico = null;
                Integer indiceTabelaSimbolos = null;

                if (matcher.group("palavrasReservadas") != null) {
                    String lexeme = matcher.group("palavrasReservadas").toLowerCase();
                    contextoAnterior = lexeme; // Atualiza o contexto
                    codigoAtomico = getCodigoAtomico(lexeme);
                    tokens.add(new Token(lexeme, TipoToken.PALAVRAS_RESERVADAS, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("consCadeia") != null) {
                    String lexeme = matcher.group("consCadeia");
                    codigoAtomico = "C01"; // consCadeia
                    tokens.add(new Token(lexeme, TipoToken.NUMERO, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("consCaracter") != null) {
                    String lexeme = matcher.group("consCaracter");
                    codigoAtomico = "C02"; // consCaracter
                    tokens.add(new Token(lexeme, TipoToken.NUMERO, codigoAtomico, null, numeroLinha));
                } else if (matcher.group("numero") != null) {
                    String lexeme = matcher.group("numero");
                    if (lexeme.contains("e") || lexeme.contains("E")) {
                        codigoAtomico = "C04"; // Número em notação exponencial
                    } else if (lexeme.contains(".")) {
                        codigoAtomico = "C04"; // Número decimal
                    } else {
                        codigoAtomico = "C03"; // Número inteiro
                    }
                    tokens.add(new Token(lexeme, TipoToken.NUMERO, codigoAtomico, null, numeroLinha));
                }else if (matcher.group("identificador") != null) {

                    final String codigoAtomicoTeste = identificarCodigoAtomico(linha, contextoAnterior, tokens);

                	String lexemeOriginal = matcher.group("identificador");
                    String lexeme = truncarLexeme(lexemeOriginal.toLowerCase());
                    codigoAtomico = identificarCodigoAtomico(lexeme, contextoAnterior, tokens);
                    final int linhaAtual = numeroLinha;

                    // Atualiza ou cria o símbolo detalhado
                    Simbolo simboloDetalhado = tabelaSimbolosDetalhada.computeIfAbsent(lexeme,
                        key -> new Simbolo(lexeme, lexemeOriginal.length(), null, linhaAtual, codigoAtomicoTeste));
                    simboloDetalhado.adicionarLinha(numeroLinha);

                    // Atualiza ou cria o símbolo na tabela de símbolos simplificada
                    tabelaSimbolos.computeIfAbsent(lexeme,
                        key -> new Simbolo(lexeme, lexemeOriginal.length(), null, linhaAtual, codigoAtomicoTeste)).adicionarLinha(numeroLinha);

                    // Determina o índice na tabela de símbolos
                    indiceTabelaSimbolos = new ArrayList<>(tabelaSimbolos.keySet()).indexOf(lexeme) + 1;

                    tokens.add(new Token(lexeme, TipoToken.IDENTIFICADOR, codigoAtomico, indiceTabelaSimbolos, numeroLinha));
                	

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
        //return lexeme.length() > 30 ? lexeme.substring(0, 30) : lexeme;
    	if (lexeme.startsWith("\"")) { 
            // Tratar strings com aspas
            if (lexeme.length() > 30) {
                lexeme = lexeme.substring(0, 29) + "\""; // Trunca com aspas finais
            }
        } else if (lexeme.matches("\\d+\\.\\d+([eE][-+]?\\d+)?")) {
            // Tratar números
            if (lexeme.length() > 30) {
                lexeme = lexeme.substring(0, 30); // Trunca inicialmente
                while (!lexeme.matches("\\d+\\.\\d+([eE][-+]?\\d+)?")) {
                    lexeme = lexeme.substring(0, lexeme.length() - 1); // Ajusta até ficar válido
                }
            }
        } else {
            // Para identificadores e palavras reservadas
            if (lexeme.length() > 30) {
                lexeme = lexeme.substring(0, 30);
            }
        }
        return lexeme;
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
    	if (lexeme.matches("\"[^\"]*\"")) {
            return "C01"; // consCadeia
        } else if (lexeme.matches("'[^']'")) {
            return "C02"; // consCaracter
        } else if (lexeme.matches("\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) {
            if (lexeme.contains(".") || lexeme.toLowerCase().contains("e")) {
                return "C04"; // consReal
            }
            return "C03"; // consInteiro
        }

        switch (contextoAnterior) {
            case "programa":
                return "C06"; // nomPrograma
            case "tipoFunc":
                if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).getLexeme().equals("(")) {
                    return "C05"; // nomFuncao
                }
                break;
            case "inteiro":
            case "real":
            case "cadeia":
            case "logico":
            case "caracter":
            case "vazio":
                return "C07"; // variável
        }

        return "C07"; // Assume variável por padrão
    }

        
    private String getCodigoAtomico(String lexeme) {
        Map<String, String> codigosAtomicos = Map.ofEntries(
            Map.entry("cadeia", "A01"),
            Map.entry("caracter", "A02"),
            Map.entry("declaracoes", "A03"),
            Map.entry("enquanto", "A04"),
            Map.entry("false", "A05"),
            Map.entry("fimdeclaracoes", "A06"),
            Map.entry("fimenquanto", "A07"),
            Map.entry("fimfunc", "A08"),
            Map.entry("fimfuncoes", "A09"),
            Map.entry("fimprograma", "A10"),
            Map.entry("fimse", "A11"),
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
            Map.entry("tipofunc", "A22"),
            Map.entry("tipoparam", "A23"),
            Map.entry("tipovar", "A24"),
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
            Map.entry("conscadeia", "C01"),
            Map.entry("conscaracter", "C02"),
            Map.entry("consinteiro", "C03"),
            Map.entry("consreal", "C04"),
            Map.entry("nomfuncao", "C05"),
            Map.entry("nomprograma", "C06"),
            Map.entry("variavel", "C07")
        );
        return codigosAtomicos.getOrDefault(lexeme, "UNKNOWN");
    }
    
    public void gerarRelatorios(List<Token> tokens, File arquivoFonte) throws IOException {
        // Diretório e nome base do arquivo fonte
        String diretorioSaida = arquivoFonte.getParent();
        String nomeBase = arquivoFonte.getName().replace(".242", ""); // Remove a extensão .242
    
        // Define os arquivos de saída com as extensões corretas
        File relatorioTokens = new File(diretorioSaida, nomeBase + ".LEX"); // Arquivo para os tokens
        File relatorioTabelaSimbolos = new File(diretorioSaida, nomeBase + ".TAB"); // Arquivo para a tabela de símbolos
    
        // Cabeçalho do relatório
        String cabecalho = """
        Código da Equipe: 01
        Componentes: Arthur de Miranda Xavier; arthur.xavier@ucsal.edu.br; (71)98189-0986
                    Kevin Vasques Santos; kevinvasques.santos@ucsal.edu.br; (71)99724-5890
                    João Victor Santana Ferreira; joaovictor.ferreira@ucsal.edu.br; (71)99252-6546
                    Windson Carlos Pionório Teixeira Filho; windson.filho@ucsal.edu.br; (71)98710-8000


        RELATÓRIO DA ANÁLISE LÉXICA. Texto fonte analisado: %s
        ----------------------------------------------------------------------------------------
        """.formatted(arquivoFonte.getName());

        // Gera o relatório da análise léxica
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(relatorioTokens))) {
            writer.write(cabecalho);
            for (Token token : tokens) {
                String linhaRelatorio = "Lexeme: %s, Código: %s, ÍndiceTabSimb: %s, Linha: %d.\n"
                        .formatted(
                            token.getLexeme().toUpperCase(),
                            token.getCodigoAtomico(),
                            token.getIndiceTabelaSimbolos() == null ? "-" : token.getIndiceTabelaSimbolos(),
                            token.getLinha()
                        );
                writer.write("----------------------------------------------------------------------------------------\n");
                writer.write(linhaRelatorio);
            }
        }
        
     // Gera o relatório da tabela de símbolos (.TAB)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(relatorioTabelaSimbolos))) {
            writer.write("""
                Código da Equipe: 01
                Componentes: Arthur de Miranda Xavier; arthur.xavier@ucsal.edu.br; (71)98189-0986
                    Kevin Vasques Santos; kevinvasques.santos@ucsal.edu.br; (71)99724-5890
                    João Victor Santana Ferreira; joaovictor.ferreira@ucsal.edu.br; (71)99252-6546
                    Windson Carlos Pionório Teixeira Filho; windson.filho@ucsal.edu.br; (71)98710-8000

                RELATÓRIO DA TABELA DE SÍMBOLOS. Texto fonte analisado: %s
                ------------------------------------------------------------------------------------
                """.formatted(arquivoFonte.getName()));

            int entrada = 1;
            for (Simbolo simbolo : tabelaSimbolosDetalhada.values()) {
                writer.write("""
                    Entrada: %d, Codigo: %s, Lexeme: %s,
                    QtdCharAntesTrunc: %d, QtdCharDepoisTrunc: %d,
                    TipoSimb: %s, Linhas: %s.
                    ------------------------------------------------------------------------------------
                    """.formatted(
                    entrada++,
                    simbolo.getCodigoAtomico(),
                    simbolo.getLexeme().toUpperCase(),
                    simbolo.getQtdCharAntesTrunc(),
                    simbolo.getQtdCharDepoisTrunc(),
                    simbolo.getTipoSimb(),
                    simbolo.getLinhas()
                ));
            }
        }
        
    }
}
        