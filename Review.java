import java.util.Scanner;

public class Review {
    private int reviewID;
    private int userID;
    private int movieID;
    private float userRating;
    private String reviewText;
    private boolean isSpoiler;
    private boolean isDeleted;

    // Constructor
    public Review(int reviewID, int userID, int movieID, float userRating, String reviewText, boolean isSpoiler, boolean isDeleted) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.movieID = movieID;
        this.userRating = userRating;
        this.reviewText = "";
        this.isSpoiler = isSpoiler;
        this.isDeleted = false;
    }

    // Getter methods...
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
    
    // Setter methods...
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

    public void setIsSpoiler(boolean isSpoiler) {
        this.isSpoiler = isSpoiler;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Other Methods...
    public void giveUserRating() {
        Scanner scanner = new Scanner(System.in);
        float userInput;
        do {
            System.out.println("Please enter your personal rating for the movie (0.0 - 10.0):");
            while (!scanner.hasNextFloat()) {
                System.out.println("Invalid input. Please enter a valid rating:");
                scanner.next();
            }
            userInput = scanner.nextFloat();
        } while (userInput < 0.0 || userInput > 10.0);
        setUserRating(userInput);
        System.out.println("Thank you for your rating!");
        scanner.close();
    }

    public void writeReview() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please write your review:");
        String userInput = scanner.nextLine();
        setReviewText(userInput);
        System.out.println("Thank you for your review!");
        scanner.close();
    }

    public void editUserRating() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current Rating: " + userRating);

        String changeRating;
        do {
            System.out.println("Do you want to change your Rating? (yes/no)");
            changeRating = scanner.nextLine();
        } while (!changeRating.equalsIgnoreCase("yes") && !changeRating.equalsIgnoreCase("no"));

        if (changeRating.equalsIgnoreCase("yes")) {
            giveUserRating();
        }

        scanner.close();
    }

    public void editReviewText() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current Review: " + reviewText);

        String changeReview;
        do {
            System.out.println("Do you want to change your review? (yes/no)");
            changeReview = scanner.nextLine();
        } while (!changeReview.equalsIgnoreCase("yes") && !changeReview.equalsIgnoreCase("no"));

        if (changeReview.equalsIgnoreCase("yes")) {
            writeReview();
        }

        scanner.close();
    }

    public void checkSpoiler() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        do {
            System.out.print("Does your review contain spoilers? (yes/no): ");
            answer = scanner.nextLine();
        } while (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no"));

        if (answer.equalsIgnoreCase("yes")) {
            setIsSpoiler(true);
        } else {
            setIsSpoiler(false);
        }
        scanner.close();
    }

    public void deleteReview() {
        setIsDeleted(true);
    }
}