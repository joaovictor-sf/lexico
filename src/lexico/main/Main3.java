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
        List<Token> tokens = null;

        // Realiza a análise léxica
		try {
			tokens = analisador.analisar(arquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Exibe os tokens
        analisador.exibirTokens(tokens);

        // Exibe a tabela de símbolos
        analisador.exibirTabelaSimbolos();
        
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
