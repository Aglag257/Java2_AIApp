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

public class MovieList {
    private String listType; // Τύπος λίστας (public ή private)
    private final int creatorId;
    private String listName;
    private final int listId; // ID του δημιουργού της λίστας

    public MovieList(String listType, int creatorId, String listName, int listId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.listName = listName;
        this.listId = listId;
    }

    public String getListType() {
        return listType;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getListName() {
        return listName;
    }

    public int getListId() {
        return listId;
    }

    /**
     * Sets the list type for a given user.
     *
     * @param  listType   the type of list to set
     * @param  userId     the ID of the user
     * @return            none
     * @throws Exception  if the user does not have permission to change the list type
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
     * Sets the name of the list if the user is the creator.
     *
     * @param  listName   the new name for the list
     * @param  userId     the ID of the user trying to change the list name
     * @throws Exception  if the user does not have permission to change the list name
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
     * A method to update the list type in the database.
     *
     * @throws Exception   description of the exception
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
     * @throws Exception  if an error occurs during the database update
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
     * Creates a new list of the specified type and name, associated with the given creatorId.
     *
     * @param  listType   the type of the list
     * @param  listName   the name of the list
     * @param  creatorId  the id of the list creator
     * @return           a new MovieList object with the obtained listId
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
                    throw new Exception("List " + listName + " already exists!");
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

            System.out.println("List " + listName + " created successfully!");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // Return a new MovieList object with the obtained listId
        return new MovieList(listType, creatorId, listName, listId);
    }

    /**
     * A method to add a movie to a user's list.
     *
     * @param  movieName  the name of the movie to add
     * @param  movieId    the ID of the movie to add
     * @param  userId     the ID of the user
     * @throws Exception  if an error occurs during the process
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
                System.out.println("\n" + movieName + " added to your list " + listName + "!");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * A description of the entire Java function.
     *
     * @param  paramName	description of parameter
     * @return         	description of return value
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
     * Deletes a list for a given user.
     *
     * @param  userId   the ID of the user
     * @throws Exception  if the list is not found or the user is not the creator
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
     * A method to remove a movie from a list.
     *
     * @param  movieName   the name of the movie to be removed
     * @param  movieId     the unique identifier of the movie
     * @param  userId      the ID of the user requesting the removal
     * @throws Exception   if an error occurs during the removal process
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
     * Retrieves movie lists for a specified user.
     *
     * @param  userId  the ID of the user for whom to retrieve movie lists
     * @return         a list of MovieList objects representing the movie lists for the specified user
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
     * Helper method to check if the user is a follower of the list creator.
     *
     * @param userId     the ID of the user
     * @param creatorId  the ID of the list creator
     * @param con        the database connection
     * @return           true if the user is a follower, false otherwise
     * @throws SQLException if an error occurs while querying the database
     */
    private static boolean isFollower(int userId, int creatorId, Connection con) throws SQLException {
        String followerSql = "SELECT * FROM Followers WHERE followedId=? AND followerId=?";
        try (PreparedStatement followerStmt = con.prepareStatement(followerSql)) {
            followerStmt.setInt(1, creatorId);
            followerStmt.setInt(2, userId);

            try (ResultSet followerRs = followerStmt.executeQuery()) {
                return followerRs.next();
            }
        }
    }

    
    /**
     * Check if the movie list contains a movie with the given name and ID.
     *
     * @param  movieName  the name of the movie to check
     * @param  movieId    the ID of the movie to check
     * @return           true if the movie list contains the movie, false otherwise
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
     * Checks if the given list name exists for the specified user ID.
     *
     * @param  listName  the name of the list to check
     * @param  userId    the ID of the user
     * @return          true if the list name exists for the user, false otherwise
     */
    public static boolean isListNameExists(String listName, int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM List WHERE name=? AND userId=?;";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, listName);
                stmt.setInt(2, userId);

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
