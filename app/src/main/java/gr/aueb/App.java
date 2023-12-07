package gr.aueb;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        
        Scanner sc = new Scanner(System.in);
        File f1 = new File("C:\\Users\\Nick\\api_keys\\tmdb_api_key.txt");
        File f2 = new File("C:\\Users\\Nick\\api_keys\\chat_gpt_key");
        String tmdbApiKey = new String();
        String chatgptApiKey = new String();

        try (BufferedReader br = new BufferedReader(new FileReader(f1))) {
            tmdbApiKey = br.readLine();
        } catch (Exception e) {
            // TODO: handle exception
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f2))) {
            chatgptApiKey = br.readLine();
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("Search for a movie. \n");
        String searchInput = sc.nextLine();        
        ArrayList<Integer> ids = Movie.movieSearch(searchInput, tmdbApiKey);
        System.out.println("\nChoose your title. \n");
        int answer = sc.nextInt();
        Movie.createMovie(ids.get(answer - 1), tmdbApiKey);
        sc.close();

    }
}
