package test;

import java.util.Scanner;
import main.Chatroom;
import java.util.List;

public class ChatroomTest {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Create chatroom
            System.out.print("Enter chatroom ID: ");
            int chatroomId = scanner.nextInt();
            
            System.out.print("Enter chatroom name: ");
            String chatroomName = scanner.next();

            System.out.print("Enter creator ID: ");
            int creatorId = scanner.nextInt();

            Chatroom chatroom = new Chatroom(chatroomId, chatroomName, creatorId);
            Chatroom createdChatroom = chatroom.createChatroom();
            System.out.println("Created Chatroom: " + createdChatroom);

            // Update chatroom name
            System.out.print("Enter new chatroom name: ");
            String newChatroomName = scanner.next();
            createdChatroom.setName(newChatroomName);
            System.out.println("Updated Chatroom: " + createdChatroom);

            // Get all chatrooms
            List<Chatroom> chatrooms = Chatroom.getChatrooms();
            System.out.println("All Chatrooms: " + chatrooms);

            // Show chatroom members
            System.out.print("Enter chatroom ID to show members: ");
            int showMembersChatroomId = scanner.nextInt();
            List<String> members = Chatroom.showChatroomMembers(showMembersChatroomId);
            System.out.println("Chatroom Members: " + members);

            // Send message
            System.out.print("Enter user ID to send message: ");
            int senderId = scanner.nextInt();
            
            System.out.print("Enter a message to send: ");
            scanner.nextLine(); 
            String userMessage = scanner.nextLine();

            // Get unseen message
            System.out.print("Enter user ID to get unseen messages: ");
            int userIdForUnseen = scanner.nextInt();
            
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
