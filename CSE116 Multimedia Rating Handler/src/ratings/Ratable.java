package ratings;

import ratings.datastructures.LinkedListNode;

public class Ratable {
    private String title;

    private LinkedListNode<Rating> firstNode, lastInsertedNode;

    public void removeRatingByReviewer(Reviewer reviewer){
        LinkedListNode current = firstNode, placeholder, last = null;
        LinkedListNode nodeToRemove = null;
        Rating currentRating, nextRating = null;

        while(current != null){
            if(current.getNext() == null){
                lastInsertedNode = current;
                break;
            }
            else
                current = current.getNext();
        }

        Rating firstRating = (Rating)firstNode.getValue();
        Rating lastRating = (Rating)lastInsertedNode.getValue();

        if(reviewer.getReviewerID().equals(firstRating.getReviewerID())){
            firstNode = firstNode.getNext();
        }
        else if(reviewer.getReviewerID().equals(lastRating.getReviewerID())){
            for(current = firstNode; current.getNext() != null; current = current.getNext()){
                if(current != null){
                    if(current.getNext() == lastInsertedNode){
                        current.setNext(null);
                        lastInsertedNode = current;
                        break;
                    }
                }
            }
        }
        else{
            for(current = firstNode; current.getNext() != null; current = current.getNext()){
                currentRating = (Rating)current.getValue();
                if(current.getNext() != null)
                    nextRating = (Rating)current.getNext().getValue();

                // if the current rating was written by the given reviewer
                if(currentRating.getReviewerID().equals(reviewer.getReviewerID())){
                    if(last != null){
                        // sets the previous node's next to this node's next
                        last.setNext(current.getNext());
                        //current = null;
                        break;
                    }
                }
                else if(nextRating.getReviewerID().equals(reviewer.getReviewerID())){
                    //remove next
                    placeholder = current.getNext().getNext();
                    current.setNext(placeholder);
                    break;
                }

                last = current;
            }
        }
    }

    public double averageRating(){
        double avg = 0.0;
        double sum = 0.0;
        double nodesIgnored = 0.0; // how many nodes had an invalid rating
        Rating currRating;
        LinkedListNode<Rating> current = firstNode;

        if(firstNode != null){
            while(current != null){
                currRating = current.getValue();
                if(currRating.getRating() >= 1 && currRating.getRating() <= 5)
                    sum += currRating.getRating(); // adds to the sum if the rating is valid
                else
                    nodesIgnored += 1.0; // adds to the number of ignored nodes if the rating is invalid

                current = current.getNext();
            }

            if(firstNode.size() > 1 && nodesIgnored < firstNode.size())
                avg = (sum/(firstNode.size() - nodesIgnored));
            else if(firstNode.size() == 1)
                avg = firstNode.getValue().getRating();
        }

        return avg;
    }

    public void addRating(Rating rating){
        LinkedListNode<Rating> node = new LinkedListNode<Rating>(rating, null);
        LinkedListNode<Rating> current;

        if(firstNode != null){
            firstNode.append(rating);
        }
        else{
            firstNode = node;
        }
    }

    public double bayesianAverageRating(int numExtra, int rating){
        double avg = 0.0;
        double sum = 0.0;
        double nodesIgnored = 0.0;
        double total = 0.0;
        Rating currRating;
        LinkedListNode<Rating> current = firstNode;

        if(firstNode != null){
            while(current != null){
                currRating = current.getValue();
                if(currRating.getRating() >= 1 && currRating.getRating() <= 5)
                    sum += currRating.getRating(); // adds to the sum if the rating is valid
                else
                    nodesIgnored += 1.0; // adds to the number of ignored nodes if the rating is invalid

                current = current.getNext();
            }

            // adds the extra ratings given by the parameters
            for(int i = 0; i < numExtra; i++){
                sum += rating;
            }

            if(numExtra >= 0 && rating >= 1 && rating <= 5){
                if(firstNode.size() > 1 && nodesIgnored < firstNode.size()){
                    total = ((firstNode.size() - nodesIgnored) + numExtra);
                    avg = sum/total;
                }
                else if(firstNode.size() == 1){
                    total = firstNode.size() + numExtra;
                    avg = sum / total;
                }
            }
        }
        else{ // if the song has no ratings, add the extra ratings
            // adds the extra ratings given by the parameters
            for(int i = 0; i < numExtra; i++){
                sum += rating;
            }

            if(numExtra != 0)
                avg = sum/numExtra;
            else
                avg = sum;
        }

        // returns 0.0 if the entries are invalid
        if(numExtra < 0 || rating < 1 || rating > 5)
            avg = 0.0;

        return avg;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String val){
        this.title = val;
    }

    public LinkedListNode getRatings(){
        return firstNode;
    }

    public void setRatings(LinkedListNode node){
        firstNode = node;
    }
}
