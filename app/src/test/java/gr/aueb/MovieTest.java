
package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MovieTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

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
