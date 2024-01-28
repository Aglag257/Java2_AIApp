/*
 * MovieList
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of movies.
 * The list includes various attributes including the list type
 * (public or private),
 * the ID of the list creator, the list name, and the unique list ID.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης, Άγγελος Λαγός, Γιώργος Αναγνωστόπουλος
 */
public class MovieList {
    /** The type of the movie list, either public or private. */
    private String listType;

    /** The ID of the user who created the list. */
    private final int creatorId;

    /** The name of the movie list. */
    private String listName;

    /** The unique ID of the movie list. */
    private final int listId;

    /**
     * Constructs a MovieList object with the specified list type, creator ID, list
     * name, and list ID.
     *
     * @param listType  The type of the movie list.
     * @param creatorId The ID of the user who created the list.
     * @param listName  The name of the movie list.
     * @param listId    The unique ID of the movie list.
     */
    public MovieList(String listType, int creatorId, String listName, int listId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.listName = listName;
        this.listId = listId;
    }

    /**
     * Gets the type of the movie list (public or private).
     *
     * @return The type of the movie list.
     */
    public String getListType() {
        return listType;
    }

    /**
     * Gets the ID of the user who created the list.
     *
     * @return The ID of the list creator.
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Gets the name of the movie list.
     *
     * @return The name of the movie list.
     */
    public String getListName() {
        return listName;
    }

    /**
     * Gets the unique ID of the movie list.
     *
     * @return The unique ID of the movie list.
     */
    public int getListId() {
        return listId;
    }

    /**
     * Sets the type of the movie list. Only the creator is allowed to change the
     * list type.
     *
     * @param listType The new type of the movie list.
     * @param userId   The ID of the user making the change.
     * @throws Exception If the user does not have permission to change the list
     *                   type.
     */
    public void setListType(String listType, int userId) throws Exception {
        if (userId == creatorId) {
            this.listType = listType;
            updateListTypeInDatabase();
        } else {
            throw new Exception("User does not have permission to change the list type.");
        }
    }

    /**
     * Sets the name of the movie list. Only the creator is allowed to change the
     * list name.
     *
     * @param listName The new name of the movie list.
     * @param userId   The ID of the user making the change.
     * @throws Exception If the user does not have permission to change the list
     *                   name.
     */
    public void setListName(String listName, int userId) throws Exception {
        if (userId == creatorId) {
            this.listName = listName;
            updateListNameInDatabase();
        } else {
            throw new Exception("User does not have permission to change the list name.");
        }
    }

    /**
     * Updates the list type in the database.
     *
     * @throws Exception If an error occurs while updating the list type in the
     *                   database.
     */
    private void updateListTypeInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE List SET listType = ? WHERE list_id = ?")) {

            stmt.setString(1, listType);
            stmt.setInt(2, listId);
            stmt.executeUpdate();

            System.out.println("List type updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Updates the list name in the database.
     *
     * @throws Exception If an error occurs while updating the list name in the
     *                   database.
     */
    private void updateListNameInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE List SET name = ? WHERE list_id = ?")) {

            stmt.setString(1, listName);
            stmt.setInt(2, listId);
            stmt.executeUpdate();

            System.out.println("List name updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Creates a new movie list and adds it to the database.
     *
     * @param listType  The type of the new movie list.
     * @param listName  The name of the new movie list.
     * @param creatorId The ID of the user creating the list.
     * @return The created MovieList object.
     * @throws Exception If an error occurs during the creation of the movie list.
     */
    public static MovieList createList(String listType, String listName, int creatorId) throws Exception {
        int listId;

        try (DB db = new DB();
                Connection con = db.getConnection()) {

            String query = "SELECT * FROM List WHERE name=? AND userId=?;";
            String insertSql = "INSERT INTO List(listType, name, userId) VALUES (?,?,?)";
            String selectSql = "SELECT list_id FROM List WHERE name=? AND userId=?";

            // Check if the list already exists
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setString(1, listName);
                checkStmt.setInt(2, creatorId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    throw new Exception("List " + listName + " already exists");
                }
            }

            // Insert new list
            try (PreparedStatement insertStmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, listType);
                insertStmt.setString(2, listName);
                insertStmt.setInt(3, creatorId);
                insertStmt.executeUpdate();

                // Retrieve the generated list_id
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        listId = generatedKeys.getInt(1);
                    } else {
                        throw new Exception("Failed to retrieve generated list_id.");
                    }
                }
            }

            System.out.println("List: " + listName + " created successfully");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // Return a new MovieList object with the obtained listId
        return new MovieList(listType, creatorId, listName, listId);
    }

    /**
     * Adds a movie to the movie list.
     *
     * @param movieName The name of the movie to be added.
     * @param movieId   The ID of the movie to be added.
     * @param userId    The ID of the user making the addition.
     * @throws Exception If an error occurs while adding the movie to the list.
     */
    public void addToList(String movieName, int movieId, int userId) throws Exception {
        int listId;

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
            String sql = "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?,?,?);";

            // Check if the user is the creator of the list
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setString(1, listName);
                checkStmt.setInt(2, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new Exception("User is not the creator of the list or the list does not exist.");
                    }
                    listId = rs.getInt("list_id");
                }
            }

            // Insert the movie into the list
            try (PreparedStatement stmt1 = con.prepareStatement(sql)) {
                stmt1.setInt(1, listId);
                stmt1.setString(2, movieName);
                stmt1.setInt(3, movieId);
                stmt1.executeUpdate();
                System.out.println(movieName + " added to your list " + listName);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Gets the movies from the movie list.
     *
     * @return A map containing movie IDs as keys and movie names as values.
     * @throws Exception If an error occurs while retrieving movies from the list.
     */
    public Map<Integer, String> getMoviesFromList() throws Exception {
        Map<Integer, String> movies = new HashMap<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT movieName, movieId FROM MoviesList WHERE list_id=?;";

            // Get movies from the list
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, listId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String movieName = rs.getString("movieName");
                        int movieId = rs.getInt("movieId");
                        movies.put(movieId, movieName);
                    }
                }
            }

            return movies;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Deletes the movie list.
     *
     * @param userId The ID of the user deleting the list.
     * @throws Exception If an error occurs while deleting the list.
     */
    public void deleteList(int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "DELETE FROM list WHERE userId=? AND list_id=?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, listId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("List deleted successfully");
                } else {
                    throw new Exception("List not found or user is not the creator.");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Removes a movie from the movie list.
     *
     * @param movieName The name of the movie to be removed.
     * @param movieId   The ID of the movie to be removed.
     * @param userId    The ID of the user making the removal.
     * @throws Exception If an error occurs while removing the movie from the list.
     */
    public void removeMovie(String movieName, int movieId, int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT userId FROM List WHERE list_id=?;";
            String sql = "DELETE FROM MoviesList WHERE list_id=? AND movieName=? AND movieId=?;";

            // Check if the user is the creator of the list
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setInt(1, listId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("userId") != userId) {
                        throw new Exception("User is not the creator of the list or the list does not exist.");
                    }
                }
            }

            // Remove the movie from the list
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, listId);
                stmt.setString(2, movieName);
                stmt.setInt(3, movieId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Movie deleted successfully");
                } else {
                    throw new Exception("Movie not found in the list.");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Gets a list of movie lists for a specific user.
     *
     * @param userId The ID of the user for whom to retrieve movie lists.
     * @return A list of MovieList objects associated with the user.
     * @throws Exception If an error occurs while retrieving movie lists.
     */
    public static List<MovieList> getMovieLists(int userId) throws Exception {
        List<MovieList> movieLists = new ArrayList<>();
        try (DB db = new DB();
                Connection con = db.getConnection()) {

            String sql = "SELECT * FROM List WHERE listType IN ('public', 'protected') AND userId=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String listType = rs.getString("listType");
                        String listName = rs.getString("name");
                        int listId = rs.getInt("list_id");
                        int creatorId = rs.getInt("userId");

                        // For protected lists, check if the user is a follower of the list creator
                        if (listType.equalsIgnoreCase("protected") && !isFollower(userId, creatorId, con)) {
                            continue; // Skip this list if not a follower
                        }

                        movieLists.add(new MovieList(listType, creatorId, listName, listId));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return movieLists;
    }

    /**
     * Checks if the MovieList contains a movie with the given name or ID.
     *
     * @param movieName The name of the movie to check.
     * @param movieId   The ID of the movie to check.
     * @return True if the MovieList contains the movie, false otherwise.
     * @throws Exception If an error occurs during the database query.
     */
    public boolean containsMovie(String movieName, int movieId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM MoviesList WHERE list_id=? AND (movieName=? OR movieId=?);";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, listId);
                stmt.setString(2, movieName);
                stmt.setInt(3, movieId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    /**
     * Generates a String representation of the MovieList.
     *
     * @return A String representation of the MovieList.
     */
    @Override
    public String toString() {
        return "MovieList{" +
                "listType='" + listType + '\'' +
                ", creatorId=" + creatorId +
                ", listName='" + listName + '\'' +
                ", listId=" + listId +
                '}';
    }
}
