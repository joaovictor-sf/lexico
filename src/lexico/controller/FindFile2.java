package lexico.controller;

import java.io.File;

public class FindFile2 {
	public File find(String fileName) {
        if (!fileName.endsWith(".242")) {
            System.out.println("O arquivo deve ter a extensão .242.");
            return null;
        }

        File arquivo = new File(fileName);

        if (arquivo.isAbsolute()) {
            if (arquivo.exists()) {
            	System.out.println("Arquivo encontrado: " + arquivo.getAbsolutePath());
                return arquivo;
            }
            System.out.println("Arquivo não encontrado no caminho especificado: " + arquivo.getAbsolutePath());
        } else {

            String caminhoProjeto = System.getProperty("user.dir");
            File arquivoNoProjeto = new File(caminhoProjeto, fileName);

            if (arquivoNoProjeto.exists()) {
            	System.out.println("Arquivo encontrado na pasta do projeto: " + arquivoNoProjeto.getAbsolutePath());
                return arquivoNoProjeto;
            }
            System.out.println("Arquivo não encontrado na pasta do projeto: " + caminhoProjeto);
        }
        
        return null;
	}
	
	public static File buscarArquivo(String nomeArquivo) {
        // Verifica se o arquivo termina com a extensão .242
        if (!nomeArquivo.endsWith(".242")) {
            System.out.println("O arquivo deve ter a extensão .242.");
            return null;
        }

        File arquivo = new File(nomeArquivo);

        // Verifica se o caminho é absoluto ou relativo
        if (arquivo.isAbsolute()) {
            // Se for um caminho absoluto, verifica se o arquivo existe nesse caminho
            if (arquivo.exists()) {
                return arquivo; // Retorna o arquivo se encontrado
            }
        } else {
            // Se for apenas o nome do arquivo, busca na pasta do projeto
            String caminhoProjeto = System.getProperty("user.dir"); // Obtém o diretório atual (pasta do projeto)
            File arquivoNoProjeto = new File(caminhoProjeto, nomeArquivo);

            if (arquivoNoProjeto.exists()) {
                return arquivoNoProjeto; // Retorna o arquivo se encontrado
            }
        }

        return null; // Retorna null se o arquivo não for encontrado
    }
}
