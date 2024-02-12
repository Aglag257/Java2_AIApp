package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/*isUserInChatroomTest(): Tests the isUserInChatroom() 
method of the Chatroom class by checking if a user is in the chatroom. */
public class CountryTest {

    private static WireMockServer wireMockServer;

    /*
     * setup(): This method is annotated with @BeforeAll and is responsible for
     * setting up the WireMock server before running any tests.
     * It starts the server and configures it to listen on the localhost and a
     * random port.
     */
    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    /*
     * teardown(): This method is annotated with @AfterAll and is responsible for
     * tearing down
     * the WireMock server after all tests have been executed. It stops the server.
     */
    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    /*
     * testCountry(): This method is annotated with @Test and is the actual test
     * method. It defines a WireMock mapping for a specific URL
     * (/3/watch/providers/regions) and configures the response to return a status
     * code of 200 and a body of "correct".
     */
    @Test
    public void testCountry() throws Exception {
        // Define your WireMock mapping for movie availability
        stubFor(get(urlEqualTo("/3/watch/providers/regions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }
}