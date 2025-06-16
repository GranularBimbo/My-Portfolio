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
import ratings.FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class TestFiles {
    @Test
    public void test3Movies(){
        ArrayList<Movie> actualList = FileReader.readMovies("data/3Movies.csv");
        int expectedSize = 3;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
//        String expected1 = "Toy Story";
//        String expected2 = "Jumanji";
//        String expected3 = "Father of the Bride Part II";
//        String actual = actualList.get(0).getTitle();
//        assertTrue(expected1.equals(actual) || expected2.equals(actual) || expected3.equals(actual));
//        actual = actualList.get(1).getTitle();
//        assertTrue(expected1.equals(actual) || expected2.equals(actual) || expected3.equals(actual));
//        actual = actualList.get(2).getTitle();
//        assertTrue(expected1.equals(actual) || expected2.equals(actual) || expected3.equals(actual));
//        expectedSize = 10;
//        actualSize = actualList.get(0).getCast().size();
//        assertEquals(expectedSize, actualSize);
//        expected1 = "Tom Hanks";
//        actual = actualList.get(0).getCast().get(0);
//        assertEquals(expected1, actual);
//        expected1 = "Tim Allen";
//        actual = actualList.get(0).getCast().get(1);
//        assertEquals(expected1, actual);
    }

    @Test
    public void testReadMovies(){
        ArrayList<Movie> actualList = FileReader.readMovies("data/2Movies.csv");
        int expectedSize = 2;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
        String expected1 = "Toy Story";
        String expected2 = "Jumanji";
        String actual = actualList.get(0).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        actual = actualList.get(1).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
//        expectedSize = 10;
//        actualSize = actualList.get(0).getCast().size();
//        assertEquals(expectedSize, actualSize);
//        expected1 = "Tom Hanks";
//        actual = actualList.get(0).getCast().get(0);
//        assertEquals(expected1, actual);
//        expected1 = "Tim Allen";
//        actual = actualList.get(0).getCast().get(1);
//        assertEquals(expected1, actual);
    }

    @Test
    public void testReadSongs(){
        Song heavensDoor = new Song("Knockin' On Heaven's Door", "Guns N' Roses", "4JiEyzf0Md7KEFFGWDDdCr");
        Song giveYouUp = new Song("Never Gonna Give You Up", "Rick Astley", "4cOdK2wGLETKBW3PvgPWqT");
        ArrayList<Song> actualList = FileReader.readSongs("data/2SongRatings.csv");
        int expectedSize = 2;
        int actualSize = actualList.size();
        assertEquals("" + actualSize , expectedSize, actualSize);
        String expected1 = "4JiEyzf0Md7KEFFGWDDdCr";
        String expected2 = "4cOdK2wGLETKBW3PvgPWqT";
        String actual = actualList.get(0).getSongID();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        assertEquals(expectedSize, actualSize);
        actual = actualList.get(1).getSongID();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        double expectedAvgRating = 4.5;
        double actualAvgRating = actualList.get(0).averageRating();
        assertEquals(expectedAvgRating, actualAvgRating, 0.001);
        expected1 = "Guns N' Roses";
        expected2 = "Rick Astley";
        actual = actualList.get(0).getArtist();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        actual = actualList.get(1).getArtist();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        expected1 = "51";
        Rating rating1 = (Rating)actualList.get(0).getRatings().getValue();
        actual = rating1.getReviewerID();
        assertEquals(expected1, actual);
        Rating rating2 = (Rating)actualList.get(1).getRatings().getValue();
        expected1 = "244";
        actual = rating2.getReviewerID();
        assertEquals(expected1, actual);
        expected1 = "Knockin' On Heaven's Door";
        expected2 = "Never Gonna Give You Up";
        actual = actualList.get(0).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        actual = actualList.get(1).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
    }

    @Test
    public void songRatingsInWrongOrder(){
        ArrayList<Song> actualList = FileReader.readSongs("data/2SongRatings.csv");
        int expectedSize = 2;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
        actualSize = actualList.get(0).getRatings().size();
        assertEquals(expectedSize, actualSize);
        int expectedRating = 5;
        Rating rating1 = (Rating)actualList.get(0).getRatings().getValue();
        int actualRating = rating1.getRating();
        assertEquals(expectedRating, actualRating);
        expectedRating = 4;
        Rating rating2 = (Rating)actualList.get(0).getRatings().getNext().getValue();
        actualRating = rating2.getRating();
        assertEquals(expectedRating, actualRating);
    }

    @Test
    public void test3Songs(){
        String expected1 = "Birth of a God";
        String expected2 = "Ditto";
        String expected3 = "MONACO";
        ArrayList<Song> actualList = FileReader.readSongs("data/3Songs.csv");
        int expectedSize = 3;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testReadSongsOneSong(){
        Song hsf = new Song("Hammer Smashed Face", "Cannibal Corpse", "4pFC6tuWErxbO61oFFq3BQ");
        ArrayList<Song> actualList = FileReader.readSongs("data/OneSong.csv");
        int expectedSize = 1;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testReadSongsFakeFile(){
        ArrayList<Song> expectedList = new ArrayList<Song>();
        ArrayList<Song> actualList = FileReader.readSongs("data/poop.csv");
        int expectedSize = 0;
        int actualSize = actualList.size();
        assertEquals(expectedSize, actualSize);
    }
}
