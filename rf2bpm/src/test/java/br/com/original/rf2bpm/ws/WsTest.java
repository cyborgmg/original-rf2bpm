package br.com.original.rf2bpm.ws;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class WsTest {
	
	@Value( "${ws.properties}" )
	private String wsp;

	@Test
	public void test() throws IOException {
		Properties properties = new Properties();
		String fname = wsp+"/hello.properties";
		InputStream inputStream = new FileInputStream(new File(fname));
		properties.load(inputStream);
		String ws = properties.getProperty("ws.url");
		
		CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(ws);
        
        request.addHeader("Content-Type", "text/xml"); 
        
        StringBuilder xml = new StringBuilder();
        xml.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hel=\"http://original.com.br/types/helloworld\">");
    	xml.append("   <soapenv:Header/>");
    	xml.append("   <soapenv:Body>");
    	xml.append("      <hel:person>");
    	xml.append("         <hel:firstName>yyy</hel:firstName>");
    	xml.append("         <hel:lastName>xxx</hel:lastName>");
    	xml.append("      </hel:person>");
    	xml.append("   </soapenv:Body>");
    	xml.append("</soapenv:Envelope>");
        
  	    request.setEntity( new StringEntity(xml.toString(), Charset.forName("UTF-8")) );
               
        CloseableHttpResponse response =  client.execute(request);

        int statusCode=response.getStatusLine().getStatusCode();
       
        assertEquals(200, statusCode); 
	}

}
