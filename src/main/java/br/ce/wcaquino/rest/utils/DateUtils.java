package br.ce.wcaquino.rest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static String getDateDiferencaDias(Integer qtdDias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qtdDias);
		return getDateFormatada(cal.getTime());
		
	}
	
	public static String getDateFormatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

}
