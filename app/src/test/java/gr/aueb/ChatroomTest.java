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

        // Κλείσιμο της σύνδεσης
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

    @Test
    public void getRoomIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getRoomId

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

    @Test
    public void getNameTest() throws SQLException {
        // // Έλεγχος της μεθόδου getName

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

    @Test

    public void getCreatorIdTest() throws SQLException {
        // // Έλεγχος της μεθόδου getCreatorId

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

    @Test
    public void testIsChatroomCreator() throws SQLException, Exception {
        // Έλεγχος για την περίπτωση όταν ο χρήστης είναι ο δημιουργός του chatroom
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

    @Test
    public void testIsNotChatroomCreator() throws SQLException, Exception {
        // Έλεγχος για την περίπτωση όταν ο χρήστης δεν είναι ο δημιουργός του chatroom
        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE roomId = ? AND creatorId = ?")) {
            stmt.setInt(1, chatroom.getRoomId());
            stmt.setInt(2, 2); // δεν υπαρχει ο χρηστης 2 /ακομα και αν υπηρχε δεν ειναι ο creator του chatroom
                               // με id = 1
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    assertFalse(rs.getInt(1) > 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testSetNameAsCreator() throws Exception {
        // Προετοιμασία των δεδομένων
        try {
            // Εκτέλεση της setName με τον δημιουργό του chatroom
            chatroom.setName("NewName", 1);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
        // Έλεγχος αν το όνομα του chatroom έχει ενημερωθεί σωστά
        assertEquals("NewName", chatroom.getName());
    }

    @Test
    public void testSetNameAsNonCreator() throws Exception {
        // Προετοιμασία των δεδομένων
        try {
            // Εκτέλεση της setName με μη δημιουργό του chatroom
            chatroom.setName("NewName", 2);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
        // Έλεγχος αν το όνομα του chatroom ΔΕΝ έχει αλλάξει
        assertNotEquals("NewName", chatroom.getName());
    }

    @Test
    public void testIsNameUniqueWhenUniqueTrue() {
        // Τεστ για την περίπτωση όταν το όνομα είναι μοναδικό
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

    @Test
    public void testIsNameUniqueWhenNotUnique() {
        // Τεστ για την περίπτωση όταν το όνομα δεν είναι μοναδικό
        DB db = new DB();
        // Έλεγχος αν το όνομα του chatroom υπάρχει
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

    @Test
    public void testUpdateNameInDatabaseCorrect() throws Exception {
        // αλλαζω το ονομα του chatroom απο τον χρήστη 1 ( εχω ηδη τεσταρει την
        // λειτουργια της setName)
        chatroom.setName("name2", 1);
        DB db = new DB();
        Connection con = db.getConnection();
        // Έλεγχος αν το όνομα του chatroom έχει ενημερωθεί σωστά στη βάση δεδομένων
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

    @Test
    public void testCreateChatroom() throws Exception {
        // Έλεγχος εάν το chatroom δημιουργήθηκε σωστά
        chatroom1 = Chatroom.createChatroom("CreatedChatroomTest", 1);
        assertNotNull(chatroom1);
        assertEquals("CreatedChatroomTest", chatroom1.getName());
        assertEquals(1, chatroom1.getCreatorId());

        // Έλεγχος εάν τα δεδομένα έχουν καταχωρηθεί στη βάση δεδομένων
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

    @Test
    public void testGetChatrooms() {
        try {
            List<Chatroom> chatrooms = Chatroom.getChatrooms();

            // Έλεγχος εάν η λίστα δεν είναι null
            assertNotNull(chatrooms);

            // Έλεγχος εάν το πλήθος των chatrooms είναι σωστό
            assertEquals(1, chatrooms.size());

            // Έλεγχος εάν τα δεδομένα είναι σωστά
            assertEquals("ChatroomTest", chatrooms.get(0).getName());
            assertEquals(1, chatrooms.get(0).getCreatorId());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

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

    @Test
    public void testGetMessages() throws Exception {
        try {

            List<Message> messages = chatroom.getMessages();

            // Έλεγχος του αποτελέσματος
            assertNotNull(messages);
            assertEquals(2, messages.size());

            // Έλεγχος του περιεχομένου του πρώτου μηνύματος
            Message firstMessage = messages.get(0);
            assertEquals("TestText", firstMessage.getText());
            assertFalse(firstMessage.getSpoiler());
            assertEquals("TestUser", firstMessage.getUsername());

            // Έλεγχος του περιεχομένου του δεύτερου μηνύματος
            Message secondMessage = messages.get(1);
            assertEquals("TestText2", secondMessage.getText());
            assertTrue(secondMessage.getSpoiler());
            assertEquals("TestUser2", secondMessage.getUsername());

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

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
