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

public class TestDataStructures3 {
    @Test
    public void testDOS(){
        ArrayList<Movie> movies = FileReader.readMovies("data/dos.csv");
        String actor1 = "Owen Wilson";
        String actor2 = "Todd Field";
        DegreesOfSeparation dos = new DegreesOfSeparation(movies);
        int expected = 1;
        int actual = dos.degreesOfSeparation(actor1, actor2);
        assertEquals(expected, actual);
        actor2 = "Alan Ruck";
        expected = 2;
        actual = dos.degreesOfSeparation(actor1, actor2);
        assertEquals(expected, actual);
        actor2 = "Balthazar Getty";
        expected = 3;
        actual = dos.degreesOfSeparation(actor1, actor2);
        assertEquals(expected, actual);
        actor2 = "Owen Wilson";
        expected = 0;
        actual = dos.degreesOfSeparation(actor1, actor2);
        assertEquals(expected, actual);
        actor2 = "Mr Dond";
        expected = -1;
        actual = dos.degreesOfSeparation(actor1, actor2);
        assertEquals(expected, actual);
    }
}
