package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieListTest {
    private static Connection connection;
    private static MovieList movielist;

    @BeforeEach
    public void CreateInserts() throws Exception {
        DB db = new DB();
        connection = db.getConnection();
        movielist = new MovieList("public", 1, "list1", 1);
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
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (3, 'User3', 'TestPassword', 'TestCountry')")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (1, 'public', 'list1', 1)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (2, 'protected', 'list2', 1)")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (3, 'private', 'list3', 1)")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (4, 'protected', 'list4', 2)")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt8 = connection
                .prepareStatement("INSERT INTO followers (followedid,followerid) VALUES (1, 2)")) {
            insertStmt8.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt9 = connection
                .prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (1, 'movie1',  1)")) {
            insertStmt9.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt10 = connection
                .prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (2, 'movie1',  2)")) {
            insertStmt10.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt11 = connection
                .prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (3, 'movie1',  3)")) {
            insertStmt11.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt1 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (50, 'public', 'list1', 3)")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement(
                "INSERT INTO list (list_Id, listType, name, userid) VALUES (51, 'protected', 'list2', 3)")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    @AfterEach
    public void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM movieslist WHERE movieid = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM list WHERE userid = 1")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM list WHERE userid = 2")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM list WHERE userid = 3")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection
                .prepareStatement("DELETE FROM followers WHERE followerId = 2")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection
                .prepareStatement("DELETE FROM followers WHERE followerId = 1")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt8 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 3")) {
            insertStmt8.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void getListTypeTest() throws SQLException {
        // // Έλεγχος της μεθόδου getListTypeTest
        try (PreparedStatement stmt = connection.prepareStatement("SELECT listType FROM list WHERE list_id = 1")) {
            System.out.println(movielist.getListType());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println(movielist.getListType());
                    assertEquals(movielist.getListType(), resultSet.getString("listType"));
                } else {
                    fail("the listType does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getCreatorIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getCreatorId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getCreatorId(), resultSet.getInt("userid"));
                } else {
                    fail("the userid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getListNameTest() throws SQLException {
        // // Έλεγχος της μεθόδου getListName
        try (PreparedStatement stmt = connection.prepareStatement("SELECT name FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getListName(), resultSet.getString("name"));
                } else {
                    fail("the name does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getListIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getCreatorId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT list_id FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getListId(), resultSet.getInt("list_id"));
                } else {
                    fail("the listid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateList() {
        try {
            MovieList newList = MovieList.createList("public", "Test List", 1);

            // Assert
            assertNotNull(newList);
            assertEquals("public", newList.getListType());
            assertEquals("Test List", newList.getListName());
            assertEquals(1, newList.getCreatorId());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetMoviesFromList() {
        try {
            // Arrange
            String movieName1 = "Movie 1";
            int movieId1 = 5;
            String movieName2 = "Movie 2";
            int movieId2 = 6;

            // Add movies to the list
            movielist.addToList(movieName1, movieId1, movielist.getCreatorId());
            movielist.addToList(movieName2, movieId2, movielist.getCreatorId());

            // Act
            Map<Integer, String> movies = movielist.getMoviesFromList();

            // Assert
            assertNotNull(movies);
            assertEquals(3, movies.size());
            assertTrue(movies.containsKey(movieId1));
            assertEquals(movieName1, movies.get(movieId1));
            assertTrue(movies.containsKey(movieId2));
            assertEquals(movieName2, movies.get(movieId2));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteList() {
        try {
            // Arrange: Create a new list
            MovieList newList = MovieList.createList("public", "Test List", 3);

            // Act: Call deleteList method
            movielist.deleteList(3);
            // Assert: Check if the list is deleted
            fail("Expected an Exception to be thrown.");
            // Assert: Check if the list is deleted
        } catch (Exception e) {
            assertEquals("List not found or user is not the creator.", e.getMessage());
        }
    }

    @Test
    public void testRemoveMovie() {
        try {
            // Arrange: Add a movie to the list
            movielist.addToList("Avengers: Endgame", 10, 1);

            // Act: Remove the movie from the list
            movielist.removeMovie("Avengers: Endgame", 10, 1);

            // Assert: Check if the movie is removed from the list
            assertFalse(movielist.containsMovie("Avengers: Endgame", 10), "Movie should be removed from the list");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetMovieListsWhenNotFollower() {
        try {
            // Act: Retrieve movie lists for user 1
            List<MovieList> user3Lists = MovieList.getMovieLists(3);

            // Assert: Check if the correct list is retrieved , since he has 1 public, 1
            // protected, 1 private and i am searching without having followed the user 1
            assertEquals(1, user3Lists.size(), "User 3 should have 1 movie lists");

            // Assert: Check if the retrieved lists contain the expected list names
            List<String> listNames = user3Lists.stream().map(MovieList::getListName).collect(Collectors.toList());
            assertTrue(listNames.contains("list1"), "User 3 should have list1");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM list WHERE userid = 1")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

    }

    @Test
    public void testGetMovieListsWhenFollower() {
        try (PreparedStatement insertStmt1 = connection
                .prepareStatement("INSERT INTO followers (followedid,followerid) VALUES (3, 3)")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try {
            // Act: Retrieve movie lists for user 1
            List<MovieList> user3Lists = MovieList.getMovieLists(3);

            // Assert: Check if the correct lists are retrieved , since he has 1 public, 1
            // protected, 1 private and i am searching having followed the user 1
            assertEquals(2, user3Lists.size(), "User 3 should have 2 movie lists");

            // Assert: Check if the retrieved lists contain the expected list names
            List<String> listNames = user3Lists.stream().map(MovieList::getListName).collect(Collectors.toList());
            assertTrue(listNames.contains("list1"), "User 3 should have list1");
            assertTrue(listNames.contains("list2"), "User 3 should have list1");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM list WHERE userid = 1")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection
                .prepareStatement("DELETE FROM followers WHERE followerId = 3")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());

        }
    }

    @Test
    public void testContainsMovie() {
        try {

            // Act & Assert
            assertTrue(movielist.containsMovie("movie1", 1),
                    "Movie should be present in the list");

            // Assert: Check for a movie that doesn't exist
            assertFalse(movielist.containsMovie("movie2", 2),
                    "Movie should not be present in the list");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsListNameExists() {
        try {

            // Act & Assert
            assertTrue(MovieList.isListNameExists("list1", 1),
                    "List name should exist for the specified user");

            // Assert: Check for a non-existing list name
            assertFalse(MovieList.isListNameExists("Non-existing List", 3),
                    "List name should not exist for the specified user");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}