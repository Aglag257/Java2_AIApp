/*
 * Person 
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * Represents details about a person, including their movie credits.
 * This class fetches data from The Movie Database (TMDb) API using a given
 * person ID
 * and API key, providing details such as personal information, movie credits as
 * both
 * a cast member and crew, and associated movie details like titles, release
 * dates, and popularity.
 *
 * @version 1.8 28 January 2024
 * @author Νικόλαος Ραγκούσης
 */
public class Person {
    /** Details about the person. */
    private PersonDetails pd;

    /** Credits of the person, including movies they were part of. */
    private PersonCredits pc;

    /** List of movie IDs associated with the person. */
    private final ArrayList<Integer> movieIds;

    /** List of movie titles associated with the person. */
    private final ArrayList<String> movieTitles;

    /** List of movie release dates associated with the person. */
    private final ArrayList<String> movieDates;

    /**
     * Map of movie IDs to an array of details (title, release date, popularity).
     */
    private final HashMap<Integer, Object[]> movies;

    /** List of movie popularity values associated with the person. */
    private final ArrayList<Float> moviePopularity;

    /**
     * Constructs a Person object with the given ID and API key.
     * 
     * @param id     The ID of the person.
     * @param apikey The API key for authentication.
     */
    public Person(int id, String apikey) {
        Gson gson = new Gson();
        //temporary holds information of person's movies. 
        //Is used to filter double values of movies the person is a part in multiple roles
        movies = new HashMap<>();
        //parallel ArrayLists with person's movies information
        movieIds = new ArrayList<>();
        movieTitles = new ArrayList<>();
        movieDates = new ArrayList<>();
        moviePopularity = new ArrayList<>();

        //details response
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/person/" + id + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response1 = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            //deserialize JSON response
            pd = gson.fromJson(response1.body(), PersonDetails.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            System.exit(1);
        }

        // credits response
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/person/" + id + "/movie_credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response2 = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            //deserialize JSON response
            pc = gson.fromJson(response2.body(), PersonCredits.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            System.exit(1);
        }

        //fill movies HashMap with movies that the person is patt of the cast
            if (pc.getCast() != null) {
            Object[] temp;
            for (Cast c : pc.getCast()) {
                temp = new Object[3];
                temp[0] = c.getTitle();
                temp[1] = c.getRelease_date();
                temp[2] = c.getPopularity();
                movies.put(c.getId(), temp);
            }
        }

        //fill movies HashMap with movies that the person is patt of the crew
        if (pc.getCrew() != null) {
            Object[] temp;
            for (Crew c : pc.getCrew()) {
                temp = new Object[3];
                temp[0] = c.getTitle();
                temp[1] = c.getRelease_date();
                temp[2] = c.getPopularity();
                movies.put(c.getId(), temp);
            }
        }

        //fill peopleId ArrayList with all person's movies
        movieIds.addAll(movies.keySet());

        //fill ArrayLists with person's movies other information
        for (Object[] i : movies.values()) {
            movieTitles.add((String) i[0]);
            movieDates.add((String) i[1]);
            moviePopularity.add((float) i[2]);
        }
    }

    /**
     * Returns a string representation of the person, including details and credits.
     */
    @Override
    public String toString() {
        return "\n\n" + pd.toString() + pc.toString();
    }

    /**
     * Returns a list of movie IDs associated with the person.
     * 
     * @return ArrayList<Integer>.
     */
    public ArrayList<Integer> getMovieIds() {
        return movieIds;
    }

    /**
     * Returns a list of movie titles associated with the person.
     * 
     * @return ArrayList<String>.
     */
    public ArrayList<String> getMovieTitles() {
        return movieTitles;
    }

    /**
     * Returns a list of movie release dates associated with the person.
     * 
     * @return ArrayList<String>.
     */
    public ArrayList<String> getMovieDates() {
        return movieDates;
    }

    /**
     * Returns a list of movie popularity values associated with the person.
     * 
     * @return ArrayList<Float>.
     */
    public ArrayList<Float> getMoviePopularity() {
        return moviePopularity;
    }
}
