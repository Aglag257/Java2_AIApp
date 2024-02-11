package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private static Connection connection;
    private static Message message;
    private static Message message2;
    private static User user;

    @BeforeAll
    public static void CreateInserts() throws Exception {
        DB db = new DB();
        connection = db.getConnection();
        message = new Message(1, 1, false, "TestMessage", 1, "TestUser");
        try (PreparedStatement insertStmt1 = connection.prepareStatement(
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'TestUser', 'TestPassword', 'TestCountry')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement(
                "INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'TestUser2', 'TestPassword', 'TestCountry')")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        try (PreparedStatement insertStmt3 = connection
                .prepareStatement("INSERT INTO chatroom (roomId, name, creatorId) VALUES (1, 'ChatroomTest', 1)")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection
                .prepareStatement("INSERT INTO chatroomuser (roomId, userId) VALUES (1, 1)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection
                .prepareStatement("INSERT INTO chatroomuser (roomId, userId) VALUES (1, 2)")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement(
                "INSERT INTO message (id,roomId, userId,spoiler,text,username) VALUES (1, 1, 1, false, 'TestMessage','TestUser')")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

    }

    @AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM message WHERE id = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM message WHERE id = 2")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM message WHERE id = 5000")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM chatroomuser WHERE userid = 1")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement("DELETE FROM chatroomuser WHERE userid = 2")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement("DELETE FROM chatroom WHERE creatorid = 1")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt8 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
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
    public void getMessageIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getMessageId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getMessageId(), resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getUserIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getUserId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getUserId(), resultSet.getInt("userid"));
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getSpoilerTest() throws SQLException {
        boolean t = false;
        // // Έλεγχος της μεθόδου getSpoiler
        try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt("spoiler") == 0) {
                        t = false;
                        assertEquals(message.getSpoiler(), t);
                    } else {
                        t = true;
                        assertEquals(message.getSpoiler(), t);
                    }
                } else {
                    fail("the spoiler does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getTextTest() throws SQLException {
        // // Έλεγχος της μεθόδου getText
        try (PreparedStatement stmt = connection.prepareStatement("SELECT text FROM message WHERE Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getText(), resultSet.getString("text"));
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getChatroomIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getRoomId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT roomid FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getChatroomId(), resultSet.getInt("roomid"));
                } else {
                    fail("the Roomid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void getUsernameTest() throws SQLException {
        // // Έλεγχος της μεθόδου getUsername
        try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM message WHERE Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getUsername(), resultSet.getString("username"));
                } else {
                    fail("the username does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    @Test
    public void addMessageTest() {
        try {
            // Add a new message
            message2 = Message.addMessage(2, false, "New test message", 1, "TestUser2");

            // Query the database to check if the message was inserted
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE id = ?")) {
                stmt.setInt(1, message2.getMessageId());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        // Verify that the inserted message has correct attributes
                        assertEquals(2, resultSet.getInt("userId"));
                        assertFalse(resultSet.getBoolean("spoiler"));
                        assertEquals("New test message", resultSet.getString("text"));
                        assertEquals(1, resultSet.getInt("roomId"));
                        assertEquals("TestUser2", resultSet.getString("username"));
                    } else {
                        fail("Newly added message not found in the database.");
                    }
                } catch (SQLException e) {
                    fail("Exception thrown during select: " + e.getMessage());
                }
            } catch (SQLException e) {
                fail("Exception thrown during preparation of select statement: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Exception thrown during addMessageTest: " + e.getMessage());
        }
    }

    public void unseenMessageTest() {
        try {
            // Query the database to check if the useenmessage was inserted
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM unseenmessage WHERE id = ?")) {
                stmt.setInt(1, message2.getMessageId());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        // Verify that the inserted message has correct attributes
                        assertEquals(1, resultSet.getInt("userId"));
                        assertEquals(1, resultSet.getInt("roomId"));
                        assertEquals(message2.getMessageId(), resultSet.getInt("UnSeenMessageId"));
                    } else {
                        fail("Newly added message not found in the database.");
                    }
                } catch (SQLException e) {
                    fail("Exception thrown during select: " + e.getMessage());
                }
            } catch (SQLException e) {
                fail("Exception thrown during preparation of select statement: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Exception thrown during addMessageTest: " + e.getMessage());
        }
    }

    @Test
    public void deleteMessageTest() {
        try {
            // Attempt to delete the message
            message.deleteMessage(1);

            // Query the database to check if the message was deleted
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE id = ?")) {
                stmt.setInt(1, message.getMessageId());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    assertFalse(resultSet.next(), "Message should have been deleted from the database.");
                } catch (SQLException e) {
                    fail("Exception thrown during select: " + e.getMessage());
                }
            } catch (SQLException e) {
                fail("Exception thrown during preparation of select statement: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Exception thrown during deleteMessageTest: " + e.getMessage());
        }
    }

    @Test
    public void deleteOtherUserMessageTest() {
        // Create a second message by another user
        user = new User(1, "TestUser", "Password", "TestCountry");
        message2 = new Message(5000, 2, false, "Other user's message", 1, "OtherUser");

        // Attempt to delete the other user's message by passing the current user's ID
        // (1)
        try {
            message2.deleteMessage(1);
            fail("Expected Exception to be thrown when user tries to delete another user's message");
        } catch (Exception e) {
            // Verify that the correct exception is thrown
            assertEquals("User does not have permission to delete this message.", e.getMessage());
        }
    }
}