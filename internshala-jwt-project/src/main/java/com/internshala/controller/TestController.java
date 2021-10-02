package com.internshala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internshala.model.User;

@RestController
public class TestController {

	@Autowired
	RestTemplate restTemplate;

	private static final String AUTHENTICATION_URL = "https://customaise.com/api/token";
    private static final String HELLO_URL = "https://customaise.com/api/get_current_user_info";

    @RequestMapping(value = "/getResponse", method = RequestMethod.GET)
    public String getResponse() throws JsonProcessingException {

        String response = null;
       
       String getToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3R1c2VyQGN1c3RvbWFpc2UuY29tIiwic2NvcGVzIjpbXSwiZXhwIjoxNjMzMTc5MDI0fQ.h0BPIwZe9T75X5WKVuQ-tT3iUjAXPD9I4z_XheeHGv4";
        try {

                User authenticationUser = getAuthenticationUser();
                    String token = "Bearer " + getToken;
                    System.out.println(token);
                    HttpHeaders headers = getHeaders();
                    headers.set("Authorization", token);
                    HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
                    // Use Token to get Response
                    ResponseEntity<String> helloResponse = restTemplate.exchange(HELLO_URL, HttpMethod.GET, jwtEntity, String.class);
                    if (helloResponse.getStatusCode().equals(HttpStatus.OK)) {
                        response = helloResponse.getBody();
                    }
                
           
        } catch (Exception ex) {
            System.out.println("ex: "+ex);
        }
        return response;
    }


    private User getAuthenticationUser() {
        User user = new User();
        user.setUsername("testuser@customaise.com");
        user.setPassword("intellipwd");
        return user;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.set("Accept", MediaType.ALL_VALUE);
        return headers;
    }

    private String getBody(final User user) throws JsonProcessingException {
        String out = new ObjectMapper().writeValueAsString(user);
        return out;
    }
}