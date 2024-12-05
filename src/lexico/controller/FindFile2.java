package lexico.controller;

import java.io.File;

public class FindFile2 {

    /**
     * Busca um arquivo pelo nome fornecido, verificando se ele tem a extensão .242.
     * O arquivo pode ser especificado como caminho absoluto ou apenas o nome.
     * @param nomeArquivo Nome ou caminho completo do arquivo
     * @return O arquivo encontrado ou null se não for encontrado
     */
    public File find(String nomeArquivo) {
        // Adiciona a extensão .242 caso o usuário não a tenha incluído
        if (!nomeArquivo.endsWith(".242")) {
            nomeArquivo += ".242";
        }

        // Verifica se é um caminho absoluto
        File arquivo = new File(nomeArquivo);
        if (arquivo.isAbsolute()) {
            if (arquivo.exists()) {
                System.out.println("Arquivo encontrado: " + arquivo.getAbsolutePath());
                return arquivo;
            } else {
                System.out.println("Arquivo não encontrado no caminho especificado: " + arquivo.getAbsolutePath());
                return null;
            }
        }

        // Caso seja um caminho relativo, busca na pasta do projeto
        String caminhoProjeto = System.getProperty("user.dir");
        File arquivoNoProjeto = new File(caminhoProjeto, nomeArquivo);

        if (arquivoNoProjeto.exists()) {
            System.out.println("Arquivo encontrado na pasta do projeto: " + arquivoNoProjeto.getAbsolutePath());
            return arquivoNoProjeto;
        } else {
            System.out.println("Arquivo não encontrado na pasta do projeto: " + caminhoProjeto);
            return null;
        }
    }
}