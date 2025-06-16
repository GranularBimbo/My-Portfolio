package tests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ratings.ProblemSet;
import ratings.Rating;
import ratings.Reviewer;
import ratings.Song;
import ratings.Movie;
import ratings.datastructures.LinkedListNode;
import ratings.datastructures.SongBayesianRatingComparator;
import ratings.datastructures.SongTitleComparator;

import java.util.ArrayList;
import java.util.LinkedList;

public class TestClasses2 {
    @Test
    public void testSongBayesianRatingComparator(){
        SongBayesianRatingComparator comparator = new SongBayesianRatingComparator();
        Song song1 = new Song("song1", "band1", "ID1");
        Song song2 = new Song("song2", "band2", "ID2");
        Rating song1Rating = new Rating("joe", 1);
        Rating song1Rating2 = new Rating("bob", 1);
        Rating song2Rating = new Rating("steve", 2);
        Rating song2Rating2 = new Rating("alex", 2);
        song1.addRating(song1Rating);
        song1.addRating(song1Rating2);
        song2.addRating(song2Rating);
        song2.addRating(song2Rating2);
        boolean expected = false;
        boolean actual = comparator.compare(song1, song2);
        assertTrue(expected == actual);
        actual = comparator.compare(song1, song1);
        assertTrue(expected == actual);
        expected = true;
        actual = comparator.compare(song2, song1);
        assertTrue(expected == actual);
    }

    @Test
    public void testSongTitleComparator(){
        SongTitleComparator comparator = new SongTitleComparator();
        Song bees = new Song("Bees", "band1", "song1ID");
        Song ants = new Song("Ants", "band2", "song2ID");
        boolean expected = false;
        boolean actual = comparator.compare(bees, ants);
        assertTrue(expected == actual);
        expected = true;
        actual = comparator.compare(ants, bees);
        assertTrue(expected == actual);
        Song bandits = new Song("Bandits", "band3", "song3ID");
        expected = false;
        actual = comparator.compare(bandits, ants);
        assertTrue(expected == actual);
        Song aants = new Song("Aants", "aantsBand", "aantsID");
        expected = true;
        actual = comparator.compare(aants, ants);
        assertTrue(expected == actual);
        Song acts = new Song("Acts", "actsBand", "actsID");
        expected = false;
        actual = comparator.compare(ants, acts);
        assertTrue(expected == actual);
        actual = comparator.compare(ants, ants);
        assertTrue(expected == actual);
    }

    @Test
    public void testGetTitle(){
        Movie movie1 = new Movie("Movie 1", new ArrayList<String>());
        String expected = "Movie 1";
        String actual = movie1.getTitle();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetCast(){
        ArrayList<String> movieCast = new ArrayList<String>();
        movieCast.add("Steven Tyler");
        movieCast.add("Owen Wilson");
        movieCast.add("Vince Vaughn");
        Movie castMovie = new Movie("Cast Movie", movieCast);
        String expected = "sTeVeN tYlEr";
        String actual = castMovie.getCast().get(0);
        assertTrue(expected.equalsIgnoreCase(actual));
        expected = "oWeN wIlSoN";
        actual = castMovie.getCast().get(1);
        assertTrue(expected.equalsIgnoreCase(actual));
        expected = "vInCe vAuGhN";
        actual = castMovie.getCast().get(castMovie.getCast().size()-1);
        assertTrue(expected.equalsIgnoreCase(actual));
    }

    @Test
    public void testGetCast2(){
        ArrayList<String> movieCast = new ArrayList<String>();
        movieCast.add("Owen Wilson");
        Movie movie = new Movie("movie", movieCast);
        String expected = "Owen Wilson";
        String actual = movie.getCast().get(0);
        assertTrue(expected.equalsIgnoreCase(actual));
    }

    @Test
    public void testBayesianNoRatings(){
        Song song = new Song("song", "band", "ID");
        double expectedAvg = 3.0;
        double actualAvg = song.bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
        expectedAvg = 0.0;
        actualAvg = song.bayesianAverageRating(0, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testBayesianAverageSong(){
        Song song = new Song("song", "band", "ID");
        double expectedAvg = 0.0;
        double actualAvg = song.bayesianAverageRating(0,0);
        assertEquals(expectedAvg, actualAvg, 0.001);
        Rating songRating1 = new Rating("joe", 5);
        song.addRating(songRating1);
        actualAvg = song.bayesianAverageRating(-3, 2);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = song.bayesianAverageRating(2, 6);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = song.bayesianAverageRating(2, 0);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = song.bayesianAverageRating(2, -100);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testBayesianAverageSong2(){
        Song song = new Song("song", "band", "ID");
        Rating songRating1 = new Rating("joe", 5);
        song.addRating(songRating1);
        double expectedAvg = 11.0/3.0; // 3.666666 something like that
        double actualAvg = song.bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
        expectedAvg = 5.0;
        actualAvg = song.bayesianAverageRating(0, 5);
        assertEquals(expectedAvg, actualAvg, 0.001);
        Rating songRating2 = new Rating("bob", 3);
        Rating songRating3 = new Rating("steve", 4);
        song.addRating(songRating2);
        song.addRating(songRating3);
        expectedAvg = 3.2;
        actualAvg = song.bayesianAverageRating(2, 2);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testBayesianAverageMovie(){
        Movie movie = new Movie("movie", new ArrayList<String>());
        double expectedAvg = 0.0;
        double actualAvg = movie.bayesianAverageRating(0,0);
        assertEquals(expectedAvg, actualAvg, 0.001);
        Rating songRating1 = new Rating("joe", 5);
        movie.addRating(songRating1);
        actualAvg = movie.bayesianAverageRating(-3, 2);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = movie.bayesianAverageRating(2, 6);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = movie.bayesianAverageRating(2, 0);
        assertEquals(expectedAvg, actualAvg, 0.001);
        actualAvg = movie.bayesianAverageRating(2, -100);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testBayesianAverageMovie2(){
        Movie movie = new Movie("movie", new ArrayList<String>());
        Rating songRating1 = new Rating("joe", 5);
        movie.addRating(songRating1);
        double expectedAvg = 11.0/3.0; // 3.666666 something like that
        double actualAvg = movie.bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
        expectedAvg = 5.0;
        actualAvg = movie.bayesianAverageRating(0, 5);
        assertEquals(expectedAvg, actualAvg, 0.001);
        Rating songRating2 = new Rating("bob", 3);
        Rating songRating3 = new Rating("steve", 4);
        movie.addRating(songRating2);
        movie.addRating(songRating3);
        expectedAvg = 3.2;
        actualAvg = movie.bayesianAverageRating(2, 2);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testAddRatings(){
        Movie beeMovie = new Movie("Bee Movie", new ArrayList<String>());
        Rating beeRating = new Rating("Joe", 5);
        beeMovie.addRating(beeRating);
        String expected = "Joe";
        Rating answerRating = (Rating)beeMovie.getRatings().getValue();
        String answer = answerRating.getReviewerID();
        assertTrue(answer.equals(expected));
        Rating beeRating2 = new Rating("Bob", 1);
        LinkedListNode<Rating> bobNode = new LinkedListNode<Rating>(beeRating2, null);
        beeMovie.addRating(bobNode.getValue());
        expected = "Bob";
        answerRating = (Rating)beeMovie.getRatings().getNext().getValue();
        answer = answerRating.getReviewerID();
        assertTrue("Expected: " + expected + " Actual: " + answer , expected.equals(answer));
        Rating beeRating3 = new Rating("Steve", 6);
        beeMovie.addRating(beeRating3);
        int expectedSize = 3;
        int actualSize = beeMovie.getRatings().size();
        assertTrue("" + actualSize , actualSize == expectedSize);
        expected = "Steve";
        answerRating = (Rating)beeMovie.getRatings().getNext().getNext().getValue();
        answer = answerRating.getReviewerID();
        assertTrue(answer.equals(expected));
        Rating negRating = new Rating("Stupid Joe", -100);
        beeMovie.addRating(negRating);
        expectedSize = 4;
        actualSize = beeMovie.getRatings().size();
        assertTrue(expectedSize == actualSize);
        expected = "Stupid Joe";
        Rating actualRating = (Rating)beeMovie.getRatings().getNext().getNext().getNext().getValue();
        String actual = actualRating.getReviewerID();
        assertTrue(expected.equals(actual));
        Rating nullRating = null;
        beeMovie.addRating(nullRating);
        expectedSize = 5;
        actualSize = beeMovie.getRatings().size();
        assertTrue("" + actualSize,expectedSize == actualSize);
    }
}
