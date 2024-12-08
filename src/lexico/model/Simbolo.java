package lexico.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simbolo {
	String lexeme;
    int qtdCharAntesTrunc;
    int qtdCharDepoisTrunc;
    String tipoSimb; // Tipo do símbolo (ou "-" se não aplicável)
    //Set<Integer> linhas; // Linhas onde o símbolo apareceu
    List<Integer> linhas;
    boolean primeiro = false;

    public Simbolo(String lexeme, int qtdCharAntesTrunc, String tipoSimb, int linha) {
        this.lexeme = lexeme;
        this.qtdCharAntesTrunc = qtdCharAntesTrunc;
        this.qtdCharDepoisTrunc = lexeme.length();
        this.tipoSimb = tipoSimb != null ? tipoSimb : "-";
        this.linhas = new ArrayList<>();
        this.linhas.add(linha);
    }

    public void adicionarLinha(int linha) {
    	/*if(!(linha == 1 && linhas.get(0) == 1)) {
    		this.linhas.add(linha);
    	}*/
    	if(primeiro == false) {
    		primeiro = true;
    	}
    	else{
    		linhas.add(linha);
    	}
    	
    }

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public int getQtdCharAntesTrunc() {
		return qtdCharAntesTrunc;
	}

	public void setQtdCharAntesTrunc(int qtdCharAntesTrunc) {
		this.qtdCharAntesTrunc = qtdCharAntesTrunc;
	}

	public int getQtdCharDepoisTrunc() {
		return qtdCharDepoisTrunc;
	}

	public void setQtdCharDepoisTrunc(int qtdCharDepoisTrunc) {
		this.qtdCharDepoisTrunc = qtdCharDepoisTrunc;
	}

	public String getTipoSimb() {
		return tipoSimb;
	}

	public void setTipoSimb(String tipoSimb) {
		this.tipoSimb = tipoSimb;
	}

	public List<Integer> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<Integer> linhas) {
		this.linhas = linhas;
	}
}
