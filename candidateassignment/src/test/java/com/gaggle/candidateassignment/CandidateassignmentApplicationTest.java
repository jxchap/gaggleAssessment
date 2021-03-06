package com.gaggle.candidateassignment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.gaggle.candidateassignment.domain.Person;


@SpringBootTest
class CandidateassignmentApplicationTest {

	Context context;

	Map<String, String> testJson;
	
	@Autowired
	LambdaRequestHandler lambdaRequestHandler;

	@BeforeEach
	public void setContext() {
		context = new TestContext();
		testJson = new HashMap<>();
	}

	@Test
	public void testHandleRequestByID() {

		testJson.put("searchById", "1");

		Map<String, Object> result = lambdaRequestHandler.handleRequest(testJson, context);

		assertEquals("Jack", ((Person) result.get("result")).getFirstname());

	}

	@Test
	public void testHandleRequestByString() {

		testJson.put("searchByName", "b");

		Map<String, Object> result = lambdaRequestHandler.handleRequest(testJson, context);

		List<Person> personList = new ArrayList<>();

		result.forEach((key, value) -> {

			((List) value).forEach(x -> personList.add((Person) x));

		});

		String[] testNames = { "Brian", "Bruce" };

		for (int i = 0; i < personList.size(); i++) {

			assertEquals(testNames[i], personList.get(i).getFirstname());
		}

	}

}

class TestContext implements Context {

	@Override
	public String getAwsRequestId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogGroupName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogStreamName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFunctionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFunctionVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvokedFunctionArn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CognitoIdentity getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientContext getClientContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemainingTimeInMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMemoryLimitInMB() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LambdaLogger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

}
