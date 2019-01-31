package br.com.original.rf2bpm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class DateTimeAdapter extends XmlAdapter<String, Date> {

    public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss z";

    @Override
    public String marshal(Date date){
    	
        try {
			final SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.US);
			formatter.setTimeZone(TimeZone.getDefault());
			return formatter.format(date);
		} catch (Exception e) {
			return null;
		}
        
    }

    @Override
    public Date unmarshal(String date){
    	
        try {
			final SimpleDateFormat parser = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.US);
			parser.setTimeZone(TimeZone.getDefault());
			return parser.parse(date);
		} catch (ParseException e) {
			return null;
		}
        
    }

}