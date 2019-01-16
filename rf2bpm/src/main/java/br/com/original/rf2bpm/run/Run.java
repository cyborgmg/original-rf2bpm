package br.com.original.rf2bpm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.original.rf2bpm.dto.CabecalhoDto;
import br.com.original.rf2bpm.dto.ItemDto;
import br.com.original.rf2bpm.dto.LoteDto;
import br.com.original.rf2bpm.util.Utils;


@Component
public class Run {

	@Value( "${system.files}" )
	private String FS;
	
	@Value( "${ws.properties}" )
	private String wsp;

	@EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void start() throws IOException {
		
        File directory = new File(FS);
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        
        for (File file : fileList){ 
        	try {
				
				StringBuilder sb = new StringBuilder("");
				
				File arquivo = new File( FS+"/"+file.getName() );
				
				LoteDto loteDto = ((LoteDto) Utils.xml2obj( FileUtils.readFileToString( arquivo ) , LoteDto.class) );
				
				for (ItemDto item : loteDto.getItems()) {
					try {
						
						sb.append(this.postSOAP( item.getPayload(), loteDto.getCabecalho() )+" - "+ item.getPayload()+"\n");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				String fname = file.getName().replaceAll(".xml", "")+".status";
				
				FileUtils.writeStringToFile(new File( FS +"/"+ fname ), sb.toString() );
				
				arquivo.renameTo( new File( FS+"/"+file.getName().replaceAll(".xml", ".read") ) );
				
			} catch (JAXBException | IOException e) {
				e.printStackTrace();
			} 
        };
        
	}
	
	private int postSOAP(String xml, CabecalhoDto cab) throws Exception{
		
		Properties properties = new Properties();
		String fname = wsp+"/"+cab.getOperacao()+".properties";
		InputStream inputStream = new FileInputStream(new File(fname));
		properties.load(inputStream);
		
		CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(properties.getProperty("ws.url"));
        
        request.addHeader("Content-Type", "text/xml"); 
        
  	    request.setEntity( new StringEntity(xml, Charset.forName("UTF-8")) );
               
        CloseableHttpResponse response =  client.execute(request);

        int statusCode=response.getStatusLine().getStatusCode();
       
        return statusCode;
		
	}

}
