/*
 * Person Details
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents details about a person, including their name, biography, birthday,
 * deathday (if applicable), and ID.
 * This class is designed to hold information retrieved from The Movie Database
 * (TMDb) API related to a specific person.
 * The details encompass key aspects of a person's life, providing a
 * comprehensive snapshot of their background.
 * The information includes the person's full name, biographical details,
 * birthdate, and, if applicable, their death date.
 * The class also encapsulates a unique identifier (ID) associated with the
 * person, facilitating identification and retrieval.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης
 */
public class PersonDetails {
    /** The name of the person. */
    @SerializedName("name")
    private String name;

    /** The biography of the person. */
    @SerializedName("biography")
    private String biography;

    /** The birthday of the person. */
    @SerializedName("birthday")
    private String birthday;

    /** The deathday of the person, if applicable. */
    @SerializedName("deathday")
    private String deathday;

    /** The unique identifier (ID) associated with the person. */
    @SerializedName("id")
    private int id;

    /**
     * Gets the name of the person.
     * 
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique identifier (ID) associated with the person.
     * 
     * @return The ID of the person.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a string representation of the person's details, including name,
     * biography, birthday, and deathday (if applicable).
     * 
     * @return A formatted string representing the person's details.
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        if (this.name != null)
            returnString.append("Name: ").append(this.name);
        if (this.biography != null)
            returnString.append("\n\n").append(this.biography).append("\n\n");
        if (this.birthday != null)
            returnString.append("Birthday: ").append(this.birthday).append("\n");
        if (this.deathday != null) {
            returnString.append("Deathday: ").append(this.name).append("\n\n");
        } else
            returnString.append("\n");
        return returnString.toString();
    }
}
