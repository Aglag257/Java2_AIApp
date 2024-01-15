/*
 * Country
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

/**
 * Represents information about streaming providers in different countries.
 * 
 * This class is designed to facilitate access to streaming provider
 * information globally. The class includes lists of providers for free,
 * ad-supported, purchase, flat-rate, and rental services.
 * It also provides a utility method, allCountriesNames, to retrieve a mapping
 * of country codes to names
 * using The Movie Database (TMDb) API.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νίκος Ραγκούσης
 */

public class Country {
    /** List of free streaming providers. */
    @SerializedName("free")
    private ArrayList<Provider> free;

    /** List of ad-supported streaming providers. */
    @SerializedName("ads")
    private ArrayList<Provider> ads;

    /** List of streaming providers for purchase. */
    @SerializedName("buy")
    private ArrayList<Provider> buy;

    /** List of flat-rate streaming providers. */
    @SerializedName("flatrate")
    private ArrayList<Provider> flatrate;

    /** List of streaming providers for rental. */
    @SerializedName("rent")
    private ArrayList<Provider> rent;

    /**
     * Retrieves a mapping of country codes to names using The Movie Database (TMDb)
     * API.
     * 
     * @param apiKey The API key for TMDb authentication.
     * @return A HashMap containing country codes as keys and corresponding names as
     *         values.
     */
    public static HashMap<String, String> allCountriesNames(String apiKey) {
        HashMap<String, String> countries = new HashMap<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/watch/providers/regions?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JsonElement element = JsonParser.parseString(response.body());
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            for (JsonElement countryElement : resultsArray) {
                JsonObject countryObject = countryElement.getAsJsonObject();
                String code = countryObject.get("iso_3166_1").getAsString();
                String name = countryObject.get("english_name").getAsString();

                countries.put(code, name);
            }

        } catch (IOException e) {
            System.err.println("Check your internet connection!");
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * Gets the list of free streaming providers.
     * 
     * @return The list of free streaming providers.
     */
    public ArrayList<Provider> getFree() {
        return free;
    }

    /**
     * Gets the list of ad-supported streaming providers.
     * 
     * @return The list of ad-supported streaming providers.
     */
    public ArrayList<Provider> getAds() {
        return ads;
    }

    /**
     * Gets the list of streaming providers for purchase.
     * 
     * @return The list of streaming providers for purchase.
     */
    public ArrayList<Provider> getBuy() {
        return buy;
    }

    /**
     * Gets the list of flat-rate streaming providers.
     * 
     * @return The list of flat-rate streaming providers.
     */
    public ArrayList<Provider> getFlatrate() {
        return flatrate;
    }

    /**
     * Gets the list of streaming providers for rental.
     * 
     * @return The list of streaming providers for rental.
     */
    public ArrayList<Provider> getRent() {
        return rent;
    }
}
