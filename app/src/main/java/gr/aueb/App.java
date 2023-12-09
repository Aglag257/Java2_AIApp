package gr.aueb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static String currentUser;
    private static String tmdbApiKey;
    private static String chatgptApiKey;
    private static String youtubeApiKey; 

    public static void main(String[] args) throws Exception {
        loadApiKeys(); // Load API keys from files

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayStartMenu();

            int startChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (startChoice) {
                case 1:
                    //login
                    break;
                case 2:
                    //sign up
                    break;
                case 3:
                    // Continue as a guest
                    currentUser = "Guest";
                    break;
                case 4:
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

            // If the user is logged in, display the main menu
            if (isLoggedIn()) {
                while (true) {
                    displayMainMenu();

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume the newline character

                    switch (choice) {
                        case 1:
                            getAIRecommendation(scanner);
                            break;
                        case 2:
                            searchForMovie(scanner);
                            break;
                        case 3:
                            if(currentUser != "Guest") {
                                logOut();
                            } else {
                                displayStartMenu();
                                choice = scanner.nextInt();
                                scanner.nextLine(); // consume the newline character
                            }
                            break;
                        case 4:
                            System.out.println("Exiting the application. Goodbye!");
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                }
            }
        }
    }

    private static void loadApiKeys() {
        File tmdbFile = new File("C:\\Users\\Nick\\api_keys\\tmdb_api_key.txt");
        File chatgptFile = new File("C:\\Users\\Nick\\api_keys\\chat_gpt_key.txt");
        File youtubeFile = new File("C:\\Users\\Nick\\api_keys\\youtube_key.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(tmdbFile))) {
            tmdbApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading TMDB API key file.");
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(chatgptFile))) {
            chatgptApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading ChatGPT API key file.");
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(youtubeFile))) {
            youtubeApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading ChatGPT API key file.");
            System.exit(1);
        }
    }

    private static void displayStartMenu() {
        System.out.println("\nStart Menu:");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Continue as a Guest");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Get AI recommendation for a movie");
        System.out.println("2. Search for a movie");
        if(currentUser != "Guest") {
            System.out.println("3. Log Out");
            
        } else {
            System.out.println("3. Login");
        }
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static boolean isLoggedIn() {
        return currentUser != null;
    }

    private static void logOut() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private static void getAIRecommendation(Scanner scanner) throws Exception {
        System.out.println("\nType your preferences for movie recommendations.");
        String userMessage = scanner.nextLine();
        AiRecommandation2.testChatCompletions(userMessage + " (Only movie titles, no description or other movie details, no apologies for your previous responses or things you can't do as an AI.)", chatgptApiKey);
        System.out.println("\nChoose your title");
        scanner.nextInt();
        //Temporary
        Movie.createMovie(157336,tmdbApiKey);

    }

    private static void searchForMovie(Scanner scanner) throws Exception {
        System.out.println("\nType your search. \n");
        String userMessage = scanner.nextLine();
        ArrayList<Integer> ids = Movie.movieSearch(userMessage, tmdbApiKey);
        System.out.println("\nChoose your title. \n");
        int answer = scanner.nextInt();
        Movie.createMovie(ids.get(answer - 1), tmdbApiKey);
    }
}