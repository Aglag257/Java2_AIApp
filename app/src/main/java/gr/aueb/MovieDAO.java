/*
 * MovieDAO
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Movie-related database operations.
 * 
 * The class facilitates interactions with the database concerning movies,
 * particularly focusing on reviews. It handles standard database queries and
 * transactions
 * associated with movies and their reviews.
 * Key operations provided include fetching reviews for a specific movie,
 * retrieving
 * spoiler-free reviews, and calculating the average rating for a movie.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Άγγελος Λαγός
 */

public class MovieDAO {
    /**
     * Retrieves all reviews for a specific movie ID from the database.
     * 
     * @param movieId The ID of the movie.
     * @return A list of reviews for the specified movie.
     * @throws Exception If an error occurs during database interaction.
     */
    public static List<Review> getAllReviewsForMovie(int movieId) throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM Review WHERE movieId = ?")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("reviewId");
                    int userId = rs.getInt("userId");
                    String reviewText = rs.getString("review_text");
                    float rating = rs.getFloat("rating");
                    boolean spoiler = rs.getBoolean("spoiler");

                    Review review = new Review(reviewId, userId, movieId, reviewText, rating, spoiler);
                    reviews.add(review);
                }
            }
        }

        return reviews;
    }

    /**
     * Retrieves spoiler-free reviews for a specific movie ID from the database.
     * 
     * @param movieId The ID of the movie.
     * @return A list of spoiler-free reviews for the specified movie.
     * @throws Exception If an error occurs during database interaction.
     */
    public static List<Review> getSpoilerFreeReviewsForMovie(int movieId) throws Exception {
        List<Review> spoilerFreeReviews = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM Review WHERE movieId = ? AND spoiler = false")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("reviewId");
                    int userId = rs.getInt("userId");
                    String reviewText = rs.getString("review_text");
                    float rating = rs.getFloat("rating");
                    boolean spoiler = rs.getBoolean("spoiler");

                    Review review = new Review(reviewId, userId, movieId, reviewText, rating, spoiler);
                    spoilerFreeReviews.add(review);
                }
            }
        }

        return spoilerFreeReviews;
    }

    /**
     * Calculates the average rating for a specific movie ID based on reviews in the
     * database.
     * 
     * @param movieId The ID of the movie.
     * @return The average rating for the specified movie.
     * @throws Exception If an error occurs during database interaction.
     */
    public static double getAverageRatingForMovie(int movieId) throws Exception {
        double averageRating = 0;
        int totalReviews = 0;
        double totalRating = 0;

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT rating FROM Review WHERE movieId = ?")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    float rating = rs.getFloat("rating");
                    totalRating += rating;
                    totalReviews++;
                }
            }
            if (totalReviews > 0) {
                averageRating = totalRating / totalReviews;
            }
        }

        return averageRating;
    }
}
