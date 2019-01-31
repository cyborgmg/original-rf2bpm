package br.com.original.rf2bpm.files;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.original.rf2bpm.dto.CabecalhoDto;
import br.com.original.rf2bpm.dto.ItemDto;
import br.com.original.rf2bpm.dto.LoteDto;
import br.com.original.rf2bpm.util.Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@TestPropertySource("file:/opt/BPMO/properties/rf2bpm.properties")
public class FilesTest {
	
	@Value( "${system.files}" )
	private String FS;
	
	@Value( "${system.files.error}" )
	private String FSE;
	
	@Before
	public void init() throws IOException{
		FileUtils.cleanDirectory(new File(FS));
		FileUtils.cleanDirectory(new File(FSE));
	}
	
	@Test
	public void produceArquivos() throws IOException, JAXBException{

		for (int i = 0; i < 10; i++) {
			
			
			LoteDto loteDto = new LoteDto();

			CabecalhoDto cabecalhoDto = new CabecalhoDto();
			cabecalhoDto.setOperacao("hello");
			cabecalhoDto.setSistemaOrigem("sistemaOrigem");
			cabecalhoDto.setTimestamp("01-01-2018 10:10:10");
			
			loteDto.setCabecalho(cabecalhoDto);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hel=\"http://original.com.br/types/helloworld\">");
			sb.append("   <soapenv:Header/>");
			sb.append("   <soapenv:Body>");
			sb.append("      <hel:person>");
			sb.append("         <hel:firstName>Nome"+String.valueOf(i)+"</hel:firstName>");
			sb.append("         <hel:lastName>SobreNome"+String.valueOf(i)+"</hel:lastName>");
			sb.append("      </hel:person>");
			sb.append("   </soapenv:Body>");
			sb.append("</soapenv:Envelope>");
			
			
			loteDto.getItems().add(new ItemDto( Utils.toCdata( sb.toString()) ) );
			loteDto.getItems().add(new ItemDto( Utils.toCdata( sb.toString()) ) );
			loteDto.getItems().add(new ItemDto( Utils.toCdata( sb.toString()) ) );

			FileUtils.writeStringToFile(new File( FS +"/"+ "Arquivo" + i + ".xml"), Utils.obj2xml(loteDto).replaceAll("&lt;", "<").replaceAll("&gt;", ">"));

		}
	}
	


}
