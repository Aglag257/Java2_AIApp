/*
 * Movie
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

/**
 * Represents information about a movie, including credits, details, and
 * availability.
 * The class includes methods to get information about a movie,
 * credits, details, and availability.
 * Connects the application to The Movie Database (TMDb) API using an API key.
 * Utilizes Gson for JSON parsing from TMDb API, making HTTP requests to gather
 * data on movie credits, details, and availability.
 * The credits section covers both cast and crew, revealing key contributors.
 * The details section provides comprehensive movie information, including
 * title, overview, genres, IMDb rating, runtime, release date, directors,
 * writers, and top-billed cast.
 * Fetches availability information on various platforms. IMDb rating is
 * obtained by cross-referencing the IMDb ID from TMDb with an external file.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νικόλαος Ραγκούσης και Γεώργιος Καρανδρέας
 */

public class Movie {
    /** The availability information for the movie. */
    private Availability av;
    /** The contributors (cast and crew) information for the movie. */
    private Contributors co;
    /** The details of the movie. */
    private MovieDetails md;
    /** The IMDb rating of the movie. */
    private final double imdbRating;
    /** The list of people IDs associated with the movie. */
    private final ArrayList<Integer> peopleId;
    /** The list of people names associated with the movie. */
    private final ArrayList<String> peopleName;
    /** The list of people job roles associated with the movie. */
    private final ArrayList<String> peopleJob;
    /**
     * A map associating people IDs with their details (name, job role, popularity).
     */
    private final HashMap<Integer, Object[]> people;
    /** The list of people popularity values associated with the movie. */
    private final ArrayList<Float> peoplePopularity;
    /** The FilmBro rating of the movie. */
    private double filmBroRating;

    /**
     * Constructs a Movie object for the given movie ID using the provided API key.
     * Fetches information about credits, details, and availability from TMDb.
     * 
     * @param id     The ID of the movie.
     * @param apiKey The API key for accessing TMDb.
     * @throws Exception 
     */
    public Movie(int id, String apiKey) throws Exception {
        Gson gson = new Gson();
        //temporary holds information of movie's contributors. 
        //Is used to filter double values of people having multiple roles in the movie
        people = new HashMap<>();
        //parallel ArrayLists with movie's contributors information
        peopleId = new ArrayList<>();
        peopleName = new ArrayList<>();
        peopleJob = new ArrayList<>();
        peoplePopularity = new ArrayList<>();

        // Response for movie credits
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response1;
        try {
            response1 = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //deserialize JSON response
            co = gson.fromJson(response1.body(), Contributors.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            System.exit(1);
        }

        // Response for movie details
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response2;
        try {
            response2 = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //deserialize JSON response
            md = gson.fromJson(response2.body(), MovieDetails.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            System.exit(1);
        }

        // Response for movie availability
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/watch/providers"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response3;
        try {
            response3 = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //deserialize JSON response
            av = gson.fromJson(response3.body(), Availability.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            System.exit(1);
        }

        filmBroRating = MovieDAO.getAverageRatingForMovie(this.getMd().getId());

        imdbRating = getImdbRatingFromID(md.getImdb_id());

        //fill people HashMap with cast members
        if (co.getCast() != null) {
            Object[] temp;
            for (Cast c : co.getCast()) {
                temp = new Object[3];
                temp[0] = c.getName();
                temp[1] = "Actor";
                temp[2] = c.getPopularity();
                people.put(c.getId(), temp);
            }
        }

        //fill people HashMap with crew members
        if (co.getCrew() != null) {
            Object[] temp;
            for (Crew c : co.getCrew()) {
                temp = new Object[3];
                temp[0] = c.getName();
                temp[1] = c.getJob();
                temp[2] = c.getPopularity();
                people.put(c.getId(), temp);
            }
        }

        //fill peopleId ArrayList with all contributor ids
        peopleId.addAll(people.keySet());

        //fill ArrayLists with people's other information
        for (Object[] i : people.values()) {
            peopleName.add((String) i[0]);
            peopleJob.add((String) i[1]);
            peoplePopularity.add((float) i[2]);
        }
    }

    /**
     * Gets the IMDb rating of a movie based on its IMDb ID.
     * 
     * @param imdbID The IMDb ID of the movie.
     * @return The IMDb rating or -1 if not found.
     */
    public static double getImdbRatingFromID(String imdbID) {
        String filePath = "/title.rating.tsv";  // Adjust the path based on your file location in resources
        String line;
        double imdbRating = -1; // Default value if the ID is not found

        try (InputStream is = App.class.getResourceAsStream(filePath);
            InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
            BufferedReader br = new BufferedReader(isr)) {

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length >= 3 && data[0].equals(imdbID)) {
                    imdbRating = Double.parseDouble(data[1]);
                    break;
                }
            }

        } catch (IOException | NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        }

        return imdbRating;
    }

    /**
     * Gets the list of directors for the movie.
     * 
     * @return The list of directors' names.
     */
    private ArrayList<String> getDirectors() {
        ArrayList<String> directors = new ArrayList<>();
        if (this.co.getCrew() != null) {
            for (Crew c : this.co.getCrew()) {
                if (c.getJob().equals("Director")) {
                    directors.add(c.getName());
                }
            }
        }
        return directors;
    }

    /**
     * Gets the list of writers for the movie.
     * 
     * @return The list of writers' names.
     */
    private ArrayList<String> getWriters() {
        ArrayList<String> writers = new ArrayList<>();
        if (this.co.getCrew() != null) {
            for (Crew c : this.co.getCrew()) {
                if (c.getDepartment().equals("Writing")) {
                    writers.add(c.getName());
                }
            }
        }
        return writers;
    }

    /**
     * Gets the list of top-billed actors for the movie.
     * 
     * @return The list of actors' names with their character names.
     */
    private ArrayList<String> getActors() {
        ArrayList<String> actors = new ArrayList<>();
        if (this.co.getCast() != null) {
            if (this.co.getCast().length >= 4) {
                actors.add(this.co.getCast()[0].getName() + " (" + this.co.getCast()[0].getCharacter() + ")");
                actors.add(this.co.getCast()[1].getName() + " (" + this.co.getCast()[1].getCharacter() + ")");
                actors.add(this.co.getCast()[2].getName() + " (" + this.co.getCast()[2].getCharacter() + ")");
                actors.add(this.co.getCast()[3].getName() + " (" + this.co.getCast()[3].getCharacter() + ")");
            } else {
                for (Cast c : this.co.getCast()) {
                    actors.add(c.getName() + " (" + this.co.getCast()[0].getCharacter() + ")");
                }
            }
        }
        return actors;
    }

    /**
     * Generates a string containing detailed information about the movie in user's country
     * specidied by the parameters.
     * 
     * @param countryName the name of the user's country
     * @param countryKey the key of the user's country
     * @return The string with movie details.
     */
    private String printResult(String countryName, String countryKey) {
        StringBuilder returnString = new StringBuilder();
        if (this.md.getOriginal_title() != null) {
            returnString.append("Title: ").append(this.md.getOriginal_title()).append("\n\n");
        }

        if (this.md.getOverview() != null) {
            returnString.append(this.md.getOverview()).append("\n \n");
        }

        if (this.md.getGenres() != null) {
            returnString.append("Genres: ");
            Genre[] genres = this.md.getGenres();
            for (int i = 0; i < genres.length; i++) {
                returnString.append(genres[i]);
                if (i < genres.length - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if (this.filmBroRating != 0.0) {
            returnString.append("FilmBro Rating: ").append(this.getFilmBroRating()).append("\n");
        }

        if (this.getImdbRating() != -1)
            returnString.append("Imdb Rating: ").append(this.getImdbRating()).append("\n");

        if (this.md.getVote_average() != 0.0) {
            returnString.append("Tmdb Rating: ").append(this.getMd().getVote_average()).append("\n\n");
        }

        if (this.md.getRuntime() != 0) {
            returnString.append("Runtime: ").append(this.md.getRuntime()).append("m").append("\n\n");
        }

        if (this.md.getRelease_date() != null) {
            returnString.append("Release Date: ").append(this.md.getRelease_date()).append("\n\n");
        }

        if (getDirectors() != null) {
            returnString.append("Director(s): ");
            for (int i = 0; i < getDirectors().size(); i++) {
                returnString.append(getDirectors().get(i));
                if (i < getDirectors().size() - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if (getWriters() != null) {
            returnString.append("Writer(s): ");
            for (int i = 0; i < getWriters().size(); i++) {
                returnString.append(getWriters().get(i));
                if (i < getWriters().size() - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if (getActors() != null) {
            returnString.append("Top billed cast:\n");
            for (int i = 0; i < getActors().size(); i++) {
                returnString.append(getActors().get(i));
                if (i < getActors().size() - 1) {
                    returnString.append("\n");
                }
            }
            returnString.append("\n\n");
        }
        if(countryKey != null) {
            returnString.append(av.toString(countryName, countryKey));
        } 
        return returnString.toString();
    }

    /**
     * Prints the full cast and crew details for the movie.
     */
    public void printFullCast() {
        StringBuilder ca = new StringBuilder();
        StringBuilder cr = new StringBuilder();

        // Cast
        for (Cast c : this.co.getCast()) {
            ca.append(c);
        }

        // Crew
        for (Crew c : this.co.getCrew()) {
            cr.append(c);
        }

        System.out.println("\n\nCast:");
        System.out.println(ca + "\n");
        System.out.println("Crew:");
        System.out.println(cr + "\n");
    }

    /**
     * Generates a string representation of the Movie object.
     * 
     * @return A string containing movie details.
     */
    public String toString(String countryName, String countryKey) {
        String s = printResult(countryName, countryKey);
        return "\n\n" + s;
    }

    /**
     * Gets the movie details.
     * 
     * @return The movie details.
     */
    public MovieDetails getMd() {
        return md;
    }

    /**
     * Gets the IMDb rating of the movie.
     * 
     * @return The IMDb rating.
     */
    public double getImdbRating() {
        return imdbRating;
    }

    /**
     * Gets the list of people IDs involved in the movie.
     * 
     * @return The list of people IDs.
     */
    public ArrayList<Integer> getPeopleId() {
        return peopleId;
    }

    /**
     * Gets the list of people names involved in the movie.
     * 
     * @return The list of people names.
     */
    public ArrayList<String> getPeopleName() {
        return peopleName;
    }

    /**
     * Gets the list of people job roles involved in the movie.
     * 
     * @return The list of people job roles.
     */
    public ArrayList<String> getPeopleJob() {
        return peopleJob;
    }

    /**
     * Gets a map containing people IDs and corresponding details.
     * 
     * @return The map of people details.
     */
    public HashMap<Integer, Object[]> getPeople() {
        return people;
    }

    /**
     * Gets the FilmBro rating of the movie.
     * 
     * @return The FilmBro rating.
     */
    public double getFilmBroRating() {
        return filmBroRating;
    }

    public void setFilmBroRating(double filmBroRating) {
        this.filmBroRating = filmBroRating;
    }

    /**
     * Gets the list of people popularity scores involved in the movie.
     * 
     * @return The list of people popularity scores.
     */
    public ArrayList<Float> getPeoplePopularity() {
        return peoplePopularity;
    }
}
