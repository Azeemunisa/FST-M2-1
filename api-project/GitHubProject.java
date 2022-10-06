package LiveProject;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;



public class GitHubProject {
    String sshkey="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCDTycohQd3IbRoGfq9ZloRvNktxWO4k4cQ0/zWhr3JMTX7G3QmPSQvP3iRfuP5gnVq47vhznWcGxuos/AbkwNvyY8kmLUev4lWotlQGpJ/Su6I1sgY6/vHQws6LHJkGf7ONjP9pxtnFz9VnmDIl/JSANouY4hf40fxO3jyVNm6XCicpOFWWRdWvxeACtUjyycgZ9B0ILEfVDjNBuotfZ1YJnVb3QjHtN3NyrZG5nJlU0BHbSAj41yh3O1j7J4cEIKt3nyA0qCFrD6no3WodD1zD47/ZkZUWaJB0XI8LM/ipDSPi179VLdc/zzazNXkO1gzqdOgb9lXtqcgsDijZeJZ";
    int sshid;
    RequestSpecification rqs;

    @BeforeClass

    public void setup() {
        rqs = new RequestSpecBuilder().setBaseUri("https://api.github.com").addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token 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").build();
    }

    @Test(priority = 1)
    public void postTest() {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("title", "testkey");
        reqBody.put("key", sshkey);

        Response resp = given().spec(rqs).body(reqBody).when().post("/user/keys");
        System.out.println(resp.getBody().asPrettyString());

        sshid = resp.then().extract().path("id");
        resp.then().statusCode(201).body("key", equalTo(sshkey));
    }

    @Test(priority = 2)

    public void getTest() {
        Response resp = given().spec(rqs).when().pathParam("keyId", sshid)
                .when().get("/user/keys/{keyId}");
        System.out.println(resp.getBody().asPrettyString());
        sshid = resp.then().extract().path("id");
        resp.then().statusCode(200).body("key", equalTo(sshkey));
    }

    @Test(priority = 3)

    public void deleteTest() {
        Response resp = given().spec(rqs).pathParam("keyId",sshid)
                .when().delete("/user/keys/{keyId}");
        System.out.println(resp.getBody().asPrettyString());
        resp.then().statusCode(204);
    }
}