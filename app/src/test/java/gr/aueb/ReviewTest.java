package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*The ReviewTest class is a JUnit test class that tests the functionality of the Review class. It includes test methods for various getters and setters of the Review class, as well as methods to add and delete reviews from the database. */
public class ReviewTest {
    private static User user;
    private static Movie movie;
    private static String tmdbApiKey;
    private static Connection connection;
    private static Review review;
    private static Review reviewDelete;
    private static Review review3;
    static File tmdbFile = new File("c:/Users/Βασιλης/OneDrive/Υπολογιστής/apiKeys/tmdb_api_key.txt");
    private static Date d;

    /*
     * CreateInserts(): Sets up the necessary objects and inserts test data into the
     * database before running the tests.
     */
    @BeforeAll
    public static void CreateInserts() throws Exception {
        DB db = new DB();
        connection = db.getConnection();
        user = new User(1000, "TestUser", "TestPassword", "Greece");

        reviewDelete = new Review(1, 1000, 1, "Sample review text", 8, false, "TestUser", d = new Date(), "Movie");

        review = new Review(150, 1000, 389, "bad movie", 9, false, "TestUser", d = new Date(), "Movie");
        try (BufferedReader br = new BufferedReader(new FileReader(tmdbFile))) {
            tmdbApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading tmdb API key file.");
            System.exit(1);
        }
        movie = new Movie(389, tmdbApiKey);
        try (PreparedStatement insertStmt1 = connection.prepareStatement(
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1000, 'TestUser', 'TestPassword', 'Greece')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement(
                "INSERT INTO review (reviewId, userId, movieId, review_text, rating, spoiler, username, date, movieName) VALUES (1, 1000, 1, 'Sample review text', 8, false,'TestUser','2024-01-19','Movie')")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement(
                "INSERT INTO review (reviewId, userId, movieId, review_text, rating, spoiler, username, date ,  movieName) VALUES (150, 1000, 389, 'bad movie', 9, false, 'TestUser', '2024-01-19' , 'Movie')")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    /*
     * DeleteReviewTestInsert(): Deletes the test data from the database after
     * running the tests.
     */
    @AfterAll
    public static void DeleteReviewTestInsert() throws SQLException {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM review WHERE userid = 1000")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1000")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        if (connection != null) {
            connection.close();
        }
    }

    /*
     * DeleteReviewTestInsert(): Deletes the test data from the database after
     * running the tests.
     */
    @Test
    public void getReviewIdTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT reviewid FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(review.getReviewId(), resultSet.getInt("reviewid"));
                } else {
                    fail("the reviewid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* getUserIdTest(): Tests the getUserId() method of the Review class. */
    @Test
    public void getUserIdTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(review.getUserId(), resultSet.getInt("userid"));
                } else {
                    fail("the userId does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* getMovieIdTest(): Tests the getMovieId() method of the Review class. */
    @Test
    public void getMovieIdTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT movieid FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(review.getMovieId(), resultSet.getInt("movieid"));
                } else {
                    fail("the movieId does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* getReviewTextTest(): Tests the getReviewText() method of the Review class. */
    @Test
    public void getReviewTextTest() throws SQLException {

        try (PreparedStatement stmt = connection
                .prepareStatement("SELECT review_text FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(review.getReviewText(), resultSet.getString("review_text"));
                } else {
                    fail("the review_text does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* getRatingTest(): Tests the getRating() method of the Review class. */
    @Test
    public void getRatingTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT rating FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(review.getRating(), resultSet.getFloat("rating"));
                } else {
                    fail("the rating does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* getSpoilerTest(): Tests the isSpoiler() method of the Review class. */
    @Test
    public void getSpoilerTest() throws SQLException {

        boolean t = false;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM review WHERE reviewId = 150")) {
            try (ResultSet resultSet = stmt.executeQuery()) {

                if (resultSet.next()) {
                    if (resultSet.getInt("spoiler") == 0) {
                        t = false;
                        assertEquals(review.isSpoiler(), t);
                    } else {
                        t = true;
                        assertEquals(review.isSpoiler(), t);
                    }
                } else {
                    fail("the spoiler does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /* addReviewTest(): Tests the addReview() method of the Review class. */
    @Test
    public void addReviewTest() throws Exception {
        review3 = Review.addReview(1000, 389, "Added test review", 9, false, user.getUsername(), "Movie");
        try {

            assertNotNull(review3);
            assertEquals(movie.getMd().getId(), review3.getMovieId());
            assertEquals(9, review3.getRating());
            assertEquals(false, review3.isSpoiler());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /* setReviewTextTest(): Tests the setReviewText() method of the Review class. */
    @Test
    public void setReviewTextTest() throws Exception {

        String newReviewText = "Okey robert de niro isnt that bad";
        review.setReviewText(newReviewText, review.getUserId());

        assertEquals(newReviewText, review.getReviewText());
    }

    /*
     * updateReviewTextInDatabaseTest(): Tests the update of the review text in the
     * database.
     */
    @Test
    public void updateReviewTextInDatabaseTest() throws Exception {

        String newReviewText = "THE BEST MOVIE EVER! Robert de niro rules";
        review.setReviewText(newReviewText, review.getUserId());
        try (PreparedStatement stmt = connection
                .prepareStatement("SELECT review_text FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(newReviewText, resultSet.getString("review_text"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    /* setRatingTest(): Tests the setRating() method of the Review class. */
    @Test
    public void setRatingTest() throws Exception {

        float newRating = 9.9f;
        review.setRating(newRating, review.getUserId());

        assertEquals(newRating, review.getRating());
    }

    /*
     * updateRatingInDatabaseTest(): Tests the update of the rating in the database.
     */
    @Test
    public void updateRatingInDatabaseTest() throws Exception {

        float newRating = 8.7f;
        review.setRating(newRating, review.getUserId());
        try (PreparedStatement stmt = connection.prepareStatement("SELECT rating FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(newRating, resultSet.getFloat("rating"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    /* setSpoilerTest(): Tests the setSpoiler() method of the Review class. */
    @Test
    public void setSpoilerTest() throws Exception {

        boolean newSpoiler = true;
        review.setSpoiler(newSpoiler, review.getUserId());

        assertEquals(newSpoiler, review.isSpoiler());
    }

    /*
     * updateSpoilerInDatabaseTest(): Tests the update of the spoiler flag in the
     * database.
     */
    @Test
    public void updateSpoilerInDatabaseTest() throws Exception {

        boolean newSpoiler = false;
        review.setSpoiler(newSpoiler, review.getUserId());
        try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(0, resultSet.getInt("spoiler"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    /* deleteReviewTest(): Tests the deleteReview() method of the Review class. */
    @Test
    public void deleteReviewTest() throws Exception {

        reviewDelete.deleteReview(reviewDelete.getUserId());

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Review WHERE reviewId = ?")) {
            stmt.setInt(1, reviewDelete.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                assertFalse(resultSet.next(), "Review still exists in the database");
            }
        }
    }

    /*
     * getReviewsByUserAndMovieTest(): Tests the getReviewsByUserAndMovie() method
     * of the Review class
     */
    @Test
    public void getReviewsByUserAndMovieTest() throws Exception {

        List<Review> retrievedReviews = Review.getReviewsByUserAndMovie(user.getId(), movie.getMd().getId());

        assertTrue(retrievedReviews.stream().anyMatch(r -> r.getReviewId() == review.getReviewId()));
    }

    /*
     * getReviewsByUserAndMovieNoMatchTest(): Tests the getReviewsByUserAndMovie()
     * method of the Review class when there are no matching reviews.
     */
    @Test
    public void getReviewsByUserAndMovieNoMatchTest() throws Exception {

        List<Review> retrievedReviews = Review.getReviewsByUserAndMovie(user.getId(), 999);

        assertTrue(retrievedReviews.isEmpty());
    }
}