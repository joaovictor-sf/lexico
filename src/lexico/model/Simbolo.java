package lexico.model;

import java.util.HashSet;
import java.util.Set;

public class Simbolo {
	String lexeme;
    int qtdCharAntesTrunc;
    int qtdCharDepoisTrunc;
    String tipoSimb; // Tipo do símbolo (ou "-" se não aplicável)
    Set<Integer> linhas; // Linhas onde o símbolo apareceu

    public Simbolo(String lexeme, int qtdCharAntesTrunc, String tipoSimb, int linha) {
        this.lexeme = lexeme;
        this.qtdCharAntesTrunc = qtdCharAntesTrunc;
        this.qtdCharDepoisTrunc = lexeme.length();
        this.tipoSimb = tipoSimb != null ? tipoSimb : "-";
        this.linhas = new HashSet<>();
        this.linhas.add(linha);
    }

    public void adicionarLinha(int linha) {
        this.linhas.add(linha);
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

	public Set<Integer> getLinhas() {
		return linhas;
	}

	public void setLinhas(Set<Integer> linhas) {
		this.linhas = linhas;
	}
}
