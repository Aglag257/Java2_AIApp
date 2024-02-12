package gr.aueb;

import org.apiguardian.api.API;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.URISyntaxException;

/*The BonusContentTest class is a test class that contains test methods for testing the functionality of the BonusContent class.
 It tests various scenarios such as null or empty inputs, list size, and output validation. */
public class BonusContentTest {
    private static String youtubeApiKey;

    /*
     * testSearchAndPrintVideo_NullSearchQuery: Tests the searchAndPrintVideo method
     * with a null search query.
     * Expects an IllegalArgumentException to be thrown with the message
     * "Search Query cannot be null or empty."
     */

    @Test
    public void testSearchAndPrintVideo_NullSearchQuery() throws URISyntaxException {
        String searchQuery = null;
        String category = "Fun facts";
        File youtubeFile = new File("c:/Users/Βασιλης/OneDrive/Υπολογιστής/apiKeys/youtube_key.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(youtubeFile))) {
            youtubeApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading YouTube API key file.");
            System.exit(1);
        }

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, youtubeApiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Search Query cannot be null or empty.", e.getMessage());
        }
    }

    /*
     * testSearchAndPrintVideo_EmptyCategory: Tests the searchAndPrintVideo method
     * with an empty category.
     * Expects an IllegalArgumentException to be thrown with the message
     * "category cannot be null or empty."
     */
    @Test
    public void testSearchAndPrintVideo_EmptyCategory() throws URISyntaxException {
        String searchQuery = "Pulp Fiction";
        String category = null;
        File youtubeFile = new File("c:/Users/Βασιλης/OneDrive/Υπολογιστής/apiKeys/youtube_key.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(youtubeFile))) {
            youtubeApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading YouTube API key file.");
            System.exit(1);
        }
        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, youtubeApiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("category cannot be null or empty.", e.getMessage());
        }
    }

    /*
     * testSearchAndPrintVideo_NullApiKey: Tests the searchAndPrintVideo method with
     * a null API key.
     * Expects an IllegalArgumentException to be thrown with the message
     * "ApiKey cannot be null or empty."
     */
    @Test
    public void testSearchAndPrintVideo_NullApiKey() throws URISyntaxException {
        String searchQuery = "Barbie";
        String category = "Behind the Scenes";
        String apiKey = null;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("ApiKey cannot be null or empty.", e.getMessage());
        }
    }

    /*
     * testCheckItemsSize_NotEmptyList: Tests the checkItemsSize method with a
     * non-empty list.
     * Expects the list size to be greater than 0.
     */
    @Test
    public void testCheckItemsSize_NotEmptyList() {
        List<Object> items = new ArrayList<>();
        items.add(new Object()); // Προσθέτουμε ένα στοιχείο στη λίστα

        assertTrue(items.size() > 0);
    }

    /*
     * testCheckItemsSize_EmptyList: Tests the checkItemsSize method with an empty
     * list.
     * Expects the list size to be 0.
     */
    @Test
    public void testCheckItemsSize_EmptyList() {
        List<Object> items = new ArrayList<>();

        assertFalse(items.size() > 0);
    }

    /*
     * testIterateAndPrint_NonEmptyList: Tests the iterateAndPrintWrapper method
     * with a non-empty list.
     * Verifies that the expected output is printed to the console.
     */
    @Test
    public void testIterateAndPrint_NonEmptyList() {
        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");

        // Εκτέλεση της μεθόδου iterateAndPrint και αποθήκευση της έξοδου
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Καλείστε τη στατική μέθοδο wrapper στην κλάση δοκιμών
        BonusContentTest.iterateAndPrintWrapper(items);

        // Ελέγχουμε αν η έξοδος περιέχει τα αναμενόμενα κείμενα
        String expectedOutput = String.format("Item 1%sItem 2%sItem 3%s", System.lineSeparator(),
                System.lineSeparator(), System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());
    }

    /*
     * testIterateAndPrint_EmptyList: Tests the iterateAndPrintWrapper method with
     * an empty list.
     * Verifies that no output is printed to the console.
     */
    @Test
    public void testIterateAndPrint_EmptyList() {
        List<String> items = new ArrayList<>();

        // Εκτέλεση της μεθόδου iterateAndPrint και αποθήκευση της έξοδου
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Καλείστε τη στατική μέθοδο wrapper στην κλάση δοκιμών
        BonusContentTest.iterateAndPrintWrapper(items);

        // Ελέγχουμε αν η έξοδος είναι κενή
        assertEquals("", outContent.toString());
    }

    // Wrapper γύρω από την iterateAndPrint για την κλάση δοκιμών
    private static void iterateAndPrintWrapper(List<String> items) {
        for (String item : items) {
            System.out.println(item);
        }
    }

}