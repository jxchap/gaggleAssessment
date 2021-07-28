package com.gaggle.candidateassignment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.lambda.runtime.Context;
import com.gaggle.candidateassignment.domain.Person;

@Service
public class LambdaRequestHandlerService {

	@Autowired
	DynamicDatabaseService dynamicDatabaseService;

	public Map<String, Object> respondToHandleRequest(Map<String, String> input, Context context) {

		// respond according to input from lambda caller
		Map<String, Object> returnMap = new HashMap<>();
		
		if (input.get("searchByName") != null) {

			String temp = null;

			try {
				temp = "%" + input.get("searchByName") + "%";
			} catch (Exception e) {
				returnMap.put("result", "This function takes only a String value.");
				return returnMap;
			}

			List<Person> persons = dynamicDatabaseService.getListOfPersons(temp);

			returnMap.put("result", persons);
			return returnMap;

		} else if (input.get("searchById") != null) {

			Integer personId = null;

			try {
				personId = Integer.parseInt(input.get("searchById"));
			} catch (Exception e) {
				returnMap.put("result", "This function takes only an integer value.");
				return returnMap;
			}

			Person person = dynamicDatabaseService.getOnePerson(personId);

			returnMap.put("result", person);

			return returnMap;

		} else {

			returnMap.put("result",
					"There are two functions available: searchByName and searchById. To use either, create "
							+ "a simple JSON object using searchByName or searchById as a key, and either a String or integer for the value.");

			return returnMap;

		}

	}

}
