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
        String apiKey = null;

        try (BufferedReader br = new BufferedReader(new FileReader(f1))) {
            apiKey = br.readLine();
        } catch (Exception e) {
            // TODO: handle exception
        }


        System.out.println("Search for a movie. \n");
        String searchInput = sc.nextLine();        
        ArrayList<Integer> ids = Movie.movieSearch(searchInput, apiKey);
        System.out.println("\nChoose your title. \n");
        int answer = sc.nextInt();
        Movie.createMovie(ids.get(answer - 1), apiKey);
        sc.close();

    }
}
