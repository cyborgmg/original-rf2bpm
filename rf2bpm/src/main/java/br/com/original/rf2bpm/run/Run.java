package br.com.original.rf2bpm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.original.rf2bpm.dto.CabecalhoDto;
import br.com.original.rf2bpm.dto.ItemDto;
import br.com.original.rf2bpm.dto.LoteDto;
import br.com.original.rf2bpm.response.Response;
import br.com.original.rf2bpm.util.Utils;


@Component
@Configuration
@PropertySource("file:${path.properties}")
public class Run {

	@Value( "${system.files}" )
	private String FS;
	
	@Value( "${system.files.error}" )
	private String FSE;
	
	@Value( "${ws.properties}" )
	private String wsp;
	
	@Value( "${read.file.delay}" )
	private int delay;
	
	@Value( "${system.log}" )
	private String log;
	
	private Logger logger = Logger.getLogger(Run.class.getName());  
	private FileHandler fh;


	private void instaceLogger() throws SecurityException, IOException{
		fh = new FileHandler(log);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);
	}
	
	@EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void start() throws Exception {
		
		this.instaceLogger();
		
		logger.info("lendo system files");
		
        File directory = new File(FS);
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        
        for (File file : fileList){ 
        	try {
				
        		logger.info("lendo arquivo="+file.getName());
        		
				File arquivo = new File( FS+"/"+file.getName() );
				
				logger.info("unmarshal arquivo");
				
				LoteDto loteDto = ((LoteDto) Utils.xml2obj( FileUtils.readFileToString( arquivo ) , LoteDto.class) );
				
				for (ItemDto item : loteDto.getItems()) {
					
					logger.info("send soap");
					
					Response resp = this.postSOAP( item.getPayload(), loteDto.getCabecalho() );
					
					item.setStatusCode(resp.getStatusCode());
					
					item.setTimeStamp(new Date());
						
					item.setResponse( Utils.toCdata(resp.getResponse() ) );
					
					item.setPayload(  Utils.toCdata(item.getPayload()  ) );
					
					logger.info("soap response code="+resp.getStatusCode());
					
				}
				
				LoteDto loteDtoError = (LoteDto) Utils.cloneObject(loteDto); 
				
				List<ItemDto> items = new ArrayList<ItemDto>(loteDto.getItems());
				for (ItemDto item : items) {
					if(item.getStatusCode()!=200){
						loteDto.getItems().remove(item);
					}
				}
				items=null;
				
				List<ItemDto> itemsError = new ArrayList<ItemDto>(loteDtoError.getItems());
				for (ItemDto item : itemsError) {
					if(item.getStatusCode()==200){
						loteDtoError.getItems().remove(item);
					}
				}
				itemsError=null;
				
				if(loteDto.hasItemResponse()){
					logger.info("criando arquivo de status");
					String fname = file.getName().replaceAll(".xml", "")+".status";
					FileUtils.writeStringToFile(new File( FS +"/"+ fname ), Utils.obj2xml(loteDto).replaceAll("&lt;", "<").replaceAll("&gt;", ">") );
				}
				
				if(loteDtoError.getItems().size()>0){
					logger.info("criando arquivo de error");
					String fname = file.getName().replaceAll(".xml", "")+".error";
					FileUtils.writeStringToFile(new File( FSE +"/"+ fname ), Utils.obj2xml(loteDtoError).replaceAll("&lt;", "<").replaceAll("&gt;", ">") );
				}
				
				logger.info("renomeando arquivo para read");
				arquivo.renameTo( new File( FS+"/"+file.getName().replaceAll(".xml", ".read") ) );
				
				logger.info("delay="+delay);
				Thread.sleep(delay);
				
			} catch (JAXBException | IOException e) {
				//e.printStackTrace();
				logger.warning(e.getStackTrace().toString());
			} 
        };
        
	}
	
	private Response postSOAP(String xml, CabecalhoDto cab) throws Exception{
		
		Properties properties = new Properties();
		String fname = wsp+"/"+cab.getOperacao()+".properties";
		InputStream inputStream = new FileInputStream(new File(fname));
		properties.load(inputStream);
		
		CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(properties.getProperty("ws.url"));
        
        request.addHeader("Content-Type", "text/xml"); 
        
  	    request.setEntity( new StringEntity(xml, Charset.forName("UTF-8")) );
               
        CloseableHttpResponse response =  client.execute(request);

        return new Response(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(),"UTF-8"));
		
	}

}
