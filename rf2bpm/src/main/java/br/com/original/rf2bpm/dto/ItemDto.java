package br.com.original.rf2bpm.dto;

import javax.xml.bind.annotation.XmlElement;

public class ItemDto {

	private String payload;

	public ItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemDto(String payload) {
		super();
		this.payload = payload;
	}

	@XmlElement(name = "payload")
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "ItemDto [payload=" + payload + "]";
	}

}
