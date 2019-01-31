package br.com.original.rf2bpm.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.google.gson.Gson;

public class Utils {
	
	public static String obj2xml(Object o) throws JAXBException{
		
		StringWriter stringWriter = new StringWriter();
		
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());

        Marshaller marshaller = jaxbContext.createMarshaller();
        
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(o, stringWriter);
        
        return stringWriter.toString();
		
	}
	
	public static Object xml2obj(String xml, Class<?> clazz) throws JAXBException{

        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
	}
	
	public static String toCdata(String s){
		
		StringBuilder sb = new StringBuilder("<![CDATA[");
		sb.append(s);
		sb.append("]]>");
		
		return sb.toString();
	}
	
	public static String serializeObject(Object o) {
	    Gson gson = new Gson();
	    String serializedObject = gson.toJson(o);
	    return serializedObject;
	}
	//___________________________________________________________________________________

	public static Object unserializeObject(String s, Object o){
	    Gson gson = new Gson();
	    Object object = gson.fromJson(s, o.getClass());
	    return object;
	}
	       //___________________________________________________________________________________
	public static Object cloneObject(Object o){
	    String s = serializeObject(o);
	    Object object = unserializeObject(s,o);
	    return object;
	}

}
