import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MovieRatings {
    public static void main(String[] args) {
        String apiKey = ""; // το κλειδι Api που έχω δημιουργήσει
        String movieId = ""; // το id της ταινίας που θέλουμε
        HttpURLConnection connection = null;
        try {
            
            URL url = new URL("https://imdb-api.com/en/API/Ratings/" + apiKey + "/" + movieId);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            String c = null;
            StringBuilder content = new StringBuilder();
            while ((c = in.readLine()) != null) {
                content.append(c);
                content.append("\r");
            }
            in.close();

            String result = content.toString();
            
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            String title = jsonObject.get("title").getAsString();
            String ratings = jsonObject.get("imDb").getAsString();
            System.out.println("Movie Title: " + title);
            System.out.println("Movie Ratings: " + ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
