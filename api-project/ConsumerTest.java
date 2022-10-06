package LiveProject;

import au.com.dius.pact.consumer.dsl.*;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)

public class ConsumerTest {
// Headers Object

    Map<String, String> headers = new HashMap<>();
//Resources path

    String resourcePath = "/api/users";
// Generate a contract

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
//Add the Headers

        headers.put("Content-Type", "application/json");

        // create the JSON body for reqeust and return

        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id", 123)
                .stringType("firstName", "Saahil")
                .stringType("lastName","Sharma")
                .stringType("email", "saahil@example.com");

//Write the Fragment to pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .headers(headers)
                .path(resourcePath)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest() {
        //BAse Uri

        String reqestURI = "http://localhost:8282" + resourcePath;

        //Request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Saahil");
        reqBody.put("lastName","Sharma");
        reqBody.put("email", "saahil@example.com");
// Generate response

        given().headers(headers).body(reqBody).log().all().when().post(reqestURI).then()
                .statusCode(201).log().all();
    }
}
