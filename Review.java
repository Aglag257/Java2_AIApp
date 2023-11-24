import java.util.Scanner;

public class Review {
    private int reviewID;
    private int userID;
    private int movieID;
    private float userRating;
    private String reviewText;
    private boolean isSpoiler;
    private boolean isDeleted;

//Constructor
    public Review(int reviewID, int userID, int movieID, float userRating, String reviewText, boolean isSpoiler, boolean isDeleted) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.movieID = movieID;
        this.userRating = userRating;
        this.reviewText = "";
        this.isSpoiler = isSpoiler;
        this.isDeleted = false;
    }

//Getter methods
    public int getReviewID() {
        return reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public int getMovieID() {
        return movieID;
    }

    public float getUserRating() {
        return userRating;
    }
    public String getReviewText() {
        return reviewText;
    }
    public boolean getIsSpoiler() {
        return isSpoiler;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }
    
//Setter methods
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    //isDeleted and isSpoiler don't need a Setter method

//Other Methods
    public void giveUserRating() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your personal rating for the movie (0.0 - 10.0):");
        float userInput = scanner.nextFloat();
        userRating = userInput;

        System.out.println("Thank you for your rating!");
        scanner.close();
    }

    public void writeReview() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please write your review:");
        String userInput = scanner.nextLine();
        reviewText = userInput;

        System.out.println("Thank you for your review!");
        scanner.close();
    }

    public void editReview(String reviewText) {
        
    }

    public void checkSpoiler(String reviewText) {
        if () {
            isSpoiler = true;
        } else {
            isSpoiler = false;
        }
    }

    public void deleteReview() {
        this.isDeleted = true;
    }
}
