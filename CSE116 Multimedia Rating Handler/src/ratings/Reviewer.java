package ratings;

public class Reviewer {
    private String ID;

    public Reviewer(String ID){
        this.ID = ID;
    }

    public Rating rateSong(int rating){
        Rating newRating = new Rating(ID, rating);
        return newRating;
    }

    // Getters and setters
    public String getReviewerID(){
        return ID;
    }

    public void setReviewerID(String ID){
        this.ID = ID;
    }
}
