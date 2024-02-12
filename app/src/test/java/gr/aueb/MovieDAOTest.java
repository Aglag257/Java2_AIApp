package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*The MovieDAOTest class is a test class that tests the functionality of the MovieDAO class. It includes test methods for retrieving all reviews for a movie, retrieving spoiler-free reviews for a movie, and calculating the average rating for a movie. */
public class MovieDAOTest {
    private static Connection connection;

    /*
     * CreateInserts(): This method is annotated with @BeforeAll and is responsible
     * for setting up the test environment by inserting test data into the database.
     */
    @BeforeAll
    public static void CreateInserts() throws Exception {
        DB db = new DB();
        connection = db.getConnection();

        try (PreparedStatement insertStmt1 = connection.prepareStatement(
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'User1', 'TestPassword', 'TestCountry')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement(
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'User2', 'TestPassword', 'TestCountry')")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement(
                "INSERT INTO review (reviewId, userId, username, movieName, movieId, review_text, date, rating, spoiler) VALUES (1, 1, 'User1', 'MovieTest', 1, 'Sample review text', '2023-01-02', 8, false)")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement(
                "INSERT INTO review (reviewId, userId, username, movieName, movieId, review_text, date, rating, spoiler) VALUES (2, 2, 'User2', 'MovieTest', 1, 'Sample review text', '2023-01-03', 8.5, true)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

    }

    /*
     * DeleteAllInserts(): This method is annotated with @AfterAll and is
     * responsible for cleaning up the test environment by deleting the test data
     * from the database.
     */
    @AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM review WHERE reviewid = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM review WHERE reviewid = 2")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        if (connection != null) {
            connection.close();
        }
    }

    /*
     * getAllReviewsForMovieTest(): This method is annotated with @Test and tests
     * the functionality of the getAllReviewsForMovie() method in the MovieDAO
     * class. It retrieves all reviews for a specific movie and asserts that the
     * expected reviews are returned.
     */
    @Test
    public void getAllReviewsForMovieTest() throws SQLException {
        try {

            List<Review> reviews = MovieDAO.getAllReviewsForMovie(1); // the movieid we have inserted the reviews on

            assertNotNull(reviews);

            if (reviews.size() >= 1) {
                Review firstReview = reviews.get(0);
                assertEquals(1, firstReview.getReviewId());
                assertEquals(1, firstReview.getUserId());
                assertEquals(1, firstReview.getMovieId());
                assertEquals("Sample review text", firstReview.getReviewText());
                assertEquals(8, firstReview.getRating());
                assertFalse(firstReview.isSpoiler());
            }

            if (reviews.size() >= 2) {
                Review secondReview = reviews.get(1);
                assertEquals(2, secondReview.getReviewId());
                assertEquals(2, secondReview.getUserId());
                assertEquals(1, secondReview.getMovieId());
                assertEquals("Sample review text", secondReview.getReviewText());
                assertEquals(8.5, secondReview.getRating());
                assertTrue(secondReview.isSpoiler());
            }

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * getSpoilerFreeReviewsForMovieTest(): This method is annotated with @Test and
     * tests the functionality of the getSpoilerFreeReviewsForMovie() method in the
     * MovieDAO class. It retrieves spoiler-free reviews for a specific movie and
     * asserts that the expected reviews are returned.
     */
    @Test
    public void getSpoilerFreeReviewsForMovieTest() {
        try {

            List<Review> spoilerFreeReviews = MovieDAO.getSpoilerFreeReviewsForMovie(1);

            assertNotNull(spoilerFreeReviews);

            for (Review review : spoilerFreeReviews) {
                assertFalse(review.isSpoiler());
            }

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * getSpoilerFreeReviewsForMovieTest(): This method is annotated with @Test and
     * tests the functionality of the getSpoilerFreeReviewsForMovie() method in the
     * MovieDAO class. It retrieves spoiler-free reviews for a specific movie and
     * asserts that the expected reviews are returned.
     */
    @Test
    public void getAverageRatingForMovieTest() {
        try {

            double averageRating = MovieDAO.getAverageRatingForMovie(1);

            assertEquals(8.25, averageRating);
            // rating2=8.5

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

}