package com.numbergame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.numbergame.service.NumberGameService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NumberGameApplicationTests {
	
	@Autowired
	NumberGameService responseService;
	
	@Autowired 
	TestRestTemplate template;
	
	@Value("${com.responselabel.partone}")
	String partOne;
	
	@Value("${com.responselabel.parttwo}")
	String partTwo;

	/**
	 * In this test, we are taking the question from the first endpoint (/question), and parsing the response to sum the numbers and sending
	 * the calculated sum to the second endpoint (/validate). Since we are calculating sum from the original response the sum should be same and response is
	 * as expected 
	 */
	@Test
	public void testCorrectSumResponse() {
		HttpEntity<String> question =  template.exchange("/question", HttpMethod.GET, null, String.class);
		String questionResponse = question.getBody();
		HttpHeaders headers = question.getHeaders();
		List<String> headerValue = headers.get("number-game-id");
		String gameId = headerValue.get(0);
		Integer[] numbers = parseNumber(questionResponse);
		int sum = numbers[0]+numbers[1]+numbers[2];
		
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.add("number-game-id", gameId);

		HttpEntity<String> entity = new HttpEntity<>("body", reqHeaders);
		ResponseEntity<String> answerValidationResponse= template.exchange("/validate?inputQuestion="+questionResponse+"&sum="+sum, HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.OK, answerValidationResponse.getStatusCode());
	    String response = answerValidationResponse.getBody();
	    if (response != null) {
	        assertEquals("Thats Great", response);
	    }
	}
	
	/**
	 * In this test, we are taking the question from the first endpoint (/question), and sending 0 to the second endpoint (/validate). Since we are sending sum 
	 * as zero which is not the actual sum, it should send the response saying pleaase try again
	 *  
	 */
	@Test
	public void testWrongSumResponse() {
		HttpEntity<String> question =  template.exchange("/question", HttpMethod.GET, null, String.class);
		String questionResponse = question.getBody();
		HttpHeaders headers = question.getHeaders();
		List<String> headerValue = headers.get("number-game-id");
		String gameId = headerValue.get(0);
		int sum = 0;
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.add("number-game-id", gameId);

		HttpEntity<String> entity = new HttpEntity<>("body", reqHeaders);
		ResponseEntity<String> answerValidationResponse= template.exchange("/validate?inputQuestion="+questionResponse+"&sum="+sum, HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, answerValidationResponse.getStatusCode());
	    String response = answerValidationResponse.getBody();
	    if (response != null) {
	        assertEquals("Thats Wrong. Plese Try Again", response);
	    }
	}
	
	/**
	 * In this test, manipulating the question and answer.
	 */
	@Test
	public void testQuestionManipulatedRejected() {
		HttpEntity<String> question =  template.exchange("/question", HttpMethod.GET, null, String.class);
		HttpHeaders headers = question.getHeaders();
		List<String> headerValue = headers.get("number-game-id");
		String gameId = headerValue.get(0);
		String manipulatedQuestion = "Here you go, solve the question: Please sum the numbers 1,1,1";
		int manupulatedSum = 3;

		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.add("number-game-id", gameId);

		HttpEntity<String> entity = new HttpEntity<>("body", reqHeaders);
		ResponseEntity<String> answerValidationResponse= template.exchange("/validate?inputQuestion="+manipulatedQuestion+"&sum="+manupulatedSum, HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, answerValidationResponse.getStatusCode());
	    String response = answerValidationResponse.getBody();
	    if (response != null) {
	        assertEquals("Response is Tampered.", response);
	    }
	}
	
	/**
	 * In this test, the correlation is tested between Ques and Ans. If correct sequence is not passed then 
	 * the validation fails
	 */
	@Test
	public void testInvalidQuesAnsCorrelation() {
		HttpEntity<String> question =  template.exchange("/question", HttpMethod.GET, null, String.class);
		String questionResponse = question.getBody();
		HttpHeaders headers = question.getHeaders();
		List<String> headerValue = headers.get("number-game-id");
		String gameId = headerValue.get(0); // Not using this in further reuqest
		Integer[] numbers = parseNumber(questionResponse);
		int sum = numbers[0]+numbers[1]+numbers[2];
		
		HttpHeaders reqHeaders = new HttpHeaders();
		//reqHeaders.add("number-game-id", 100);

		HttpEntity<String> entity = new HttpEntity<>("body", reqHeaders);
		ResponseEntity<String> answerValidationResponse= template.exchange("/validate?inputQuestion="+questionResponse+"&sum="+sum, HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, answerValidationResponse.getStatusCode());
	    String response = answerValidationResponse.getBody();
	    if (response != null) {
	        assertEquals("Response is Tampered.", response);
	    }
	}
	
	
	/**
	 * Parses the given text for the numbers to sum
	 * @param questionText
	 * @return
	 */
	public Integer[] parseNumber(String questionText) {
		Pattern p = Pattern.compile("([a-zA-Z, _:]*)([0-9]+),([0-9]+),([0-9]+)");//. represents single character  
		Matcher m = p.matcher(questionText);  
		Integer[] numbers = new Integer[3];
		
		while(m.find()) {
			numbers[0] = Integer.valueOf(m.group(2));
			numbers[1] = Integer.valueOf(m.group(3));
			numbers[2] = Integer.valueOf(m.group(4));					
		}
		
		return numbers;						
	}
	
	

}
