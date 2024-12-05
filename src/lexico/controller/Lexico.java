package lexico.controller;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lexico.model.TipoToken;
import lexico.model.Token;

public class Lexico {

    // Padrão único para identificar todos os tokens da linguagem
    private static final Pattern padraoTokens = Pattern.compile(
        "(?<comentario>//.*|/\\*.*?\\*/)|" +  // Ignora comentários de linha ou bloco
        "(?<palavrasReservadas>cadeia|caracter|declaracoes|enquanto|false|fimDeclaracoes|fimEnquanto|fimFuncoes|fimFunc|fimPrograma|fimSe|funcoes|imprime|inteiro|logico|pausa|programa|real|retorna|se|senao|tipoFunc|tipoParam|tipoVar|true|vazio)|" + // Palavras reservadas
        "(?<identificador>[a-zA-Z_][a-zA-Z0-9_]*)|" + // Identificadores (letra ou _ seguidos de letras/dígitos/_)
        "(?<numero>(\\d+\\.\\d+|\\d+|\\d+\\.\\d+[eE][-+]?\\d+))|" + // Números inteiros ou reais (com notação científica)
        "(?<string>\"[^\"]*\")|" + // Strings entre aspas
        "(?<simbolosReservados>:?=|!=|<=|>=|<|>|\\+|\\-|\\*|\\/|:=|#|;|,|\\(|\\)|\\{|\\}|\\[|\\]|%)|" + // Operadores e símbolos
        "(?<espacos>\\s*)|" + // Ignorar espaços em branco
        "(?<erro>.+?)" // Qualquer sequência não reconhecida como token válido
);


    // Conjunto para armazenar identificadores únicos na tabela de símbolos
    private Set<String> tabelaSimbolos = new HashSet<>();

    /**
     * Realiza a análise léxica do arquivo fornecido.
     * @param arquivo Arquivo fonte a ser analisado.
     * @return Lista de tokens identificados.
     * @throws IOException Em caso de falha ao ler o arquivo.
     */
    public List<Token> analisar(File arquivo) throws IOException {
        List<Token> tokens = new ArrayList<>(); // Lista de tokens gerados
        BufferedReader reader = new BufferedReader(new FileReader(arquivo)); // Leitor do arquivo fonte
        String linha;

        int linhaAtual = 1; // Contador de linhas, para registrar onde os tokens aparecem
        while ((linha = reader.readLine()) != null) { // Lê cada linha do arquivo
            Matcher matcher = padraoTokens.matcher(linha); // Aplica o padrão para encontrar tokens
            while (matcher.find()) { // Enquanto encontrar correspondências
                if (matcher.group("comentario") != null) {
                    // Ignorar comentários (não geram tokens)
                    continue;
                } else if (matcher.group("palavrasReservadas") != null) {
                    // Identifica palavras reservadas
                    tokens.add(new Token(matcher.group("palavrasReservadas").toLowerCase(), TipoToken.PALAVRAS_RESERVADAS, linhaAtual));
                } else if (matcher.group("identificador") != null) {
                    // Identifica identificadores
                    String valor = matcher.group("identificador").toLowerCase(); // Normaliza para minúsculas

                    // Verifica se o identificador é uma palavra reservada
                    if (isPalavraReservada(valor)) {
                        tokens.add(new Token(valor, TipoToken.PALAVRAS_RESERVADAS, linhaAtual));  // Trata como palavra reservada
                    } else {
                        if (valor.length() > 30) { // Trunca identificadores maiores que 30 caracteres
                            valor = valor.substring(0, 30);
                        }

                        if (!tabelaSimbolos.contains(valor)) {
                            tokens.add(new Token(valor, TipoToken.IDENTIFICADOR, linhaAtual));
                            tabelaSimbolos.add(valor); // Armazena identificador na tabela de símbolos
                        }

                    }
                   
                } else if (matcher.group("numero") != null) {
                    // Identifica números (inteiros ou reais)
                    tokens.add(new Token(matcher.group("numero"), TipoToken.NUMERO, linhaAtual));
                } else if (matcher.group("string") != null) {
                    // Identifica strings entre aspas
                    tokens.add(new Token(matcher.group("string"), TipoToken.STRING, linhaAtual));
                } else if (matcher.group("simbolosReservados") != null) {
                    // Identifica símbolos reservados (como operadores e delimitadores)
                    tokens.add(new Token(matcher.group("simbolosReservados"), TipoToken.SIMBOLOS_RESERVADOS, linhaAtual));
                } else if (matcher.group("erro") != null) {
                    // Identifica lexemes inválidos (erros)
                    tokens.add(new Token(matcher.group("erro"), TipoToken.ERRO, linhaAtual));
                }
            }
            linhaAtual++; // Incrementa o contador de linhas
        }

        reader.close(); // Fecha o leitor do arquivo
        return tokens; // Retorna a lista de tokens gerados
    }

    private boolean isPalavraReservada(String valor) {
        Set<String> palavrasReservadas = new HashSet<>(Arrays.asList(
            "cadeia" , "caracter" , "declaracoes" , "enquanto", "false", "fimDeclaracoes", "fimEnquanto", "fimFunc", "fimFuncoes", 
            "fimPrograma", "fimSe", "funcoes", "imprime","inteiro", "logico", "pausa", "programa", "real", "retorna", "se", "senao", "tipoFunc", 
            "tipoParam", "tipoVar", "true", "vazio"
            
        ));
        return palavrasReservadas.contains(valor.toLowerCase());  // Verifica sem considerar maiúsculas/minúsculas
    }
    

    /**
     * Exibe a tabela de símbolos no console.
     */
    public void exibirTabelaSimbolos() {
        System.out.println("Tabela de Símbolos:");
        for (String simbolo : tabelaSimbolos) {
            System.out.println(simbolo);
        }
    }

    /**
     * Exibe os tokens gerados no console.
     * @param tokens Lista de tokens a serem exibidos.
     */
    public void exibirTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * Escreve os tokens e a tabela de símbolos em um arquivo.
     * @param tokens Lista de tokens a serem escritos.
     * @param nomeArquivo Nome do arquivo de saída.
     * @throws IOException Em caso de falha ao escrever no arquivo.
     */
    public void escreverTokensEmArquivo(List<Token> tokens, String nomeArquivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo)); // Abre o arquivo para escrita
        writer.write("Tokens:\n");
        for (Token token : tokens) {
            writer.write(token + "\n"); // Escreve cada token no arquivo
        }
        writer.write("\n");

        writer.write("Tabela de Símbolos:\n");
        for (String simbolo : tabelaSimbolos) {
            writer.write(simbolo + "\n"); // Escreve cada símbolo na tabela de símbolos
        }

        writer.close(); // Fecha o escritor
    }
}
