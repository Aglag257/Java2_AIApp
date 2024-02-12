package gr.aueb;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class User {
    private final int id;
    private String username;
    private String password;
    private String country;

    public User(int id, String username, String password, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.country = country;
    }
 

    /**
     * Sets the username to a new value after verifying the current password.
     *
     * @param  newUsername      the new username to be set
     * @param  currentPassword   the current password for verification
     * @throws Exception        if an error occurs during the process
     */
    public void setUsername(String newUsername, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updateUsername(newUsername, currentPassword);
    }

    /**
     * Sets a new password after verifying the current password.
     *
     * @param  newPassword      the new password to be set
     * @param  currentPassword  the current password for verification
     * @throws Exception       if an error occurs during password verification or update
     */
    public void setPassword(String newPassword, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updatePassword(newPassword, currentPassword);
    }

    /**
     * Sets the country for the user after verifying the current password.
     *
     * @param  newCountry      the new country to set for the user
     * @param  currentPassword the current password for verification
     * @throws Exception       if an error occurs during the process
     */
    public void setCountry(String newCountry, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updateCountry(newCountry, currentPassword);
    }    
    /**
     * Update the username  in the appuser table for the given user ID.
     *
     * @param  newUsername      the new username to be updated
     * @param  currentPassword  the current password for verification
     * @return                  void
     * @throws Exception        if there is an error updating the username
     */
    private void updateUsername(String newUsername, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET username = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newUsername);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.username = newUsername;
                System.out.println("Username updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Update the user's password with the new password.
     *
     * @param  newPassword      the new password to be set
     * @param  currentPassword  the current password for verification
     * @return                 no return value
     */
    private void updatePassword(String newPassword, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET pass_word = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newPassword);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.password = newPassword;
                System.out.println("Password updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Updates the country of the app user in the database.
     *
     * @param  newCountry        the new country to be set
     * @param  currentPassword   the current password for authentication
     * @return                  does not return anything
     */
    private void updateCountry(String newCountry, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET country = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newCountry);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.country = newCountry;
                System.out.println("Country updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Verifies the entered password.
     *
     * @param  enteredPassword  the password entered by the user
     * @throws Exception       if the entered password is incorrect
     */
    private void verifyPassword(String enteredPassword) throws Exception {
        if (!enteredPassword.equals(this.password)) {
            throw new Exception("Incorrect password. Operation aborted.");
        }
    }

    /**
     * A method to retrieve the ID.
     *
     * @return         	the ID
     */
    public int getId() {
        return id;
    }

    /**
     * A method to retrieve the username.
     *
     * @return         	the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * A method to retrieve the password.
     *
     * @return         	the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * A description of the entire Java function.
     *
     * @return         description of return value
     */
    public String getCountry() {
        return country;
    }

    /**
     * A method to log in a user with the given username and password.
     *
     * @param  username  the username of the user
     * @param  password  the password of the user
     * @return          the User object if login is successful, otherwise null
     */
    public static User login(String username, String password) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT * FROM AppUser WHERE username=? AND pass_word=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int userId = rs.getInt("userId");
            String country = rs.getString("country");

            return new User(userId, username, password, country);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Registers a new user with the given username, password, and country.
     *
     * @param  username   the username of the user
     * @param  password   the password of the user
     * @param  country    the country of the user
     * @return            the newly registered User object
     * @throws Exception  if an error occurs during the registration process
     */
    public static User register(String username, String password, String country) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?");
                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO AppUser (username, pass_word, country) VALUES(?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt3 = con.prepareStatement(
                        "INSERT INTO List (listType, name, userId) VALUES('private', 'favorites', ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt4 = con.prepareStatement(
                        "INSERT INTO List (listType, name, userId) VALUES('private', 'watchlist', ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, username);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                throw new Exception("Sorry, username already registered");
            }

            stmt2.setString(1, username);
            stmt2.setString(2, password);
            stmt2.setString(3, country);
            stmt2.executeUpdate();

            ResultSet generatedKeys = stmt2.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Create "favorites" list for the user
                stmt3.setInt(1, userId);
                stmt3.executeUpdate();

                // Create "watchlist" list for the user
                stmt4.setInt(1, userId);
                stmt4.executeUpdate();

                return new User(userId, username, password, country);
            } else {
                throw new Exception("Failed to retrieve generated userId.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Adds a movie to the user's favorites list in the database.
     *
     * @param  movieId    the unique identifier of the movie
     * @param  movieName  the name of the movie
     * @return            throws an exception if the movie is already in favorites, or if the user's 'favorites' list is not found
     */
    public void addToFavorites(int movieId, String movieName) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name='favorites'");
                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?, ?, ?)")) {

            stmt1.setInt(1, this.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                int listId = rs.getInt("list_id");

                // Check if the movie is already in favorites
                if (isMovieInList(listId, movieId)) {
                    throw new Exception("Movie is already in favorites.");
                }

                // Add the movie to the "favorites" list
                stmt2.setInt(1, listId);
                stmt2.setString(2, movieName);
                stmt2.setInt(3, movieId);
                stmt2.executeUpdate();

                System.out.println("\nMovie added to your favorites!");
            } else {
                throw new Exception("User's 'favorites' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    /**
     * Removes a movie from the user's favorites list.
     *
     * @param  movieId   the ID of the movie to be removed from favorites
     * @throws Exception if there's an error removing the movie from favorites
     */
    public void removeFromFavorites(int movieId) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name='favorites'");
                PreparedStatement stmt2 = con
                        .prepareStatement("DELETE FROM MoviesList WHERE list_id=? AND movieId=?")) {

            stmt1.setInt(1, this.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                int listId = rs.getInt("list_id");

                // Check if the movie is in favorites before removing
                if (!isMovieInList(listId, movieId)) {
                    throw new Exception("Movie is not in favorites.");
                }

                // Remove the movie from the "favorites" list
                stmt2.setInt(1, listId);
                stmt2.setInt(2, movieId);
                stmt2.executeUpdate();

                System.out.println("\nMovie removed from your favorites!");
            } else {
                throw new Exception("User's 'favorites' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Adds a movie to the user's watchlist.
     *
     * @param  movieId    the ID of the movie to be added
     * @param  movieName  the name of the movie to be added
     * @throws Exception  if an error occurs during the process
     */
    public void addToWatchlist(int movieId, String movieName) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name='watchlist'");
                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?, ?, ?)")) {

            stmt1.setInt(1, this.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                int listId = rs.getInt("list_id");

                // Check if the movie is already in watchlist
                if (isMovieInList(listId, movieId)) {
                    throw new Exception("Movie is already in watchlist.");
                }

                // Add the movie to the "watchlist" list
                stmt2.setInt(1, listId);
                stmt2.setString(2, movieName);
                stmt2.setInt(3, movieId);
                stmt2.executeUpdate();

                System.out.println("\nMovie added to your watchlist!");
            } else {
                throw new Exception("User's 'watchlist' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Removes a movie from the user's watchlist.
     *
     * @param  movieId   the ID of the movie to be removed from the watchlist
     * @return          throws an Exception if there's an error during the removal process
     */
    public void removeFromWatchlist(int movieId) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name='watchlist'");
                PreparedStatement stmt2 = con
                        .prepareStatement("DELETE FROM MoviesList WHERE list_id=? AND movieId=?")) {

            stmt1.setInt(1, this.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                int listId = rs.getInt("list_id");

                // Check if the movie is in watchlist before removing
                if (!isMovieInList(listId, movieId)) {
                    throw new Exception("Movie is not in watchlist.");
                }

                // Remove the movie from the "watchlist" list
                stmt2.setInt(1, listId);
                stmt2.setInt(2, movieId);
                stmt2.executeUpdate();

                System.out.println("\nMovie removed from your watchlist!");
            } else {
                throw new Exception("User's 'watchlist' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Checks if a movie with the given ID is in the favorites list.
     *
     * @param  movieId  the ID of the movie to check
     * @return          true if the movie is in the favorites list, false otherwise
     */
    public boolean isMovieInFavorites(int movieId) throws Exception {
        return isMovieInList(getListId("favorites"), movieId);
    }

    /**
     * Checks if a movie is in the watchlist.
     *
     * @param  movieId   the ID of the movie to check
     * @return          true if the movie is in the watchlist, otherwise false
     */
    public boolean isMovieInWatchlist(int movieId) throws Exception {
        return isMovieInList(getListId("watchlist"), movieId);
    }

    /**
     * Checks if a movie is in a given list.
     *
     * @param  listId   the ID of the movie list
     * @param  movieId  the ID of the movie
     * @return          true if the movie is in the list; false otherwise
     */
    private boolean isMovieInList(int listId, int movieId) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT 1 FROM MoviesList WHERE list_id=? AND movieId=?")) {

            stmt.setInt(1, listId);
            stmt.setInt(2, movieId);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If rs.next() is true, the movie is in the list; otherwise, it's not.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the ID of the list based on the list name.
     *
     * @param  listName   the name of the list
     * @return            the ID of the list
     */
    private int getListId(String listName) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name=?")) {

            stmt.setInt(1, this.getId());
            stmt.setString(2, listName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("list_id");
            } else {
                throw new Exception("User's '" + listName + "' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Method to follow a user in the system.
     *
     * @param  follow_user   the username of the user to follow
     * @throws Exception    if there are issues with the database or if the user does not exist
     */
    public void followUser(String follow_user) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?")) {

            stmt1.setString(1, follow_user);
            ResultSet rs = stmt1.executeQuery();

            if (!rs.next()) {
                throw new Exception("There is no user with username: " + follow_user);
            }

            int follow_id = rs.getInt("userId");

            try (PreparedStatement stmt2 = con
                    .prepareStatement("INSERT INTO Followers (followedId, followerId) VALUES(?, ?)")) {
                stmt2.setInt(1, follow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("\nYou now follow " + follow_user + "!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to unfollow a user.
     *
     * @param  unfollow_user   the username of the user to unfollow
     * @throws Exception       if there is an error unfollowing the user
     */
    public void unfollowUser(String unfollow_user) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?")) {

            stmt1.setString(1, unfollow_user);
            ResultSet rs = stmt1.executeQuery();

            if (!rs.next()) {
                throw new Exception("There is no user with username: " + unfollow_user);
            }

            int unfollow_id = rs.getInt("userId");

            try (PreparedStatement stmt2 = con
                    .prepareStatement("DELETE FROM Followers WHERE followedId = ? AND followerId = ?")) {
                stmt2.setInt(1, unfollow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("\nYou unfollowed " + unfollow_user + "!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of users that the current user is following.
     *
     * @throws Exception   if an error occurs while retrieving the list of followings
     * @return             the list of users that the current user is following
     */
    public ArrayList<User> getFollowing() throws Exception {
        ArrayList<User> followings = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser JOIN Followers ON " +
                        "Followers.followedId = AppUser.userId " +
                        "WHERE followerId=?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                followings.add(user);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return followings;
    }

    /**
     * Retrieves the followers of the user.
     *
     * @return         	an ArrayList of User objects representing the followers
     */
    public ArrayList<User> getFollowers() throws Exception {
        ArrayList<User> followers = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser JOIN Followers ON " +
                        "Followers.followerId = AppUser.userId " +
                        "WHERE followedId=?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                followers.add(user);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return followers;
    }

    /**
     * Deletes a user from a chatroom.
     *
     * @param  chatroomId   the ID of the chatroom
     * @return         	void
     */
    public void leaveChatroom(int chatroomId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "DELETE FROM ChatroomUser WHERE roomId=? AND userId=?;";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, chatroomId);
                stmt.setInt(2, this.id); // Assuming id is the user's ID
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Join a chatroom with the given chatroom ID.
     *
     * @param  chatroomId the ID of the chatroom to join
     * @throws Exception  if an error occurs while joining the chatroom
     */
    public void joinChatroom(int chatroomId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String insertSql = "INSERT INTO ChatroomUser VALUES(?,?);";

            try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
                stmt.setInt(1, chatroomId);
                stmt.setInt(2, this.id); // Assuming id is the user's ID
                stmt.executeUpdate();
                System.out.println("\nYou can now send messages in the chatroom!\n");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Retrieves the list of chatrooms that the user has joined.
     *
     * @return         	ArrayList of Chatroom objects representing the joined chatrooms
     */
    public ArrayList<Chatroom> getJoinedChatrooms() throws Exception {
        ArrayList<Chatroom> joinedChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT Chatroom.* " +
                        "FROM Chatroom " +
                        "JOIN ChatroomUser ON Chatroom.roomId = ChatroomUser.roomId " +
                        "WHERE ChatroomUser.userId = ?")) {
            stmt.setInt(1, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    joinedChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return joinedChatrooms;
    }

        
    /**
     * Retrieves the list of chatrooms that the user has not joined.
     * Gets also called to check if the user can access the messages of a chatroom in app.
     *
     * @return         the list of chatrooms not joined by the user
     * @throws Exception  if an error occurs while retrieving the chatrooms
     */
    public ArrayList<Chatroom> getNotJoinedChatrooms() throws Exception {
        ArrayList<Chatroom> notJoinedChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * " +
                        "FROM Chatroom " +
                        "WHERE roomId NOT IN " +
                        "(SELECT roomId FROM ChatroomUser WHERE userId = ?) " +
                        "AND creatorId <> ?")) {
            stmt.setInt(1, this.id);
            stmt.setInt(2, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    notJoinedChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return notJoinedChatrooms;
    }

    /**
     * Retrieves the list of chatrooms created by the user.
     *
     * @return         	the list of created chatrooms
     */
    public List<Chatroom> getCreatedChatrooms() throws Exception {
        List<Chatroom> createdChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM Chatroom WHERE creatorId = ?")) {
            stmt.setInt(1, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    createdChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return createdChatrooms;
    }

    /**
     * Retrieves the movie lists from the database for a specific user.
     *
     * @return         an ArrayList of MovieList objects containing the user's movie lists
     * @throws Exception  if an error occurs while retrieving the movie lists
     */
    public ArrayList<MovieList> getLists() throws Exception {
        ArrayList<MovieList> movieLists = new ArrayList<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT * FROM List WHERE userId=?;";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String listType = rs.getString("listType");
                        String listName = rs.getString("name");
                        int listId = rs.getInt("list_id");
                        int creatorId = rs.getInt("userId");

                        // Create and add MovieList object
                        MovieList movieList = new MovieList(listType, creatorId, listName, listId);
                        movieLists.add(movieList);
                    }
                }
            }

            return movieLists;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * A method to check if a username exists in the database.
     *
     * @param  username   the username to check
     * @return           true if the username exists, false otherwise
     */
    public static boolean doesUsernameExist(String username) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT username FROM AppUser WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If rs.next() is true, the username already exists; otherwise, it doesn't.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves users with partial username from the database.
     *
     * @param  partialUsername    the partial username to search for
     * @return                   the list of users with matching partial usernames
     */
    public static ArrayList<User> getUsersWithPartialUsername(String partialUsername) {
        ArrayList<User> users = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser WHERE username LIKE ?")) {

            stmt.setString(1, "%" + partialUsername + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Checks if the current user is following another user.
     *
     * @param  otherUser  the user to check if the current user is following
     * @return           true if the current user follows the other user; otherwise, false
     */
    public boolean isFollowing(User otherUser) {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT 1 FROM Followers WHERE followerId = ? AND followedId = ?")) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, otherUser.getId());

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If rs.next() is true, the current user follows the other user; otherwise,
                              // they don't

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    /**
     * Retrieves all user reviews ordered by movie ID.
     *
     * @return         	list of user reviews ordered by movie ID
     */
    public ArrayList<Review> getAllUserReviewsOrderedByMovieId() throws Exception {
        ArrayList<Review> userReviews = new ArrayList<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT Review.reviewId, Review.movieId, Review.review_Text, Review.rating, Review.spoiler, " +
                    "AppUser.username, Review.date, Review.movieName " +
                    "FROM Review " +
                    "JOIN AppUser ON Review.userId = AppUser.userId " + 
                    "WHERE Review.userId=? " +
                    "ORDER BY Review.movieId;";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int reviewId = rs.getInt("reviewId");
                        int movieId = rs.getInt("movieId");
                        String reviewText = rs.getString("review_text");
                        float rating = rs.getFloat("rating");
                        boolean spoiler = rs.getBoolean("spoiler");
                        String username = rs.getString("username");
                        Timestamp date = rs.getTimestamp("date");
                        String movieName = rs.getString("movieName");

                        Review review = new Review(reviewId, id, movieId, reviewText, rating, spoiler,
                                username, date, movieName);
                        userReviews.add(review);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return userReviews;
    }

    /**
     * Deletes a chatroom and its associated data from the database.
     *
     * @param  chatroomId   the ID of the chatroom to be deleted
     * @return         		void
     */
    public void deleteChatroom(int chatroomId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {

            // Checks if the user is the creator of the chatroom
            String checkCreatorSql = "SELECT 1 FROM Chatroom WHERE roomId=? AND creatorId=?";
            try (PreparedStatement checkCreatorStmt = con.prepareStatement(checkCreatorSql)) {
                checkCreatorStmt.setInt(1, chatroomId);
                checkCreatorStmt.setInt(2, this.id);
                ResultSet rs = checkCreatorStmt.executeQuery();

                if (!rs.next()) {
                    throw new Exception("You are not the creator of the chatroom or the chatroom doesn't exist.");
                }
            }

            // Delete every member of the chatroom from ChatroomUser table
            String deleteChatroomUserSql = "DELETE FROM ChatroomUser WHERE roomId=?";
            try (PreparedStatement deleteChatroomUserStmt = con.prepareStatement(deleteChatroomUserSql)) {
                deleteChatroomUserStmt.setInt(1, chatroomId);
                deleteChatroomUserStmt.executeUpdate();
            }

            // Delete chatroom from Chatroom table
            String deleteChatroomSql = "DELETE FROM Chatroom WHERE roomId=?";
            try (PreparedStatement deleteChatroomStmt = con.prepareStatement(deleteChatroomSql)) {
                deleteChatroomStmt.setInt(1, chatroomId);
                deleteChatroomStmt.executeUpdate();
                System.out.println("\nChatroom deleted successfully!");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Returns a string representation of the User object, including id, username, and country.
     *
     * @return a string representation of the User object
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
