package lexico.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import lexico.controller.FindFile2;
import lexico.controller.Lexico;
import lexico.model.Token;

public class Main3 {
	
	public static void main(String[] args) {
		FindFile2 findFile = new FindFile2();
		
		Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo (ou o caminho completo) com a extensão .242: ");
        String nomeArquivo = scanner.nextLine();
        
        File arquivo = findFile.find(nomeArquivo);
        scanner.close();
        
        Lexico analisador = new Lexico();
		
		try {
	        List<Token> tokens = analisador.analisar(arquivo);
	
	        // Exibir o relatório da análise léxica
	        System.out.println("\nRELATÓRIO COMPLETO:");
	        for (Token token : tokens) {
	            System.out.println(token);
	        }
	
	        // Exibir a tabela de símbolos
	        //System.out.println("\nTABELA DE SÍMBOLOS:");
	        //analisador.exibirTabelaSimbolos();
	
	        analisador.gerarRelatorios(tokens, arquivo);
	        //System.out.println("\nRelatórios gerados com sucesso na mesma pasta do arquivo fonte.");
	
	        
	    } catch (IOException e) {
	        System.err.println("Erro ao processar o arquivo: " + e.getMessage());
	    }
	
		/*  
        //String nomeArquivoSaida = "resultado_analise.txt";  // Arquivo de saída
        /*try {
		analisador.escreverTokensEmArquivo(tokens, nomeArquivoSaida);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro");
			e.printStackTrace();
		}*/
		
	}

}
