
package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/*The MovieTest class is a test class that uses WireMock to set up mock responses for movie details and movie credits endpoints. It is used to test the functionality of these endpoints in a controlled environment. */
public class MovieTest {

    private static WireMockServer wireMockServer;

    /*
     * setup(): This method is annotated with @BeforeAll and is responsible for
     * starting the WireMock server and configuring it to listen on the localhost
     * and a specific port.
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
     * testMovieDetails(): This method is annotated with @Test and is used to test
     * the movie details endpoint. It sets up a WireMock stub mapping for the
     * endpoint, specifying the expected request parameters and headers, and the
     * response status and body.
     */
    @Test
    public void testMovieDetails() throws Exception {
        // Set up WireMock mappings for the movie details request
        stubFor(get(urlPathMatching("/3/movie/[0-9]+"))
                .withQueryParam("language", equalTo("en-US"))
                .withHeader("accept", matching("application/json"))
                .withHeader("Authorization", matching("Bearer [a-zA-Z0-9]+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }

    /*
     * testGetMovieCredits(): This method is annotated with @Test and is used to
     * test the movie credits endpoint. It sets up a WireMock stub mapping for the
     * endpoint, specifying the response status, headers, and body.
     */
    @Test
    public void testGetMovieCredits() throws Exception {
        // Set up WireMock to respond with a custom response for the movie credits
        // endpoint
        stubFor(get(urlEqualTo("/3/movie/123/credits"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("correct")));
    }
}