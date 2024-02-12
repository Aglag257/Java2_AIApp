
package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/*The AvailabilityTest class is a test class that is used to test the availability
 of a movie using WireMock. It sets up a WireMock server, configures it, and defines 
 a mapping for the movie availability endpoint. */
public class AvailabilityTest {

    private static WireMockServer wireMockServer;

    /*
     * setup(): This method is annotated with @BeforeAll and is responsible
     * for setting up the WireMock server. It starts the server and configures it to
     * listen on the localhost and a random port.
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
     * testMovieAvailability(): This method is annotated with @Test and is the
     * actual test method.
     * It defines a WireMock mapping for the movie availability endpoint using the
     * stubFor method.
     */
    @Test
    public void testMovieAvailability() throws Exception {
        // Define your WireMock mapping for movie availability
        stubFor(get(urlEqualTo("/3/movie/123/watch/providers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }
}