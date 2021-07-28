package com.gaggle.candidateassignment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.gaggle.candidateassignment.service.LambdaRequestHandlerService;

@Component
public class LambdaRequestHandler implements RequestHandler<Map<String, String>, Map<String, Object>> {

	@Autowired
	LambdaRequestHandlerService lambdaRequestHandlerService;

	@Override
	public Map<String, Object> handleRequest(Map<String, String> input, Context context) {

		return lambdaRequestHandlerService.respondToHandleRequest(input, context);

	}

}