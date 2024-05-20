package restClient;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    public Response performRequest(String requestType, RequestSpecification requestSpecification, String endpoint) {
        switch (requestType) {
            case "post":
                return prepareClient(requestSpecification).post(endpoint);
            case "get":
                return prepareClient(requestSpecification).get(endpoint);
            case "delete":
                return prepareClient(requestSpecification).delete(endpoint);
        }
        return null;
    }

    public RequestSpecification prepareClient(RequestSpecification requestSpecification) {

        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        return requestSpecification;
    }
}
