package ratings;

public class Rating {
    private String reviewerID;
    private int rating;

    public Rating(String reviewerID, int rating){
        this.reviewerID = reviewerID;

        if(rating >= 1 && rating <=5) // sets rating to -1 if it is not between 1-5
            this.rating = rating;
        else
            this.rating = -1;
    }


    // Getters and setters
    public String getReviewerID(){
        return reviewerID;
    }

    public void setReviewerID(String val){
        reviewerID = val;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int val){
        if(val >= 1 && val <= 5) // sets rating to -1 if it is not between 1-5
            this.rating = val;
        else
            this.rating = -1;
    }
}
