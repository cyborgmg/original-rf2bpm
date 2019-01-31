package br.com.original.rf2bpm.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import br.com.original.rf2bpm.util.DateTimeAdapter;

public class ItemDto {
	
	private Integer statusCode;
	
	private Date timeStamp;

	@XmlCDATA
	private String payload;
	@XmlCDATA
	private String response;

	public ItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemDto(String payload) {
		super();
		this.payload = payload;
	}

	@XmlCDATA
	//@XmlElement(name = "payload")
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@XmlCDATA
	//@XmlElement(name = "response")
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	@XmlAttribute(name="status")
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@XmlAttribute(name="timestamp")
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}


}
