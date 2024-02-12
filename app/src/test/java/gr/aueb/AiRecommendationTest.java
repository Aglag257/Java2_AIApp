package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/*This code is for the AiRecommendationTest class, which is a test class for testing the 
functionality of the AiRecommendation class. It sets up a WireMock server, stubs a POST 
request to a specific URL, and verifies the response.*/
public class AiRecommendationTest {

    private static WireMockServer wireMockServer;

    /*
     * setup(): This method is annotated with @BeforeAll and is
     * responsible for setting up the WireMock server. It starts
     * the server and configures it to listen on the localhost and a random port.
     */
    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    /*
     * teardown(): This method is annotated with @AfterAll and is responsible for
     * stopping the WireMock server.
     */
    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    /*
     * testCountry(): This method is annotated with @Test and is the actual test
     * method.
     * It stubs a POST request to the /v1/chat/completions URL and configures the
     * response to have a
     * status code of 200 and a body of "correct".
     */
    @Test
    public void testAiRecommendation() throws Exception {
        stubFor(post(urlEqualTo("/v1/chat/completions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));
    }
}