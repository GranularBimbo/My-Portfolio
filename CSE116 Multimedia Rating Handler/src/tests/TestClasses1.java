package tests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ratings.ProblemSet;
import ratings.Rating;
import ratings.Reviewer;
import ratings.Song;

public class TestClasses1 {
    @Test
    public void testSetRating(){
        Rating one = new Rating("steve", 1);
        int expected = 1;
        assertTrue(one.getRating() == expected);
        one.setRating(5);
        expected = 5;
        assertTrue(one.getRating() == expected);
        one.setRating(6);
        expected = -1;
        assertTrue(one.getRating() == -1);
        one.setRating(-100);
        expected = -1;
        assertTrue(one.getRating() == -1);
        Rating two = new Rating("joe", 6);
        assertTrue(two.getRating() == -1);
        Rating three = new Rating("bob", 0);
        assertTrue(three.getRating() == -1);
    }

    @Test
    public void testRateSong(){
        Reviewer joe = new Reviewer("joe");
        Rating joeRating = joe.rateSong(0);
        int expected = -1;
        String expectedID = "joe";
        assertTrue(joeRating.getRating() == expected);
        assertTrue(joeRating.getReviewerID().equals(expectedID));
        joe.rateSong(6);
        assertTrue(joeRating.getRating() == expected);
        joeRating.setRating(5);
        expected = 5;
        assertTrue(joeRating.getRating() == expected);
        joeRating.setRating(6);
        expected = -1;
        assertTrue(joeRating.getRating() == expected);
        joeRating.setRating(0);
        assertTrue(joeRating.getRating() == expected);
        joeRating.setRating(-100);
        assertTrue(joeRating.getRating() == expected);
        Reviewer steve = new Reviewer("steve");
        Rating steveRating = steve.rateSong(1);
        expected = 1;
        assertTrue(steveRating.getRating() == expected);

    }

    @Test
    public void testSetReviewer(){
        Reviewer bob = new Reviewer("bob");
        bob.setReviewerID("Bobe");
        String expected = "Bobe";
        assertTrue(bob.getReviewerID().equals(expected));
    }

    @Test
    public void testSetSong(){
        Song freezingMoon = new Song("Freezing Moon", "Mayhem", "what");
        freezingMoon.setSongID("how");
        String expected = "how";
        assertTrue(freezingMoon.getSongID().equals(expected));
        freezingMoon.setArtist("Darkthrone");
        expected = "Darkthrone";
        assertTrue(freezingMoon.getArtist().equals(expected));
        freezingMoon.setTitle("Transylvanian Hunger");
        expected = "Transylvanian Hunger";
        assertTrue(freezingMoon.getTitle().equals(expected));
    }
}
