package com.numbergame.service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NumberGameService {
		
	@Value("${com.responselabel.partone}")
	String partOne;
	
	@Value("${com.responselabel.parttwo}")
	String partTwo;			
	
	public String getResponse() {
		Random random = new Random();
		int number1 = random.nextInt(1000);
		int number2 = random.nextInt(1000);
		int number3 = random.nextInt(1000);
		String question = partOne + partTwo + number1 + "," + number2+","+ number3;
		
		return question;		
	}	
			
	
	/**
	 * Validate whether the sum from the question is equal to the calculated sum or not
	 * @param questionText
	 * @param sum
	 * @return
	 */
	public boolean validateResponse(String questionText, String sum) {
		Pattern p = Pattern.compile("([a-zA-Z, _:]*)([0-9]+),([0-9]+),([0-9]+)");//. represents single character  
		Matcher m = p.matcher(questionText);  
		
		while(m.find()) {
			int number1 = Integer.valueOf(m.group(2));
			int number2 = Integer.valueOf(m.group(3));
			int number3 = Integer.valueOf(m.group(4));
			
			return number1+number2+number3==Integer.valueOf(sum);
		}
				
		return false;
	}

}
