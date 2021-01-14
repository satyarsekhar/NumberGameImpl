package com.numbergame.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.numbergame.service.NumberGameService;
import com.numbergame.util.NumberGameUtilities;

@RestController
public class NumberGameController {

	@Autowired
	NumberGameService numberGameService;		

	@GetMapping("/question")
	public ResponseEntity getQuestion(HttpServletRequest request, HttpServletResponse response) {
		String question = numberGameService.getResponse();
		HttpHeaders headers = NumberGameUtilities.setHeaders(request, question);
		return new ResponseEntity(question, headers, HttpStatus.OK);

	}

	@GetMapping("/validate")
	public ResponseEntity<String> validateAnswer(@RequestParam("inputQuestion") String inputQuestion, String answer,
			@RequestParam("sum") String sum, HttpServletRequest request) {
		// Get the response for the latest request
		String responseString = NumberGameUtilities.getQuestionFromHeader(request);
		// If the question is not the latest, throw the error message
		if (responseString==null || !responseString.equals(inputQuestion)) {
			return new ResponseEntity<>("Response is Tampered.", HttpStatus.BAD_REQUEST);
		}
		// If the question is latest, validate the question and the answer
		boolean response = numberGameService.validateResponse(inputQuestion, sum);
		if (response) {
			return new ResponseEntity<>("Thats Great", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Thats Wrong. Plese Try Again", HttpStatus.BAD_REQUEST);

		}
	}

	
}
