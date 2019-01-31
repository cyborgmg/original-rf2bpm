package br.com.original.rf2bpm.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.original.rf2bpm.util.DateTimeAdapter;

@XmlRootElement(name = "lote")
public class LoteDto {

	private CabecalhoDto cabecalhoDto;

	private List<ItemDto> itemDtos = new ArrayList<ItemDto>();

	@XmlElement(name = "cabecalho")
	public CabecalhoDto getCabecalho() {
		return cabecalhoDto;
	}

	public void setCabecalho(CabecalhoDto cabecalhoDto) {
		this.cabecalhoDto = cabecalhoDto;
	}

	@XmlElement(name = "item")
	public List<ItemDto> getItems() {
		return itemDtos;
	}

	public void setItems(List<ItemDto> itemDtos) {
		this.itemDtos = itemDtos;
	}

	@Override
	public String toString() {
		return "LoteDto [cabecalhoDto=" + cabecalhoDto + "]";
	}

	public boolean hasItemResponse(){
		
		for (ItemDto itemDto : itemDtos) {
			if( itemDto.getResponse()!=null ){
				return true;
			}
		}
		return false;
	}
	
}
