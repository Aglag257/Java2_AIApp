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

<<<<<<< Updated upstream
=======
    private static boolean mainCase2(Scanner scanner) throws Exception {
        int choice = 0;
        int choice2;
        System.out.println("\nType your search or press 0 to retun to main menu ");
        String userMessage = scanner.nextLine();
        userMessage = encodeMovieTitle(userMessage);
        
        if(!userMessage.equals("0")) {
            do {
                ArrayList<Integer> ids = search(userMessage);
                if(!ids.isEmpty()) {
                    Object o = pick(scanner, ids);
                    if(!o.equals(0)){
                        System.out.println(o);
                        do {
                            System.out.println(o);
                            if(o instanceof Movie) {
                                displayMovieMenu();
                                // check for input
                                choice2 = scanner.nextInt();
                                scanner.nextLine();
                                movieCase(scanner, choice2, o);
                            } else {
                                displayPersonMenu();
                                choice2 = scanner.nextInt();
                                scanner.nextLine();
                                personCase(scanner, choice2, o);
                            }                       
                        } while (choice2 != 0);
                    } else break;
                }       
            } while(choice == 0);
        } else return false;
        return true;
    }

    public static void personCase(Scanner scanner, int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1: 
                ArrayList<Integer> ids2 = ((Person)o).getMovieIds();
                ArrayList<String> titles = ((Person)o).getMovieTitles();
                ArrayList<String> dates = ((Person)o).getMovieDates();
                int choice3;
                int choice4 = 1;
                do {
                    for (int i = 0; i < ids2.size(); i++) {
                        if(!dates.get(i).isEmpty()) {
                            int year = extractYear(dates.get(i));
                            System.out.printf("%2d. %s (%d)\n", i + 1, titles.get(i), year);
                        } else {
                            System.out.printf("%2d. %s (%s)\n", i + 1, titles.get(i), "Release date not available");
                        }
                    }   
                    Object ob = pick(scanner, ids2);
                    if(ob instanceof Movie) {
                        Movie m = (Movie)ob;
                        System.out.println(m);
                        do {
                            displayMovieMenu();
                            choice3 = scanner.nextInt();
                            scanner.nextLine();
                            movieCase(scanner, choice3, ob);
                        } while (choice3 != 0);
                    } else choice4 = 0;
                } while(choice4 != 0 );
                
                
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    }

    public static void movieCase(Scanner scanner , int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1: 
                int choice3;
                do {
                    Movie m = (Movie)o;
                    m.printFullCast();
                    displayFullContributorsMenu();
                    choice3 = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice3) {
                        case 0:
                            break;
                        case 1: 
                            ArrayList<String> names = ((Movie)o).getPeopleName();
                            ArrayList<String> jobs = ((Movie)o).getPeopleJob();
                            ArrayList<Integer> originalIds = ((Movie) o).getPeopleId();
                            ArrayList<Integer> ids2 = new ArrayList<>();
                            for (Integer id : originalIds) { //negative values for prick()
                                ids2.add(-id);
                            }
                            int choice4;
                            int choice5 = 1;
                            do {
                                for (int i = 0; i < ids2.size(); i++) {
                                    if(!jobs.get(i).isEmpty()) {
                                        System.out.printf("%2d. %s (%s)\n", i + 1, names.get(i), jobs.get(i));
                                    } else {
                                        System.out.printf("%2d. %s (%s)\n", i + 1, jobs.get(i), "Known for department not available");
                                    }
                                }   
                                Object ob = pick(scanner, ids2);
                                if(ob instanceof Person) {
                                    Person p = (Person)ob;
                                    System.out.println(p);
                                    do {
                                        displayPersonMenu();
                                        choice4 = scanner.nextInt();
                                        scanner.nextLine();
                                        personCase(scanner, choice4, ob);
                                    } while (choice4 != 0);
                                } else choice5 = 0;
                            } while(choice5 != 0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option");
                            break;
                    }
                }while(choice3 != 0);
                break;
            case 8:
                 printBonusContent("Pulp Fiction" ,1995 ); 
            break;   
            default:
                System.out.println("choice2 " + choice2);
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    } 

>>>>>>> Stashed changes
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
        AiRecommendation2.testChatCompletions(userMessage + " (Only movie titles, no description or other movie details, no apologies for your previous responses or things you can't do as an AI.)", chatgptApiKey);
        System.out.println("\nChoose your title");
        scanner.nextInt();

    }

    private static void searchForMovie(Scanner scanner) throws Exception {
        System.out.println("\nType your search. \n");
        String userMessage = scanner.nextLine();
        ArrayList<?> ids = Movie.movieSearch(userMessage, tmdbApiKey, "id");
        ArrayList<?> titles = Movie.movieSearch(userMessage, tmdbApiKey, "title");
        ArrayList<?> years = Movie.movieSearch(userMessage, tmdbApiKey, "year");
        System.out.println("\nChoose your title. \n");
        int answer = scanner.nextInt();
        Movie m = new Movie((int)ids.get(answer - 1), tmdbApiKey);
        System.out.println(m);
        System.out.println("\nDo you want bonus content for your movie? (yes/no)");
        scanner.nextLine(); // consume the newline character
        String bonusContentChoice = scanner.nextLine();
        if (bonusContentChoice.equals("yes")) {
            String title = (String) titles.get(answer - 1);
            int year = (int) years.get(answer - 1);
            printBonusContent(title, year);
        }
}
    public static void printBonusContent(String movieTitle, int year) {
        if(year != -1) {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Interviews", youtubeApiKey);
        } else{
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Interviews", youtubeApiKey);
        }
    }
}