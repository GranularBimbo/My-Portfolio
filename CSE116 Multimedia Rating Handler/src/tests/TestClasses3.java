package tests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ratings.*;
import ratings.datastructures.LinkedListNode;
import ratings.datastructures.SongBayesianRatingComparator;
import ratings.datastructures.SongTitleComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class TestClasses3 {
    @Test
    public void TestTopKAvg(){
        MediaLibrary ml = new MediaLibrary();
        ml.populateLibrary("data/3Songs.csv", "data/moviesForRatingTest.csv", "data/3MovieRatings.csv");
//        int expectedSize = 3;
//        int actualSize = ml.songs.size();
//        assertEquals(expectedSize, actualSize);
//        expectedSize = 2;
//        actualSize = ml.movies.size();
//        assertEquals(expectedSize, actualSize);
//        actualSize = ml.ratedMovies.size();
//        assertEquals(expectedSize, actualSize);
        ArrayList<Ratable> topRatings = ml.topKRatables(10);
        int expectedSize = 5;
        int actualSize = topRatings.size();
        assertEquals(expectedSize, actualSize);
        double expectedAvg = 3.75;
        double actualAvg = topRatings.get(0).bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
        topRatings = ml.topKRatables(2);
        expectedSize = 2;
        actualSize = topRatings.size();
        assertEquals(expectedSize, actualSize);
        actualAvg = topRatings.get(0).bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
        expectedAvg = 11.0/3.0;
        actualAvg = topRatings.get(1).bayesianAverageRating(2, 3);
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void TestUnratedMovie(){
        ArrayList<Movie> movies = FileReader.readMovies("data/moviesWithUnratedMovie.csv");
        ArrayList<Movie> actualMovies = FileReader.readMovieRatings(movies, "data/3MovieRatings.csv");
        int expectedSize = 2;
        int actualSize = actualMovies.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void TestMovieFileNotFound(){
        ArrayList<Movie> movies = FileReader.readMovies("data/moviesForRatingTest.csv");
        ArrayList<Movie> actualMovies = FileReader.readMovieRatings(movies, "data/poopMovies.csv");
        int expectedSize = 0;
        int actualSize = actualMovies.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void TestReadMovieRatings(){
        ArrayList<Movie> movies = FileReader.readMovies("data/moviesForRatingTest.csv");
        ArrayList<Movie> actualMovies = FileReader.readMovieRatings(movies, "data/3MovieRatings.csv");
        int expectedSize = 2;
        int actualSize = actualMovies.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void TestWhichMoviesMadeIt(){
        ArrayList<Movie> movies = FileReader.readMovies("data/moviesForRatingTest.csv");
        ArrayList<Movie> actualMovies = FileReader.readMovieRatings(movies, "data/3MovieRatings.csv");
        int expectedSize = 2;
        int actualSize = actualMovies.size();
        assertEquals(expectedSize, actualSize);
        String expected1 = "Beetlejuice";
        String expected2 = "Boogie Nights";
        String actual = actualMovies.get(0).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        actual = actualMovies.get(1).getTitle();
        assertTrue(expected1.equals(actual) || expected2.equals(actual));
        expectedSize = 2;
        actualSize = actualMovies.get(0).getRatings().size();
        assertEquals(expectedSize, actualSize);
       int expectedRating = 4;
        Rating rating = (Rating)actualMovies.get(0).getRatings().getValue();
        int actualRating = rating.getRating();
        assertEquals(expectedRating, actualRating);
//        expectedRating = 5;
//        rating = (Rating)actualMovies.get(0).getRatings().getNext().getValue();
//        actualRating = rating.getRating();
//        assertEquals(expectedRating, actualRating);
    }
}
