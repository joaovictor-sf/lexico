package lexico.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lexico.main.Lexico.Token;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File arquivo = new File("codigo_ficticio.txt");  // Substitua com o caminho do seu arquivo
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
        
        String nomeArquivoSaida = "resultado_analise.txt";  // Arquivo de saída
        /*try {
			analisador.escreverTokensEmArquivo(tokens, nomeArquivoSaida);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro");
			e.printStackTrace();
		}*/
	}

}
