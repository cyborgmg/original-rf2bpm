package br.com.original.rf2bpm.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class CabecalhoDto {

	private String operacao;

	private String timestamp;

	private String sistemaOrigem;

	@XmlAttribute(name="operacao")
	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	@XmlAttribute(name="timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@XmlAttribute(name="sistemaorigem")
	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	public void setSistemaOrigem(String sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
	}

	@Override
	public String toString() {
		return "CabecalhoDto [operacao=" + operacao + ", timestamp=" + timestamp + ", sistemaOrigem=" + sistemaOrigem + "]";
	}

}
