package gr.aueb;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CountryTest {

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
    public void testCountry() throws Exception {
        // Define your WireMock mapping for movie availability
        stubFor(get(urlEqualTo("/3/watch/providers/regions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("correct")));

    }
}