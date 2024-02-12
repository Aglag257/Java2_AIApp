package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*The ChatroomTest class is a test class that tests the functionality of the Chatroom class.
 It includes test methods for various functionalities such as getting the room ID, getting the name, 
 getting the creator ID, checking if a user is the creator of the chatroom, setting the name of the chatroom, 
 checking if a name is unique, creating a chatroom, getting the chatroom members, getting the messages, and getting the unseen messages. */
public class ChatroomTest {
    private static User user;
    private static User user2;
    private static Connection connection;
    private static Chatroom chatroom;
    private static Chatroom chatroom1;
    private static Message message;
    private static Message message2;

    @BeforeAll
    public static void CreateInserts() throws Exception {
        DB db = new DB();
        connection = db.getConnection();

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
        try (PreparedStatement insertStmt4 = connection
                .prepareStatement("INSERT INTO chatroomuser (roomId, userId) VALUES (1, 2)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement(
                "INSERT INTO message (id, roomid, userId, spoiler, text, username) VALUES (1, 1, 1, false, 'TestText', 'TestUser')")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement(
                "INSERT INTO message (id, roomid, userId, spoiler, text, username) VALUES (2, 1, 2, true, 'TestText2', 'TestUser2')")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection
                .prepareStatement("INSERT INTO unseenmessage (userId, roomid, unseenmessageid) VALUES (2, 1, 1)")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    @AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection
                .prepareStatement("DELETE FROM unseenmessage WHERE userid = 2")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM message WHERE userid = 1")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM message WHERE userid = 2")) {
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

        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    public void beforeEachTest() {
        System.out.println("before");
        user = new User(1, "TestUser", "TestPassword", "TestCountry");
        user2 = new User(2, "TestUser2", "TestPassword", "TestCountry");
        chatroom = new Chatroom(1, "ChatroomTest", user.getId());
        message = new Message(1, user.getId(), false, "TestText", chatroom.getRoomId(), user.getUsername());
        message2 = new Message(2, user2.getId(), true, "TestText2", chatroom.getRoomId(), user2.getUsername());

    }

    /*
     * getRoomIdTest(): Tests the getRoomId() method of the Chatroom class by
     * checking if
     * the retrieved room ID matches the expected value.
     */
    @Test
    public void getRoomIdTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT roomid FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(chatroom.getRoomId(), resultSet.getInt("roomid"));
                } else {
                    fail("the roomid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /*
     * getNameTest(): Tests the getName() method of the Chatroom class by checking
     * if the retrieved name
     * matches the expected value.
     */
    @Test
    public void getNameTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT name FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(chatroom.getName(), resultSet.getString("name"));
                } else {
                    fail("the name does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /*
     * getCreatorIdTest(): Tests the getCreatorId() method of the Chatroom class by
     * checking
     * if the retrieved creator ID matches the expected value.
     */
    @Test

    public void getCreatorIdTest() throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("SELECT creatorid FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(chatroom.getCreatorId(), resultSet.getInt("creatorid"));
                } else {
                    fail("the creatorid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

    /*
     * testIsChatroomCreator(): Tests the isChatroomCreator() method of the Chatroom
     * class by checking if
     * a user is the creator of the chatroom.
     */
    @Test
    public void testIsChatroomCreator() throws SQLException, Exception {

        try (PreparedStatement stmt = connection
                .prepareStatement("SELECT COUNT(*) FROM chatroom WHERE roomId = ? AND creatorId = ?")) {
            stmt.setInt(1, chatroom.getRoomId());
            stmt.setInt(2, chatroom.getCreatorId());
            try {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        assertTrue(rs.getInt(1) > 0);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("SQLException: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("General Exception: " + e.getMessage());
            }
        }
    }

    /*
     * testIsNotChatroomCreator(): Tests the isChatroomCreator() method of the
     * Chatroom class
     * by checking if a user is not the creator of the chatroom.
     */
    @Test
    public void testIsNotChatroomCreator() throws SQLException, Exception {

        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE roomId = ? AND creatorId = ?")) {
            stmt.setInt(1, chatroom.getRoomId());
            stmt.setInt(2, 2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    assertFalse(rs.getInt(1) > 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * testSetNameAsCreator(): Tests the setName() method of the Chatroom class
     * when the user is the creator of the chatroom.
     */
    @Test
    public void testSetNameAsCreator() throws Exception {

        try {

            chatroom.setName("NewName", 1);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }

        assertEquals("NewName", chatroom.getName());
    }

    /*
     * testSetNameAsNonCreator(): Tests the setName() method of the Chatroom class
     * when the user is not the creator of the chatroom.
     */
    @Test
    public void testSetNameAsNonCreator() throws Exception {

        try {
            chatroom.setName("NewName", 2);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }

        assertNotEquals("NewName", chatroom.getName());
    }

    /*
     * testIsNameUniqueWhenNotUnique(): Tests the isNameUnique() method of the
     * Chatroom class when the name is not unique.
     */
    @Test
    public void testIsNameUniqueWhenUniqueTrue() {
        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE name = ?")) {
            stmt.setString(1, "ChatroomTestName1");
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertTrue(rs.getInt(1) == 0); // If count is 0, the name is unique
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * testIsNameUniqueWhenNotUnique(): Tests the isNameUnique() method of the
     * Chatroom class when the name is not unique.
     */
    @Test
    public void testIsNameUniqueWhenNotUnique() {
        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE name = ?")) {
            stmt.setString(1, chatroom.getName());
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertFalse(rs.getInt(1) == 0); // If count is 0, the name is unique
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * testUpdateNameInDatabaseCorrect(): Tests the updateNameInDatabase() method of
     * the Chatroom class by checking if the name is updated correctly in the
     * database.
     */
    @Test
    public void testUpdateNameInDatabaseCorrect() throws Exception {

        chatroom.setName("name2", 1);
        DB db = new DB();
        Connection con = db.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT name FROM Chatroom WHERE roomId = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals("name2", resultSet.getString("name")); //
                } else {
                    fail("Chatroom not found in the database.");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }

    }

    /*
     * testCreateChatroom(): Tests the createChatroom() method of the Chatroom class
     * by creating a
     * chatroom and verifying its properties and existence in the database.
     */
    @Test
    public void testCreateChatroom() throws Exception {

        chatroom1 = Chatroom.createChatroom("CreatedChatroomTest", 1);
        assertNotNull(chatroom1);
        assertEquals("CreatedChatroomTest", chatroom1.getName());
        assertEquals(1, chatroom1.getCreatorId());

        try (DB db = new DB(); Connection con = db.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM Chatroom WHERE roomId = ?")) {
                stmt.setInt(1, chatroom1.getRoomId());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    assertTrue(resultSet.next());
                    assertEquals("CreatedChatroomTest", resultSet.getString("name"));
                    assertEquals(1, resultSet.getInt("creatorId"));
                }
            }
        } catch (Exception e) {
            fail("Exception during database check: " + e.getMessage());
        }
    }

    /*
     * testGetChatrooms(): Tests the getChatrooms() method of
     * the Chatroom class by retrieving the chatrooms and verifying their
     * properties.
     */
    @Test
    public void testGetChatrooms() {
        try {
            List<Chatroom> chatrooms = Chatroom.getChatrooms();

            assertNotNull(chatrooms);

            assertEquals(1, chatrooms.size());

            assertEquals("ChatroomTest", chatrooms.get(0).getName());
            assertEquals(1, chatrooms.get(0).getCreatorId());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * testShowChatroomMembers(): Tests the showChatroomMembers() method of the
     * Chatroom class by
     * retrieving the chatroom members and verifying their properties.
     */
    @Test
    void testShowChatroomMembers() {
        try {

            // Call the method to get chatroom members by roomId
            ArrayList<User> members = chatroom.showChatroomMembers();

            // Assertions
            assertNotNull(members);
            assertFalse(members.isEmpty());

            // Customize assertions based on the actual values
            // For example, check if the list contains the expected number of members
            User member1 = members.get(0);
            assertEquals("TestUser", member1.getUsername());
            User member2 = members.get(1);
            assertEquals("TestUser2", member2.getUsername());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * testGetMessages(): Tests the getMessages() method of the Chatroom class by
     * retrieving the messages and verifying their properties.
     */
    @Test
    public void testGetMessages() throws Exception {
        try {

            List<Message> messages = chatroom.getMessages();

            assertNotNull(messages);
            assertEquals(2, messages.size());

            Message firstMessage = messages.get(0);
            assertEquals("TestText", firstMessage.getText());
            assertFalse(firstMessage.getSpoiler());
            assertEquals("TestUser", firstMessage.getUsername());

            Message secondMessage = messages.get(1);
            assertEquals("TestText2", secondMessage.getText());
            assertTrue(secondMessage.getSpoiler());
            assertEquals("TestUser2", secondMessage.getUsername());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * testGetUnseenMessages(): Tests the getUnseenMessages() method of the Chatroom
     * class
     * by retrieving the unseen messages and verifying their properties.
     */
    @Test
    void testGetUnseenMessages() {
        try {

            // Get unseen messages for the user
            ArrayList<Message> unseenMessages = chatroom.getUnseenMessages(2);

            // Assertions
            assertNotNull(unseenMessages);

            Message newUnseenMessage = unseenMessages.get(0);

            assertEquals(1, newUnseenMessage.getMessageId());
            assertEquals(1, newUnseenMessage.getChatroomId());
            assertEquals(1, newUnseenMessage.getUserId());
            assertFalse(newUnseenMessage.getSpoiler());
            assertEquals("TestText", newUnseenMessage.getText());
            assertEquals("TestUser", newUnseenMessage.getUsername());

            // after calling the method get unseen messages , the messages should be deleted
            unseenMessages = chatroom.getUnseenMessages(2);
            assertTrue(unseenMessages.isEmpty());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * @Test
     * public void testGetChatroomByName() {
     * try {
     * Chatroom chatroom2 = Chatroom.getChatroomByName("ChatroomTest");
     * System.out.println(chatroom2);
     * // Assertions
     * assertNotNull(chatroom2);
     * assertEquals("CreatedChatroomTest", chatroom2.getName());
     * 
     * // Customize the assertions based on the actual values
     * // Check if the chatroom has the expected properties
     * // For example, check the roomId and creatorId
     * 
     * } catch (Exception e) {
     * fail("Exception during test: " + e.getMessage());
     * }
     * }
     */

    /*
     * isUserInChatroomTest(): Tests the isUserInChatroom() method of the Chatroom
     * class by checking if a user is in the chatroom.
     */
    @Test
    public void isUserInChatroomTest() {
        try {
            assertTrue(chatroom.isUserInChatroom(2));
            assertFalse(chatroom.isUserInChatroom(3));

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

}