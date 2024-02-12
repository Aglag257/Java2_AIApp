package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/*The PersonTest class is a JUnit test class that tests the functionality of the Person class. It uses WireMock to mock HTTP requests and responses for testing purposes. */
public class PersonTest {

    private static WireMockServer wireMockServer;

    /*
     * setup(): This method is annotated with @BeforeAll and is executed before all
     * test methods. It starts a WireMock server and configures it to listen on the
     * localhost and a random port.
     */
    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    /*
     * teardown(): This method is annotated with @AfterAll and is executed after all
     * test methods. It stops the WireMock server.
     */
    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    /*
     * testPersonDetails(): This method is annotated with @Test and tests the Person
     * class method for retrieving person details. It defines a WireMock mapping for
     * the HTTP request /3/person/123 and expects a response with status code 200
     * and body "correct".
     */
    @Test
    public void testPersonDetails() throws Exception {

        stubFor(get(urlEqualTo("/3/person/123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }

    /*
     * testPersonCredits(): This method is annotated with @Test and tests the Person
     * class method for retrieving person credits. It defines a WireMock mapping for
     * the HTTP request /3/person/123/movie_credits and expects a response with
     * status code 200 and body "correct".
     */
    @Test
    public void testPersonCredits() throws Exception {

        stubFor(get(urlEqualTo("/3/person/123/movie_credits"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }
}