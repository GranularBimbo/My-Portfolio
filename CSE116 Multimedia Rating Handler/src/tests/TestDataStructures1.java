package tests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ratings.ProblemSet;
import ratings.Rating;
import ratings.Reviewer;
import ratings.Song;
import ratings.datastructures.LinkedListNode;

import java.util.LinkedList;

public class TestDataStructures1 {

    @Test
    public void testAddRatings(){
        // addRating() tests

        // Create song object

        // Create a Linked List of Ratings which contains all of your ratings

        // You'd then want to loop through that Linked List and add those ratings

        // And then assert them
        Song freezingMoon = new Song("Freezing Moon", "Mayhem", "What");
        LinkedList<Rating> list = new LinkedList<Rating>();
        Rating moonRating = new Rating("Joe", 5);
        LinkedListNode<Rating> firstNode = new LinkedListNode<Rating>(moonRating, null);
        freezingMoon.addRating(firstNode.getValue());
        list.add(firstNode.getValue());
        //freezingMoon.addRating(moonRating);
        String expected = "Joe";
        Rating answerRating = (Rating)freezingMoon.getRatings().getValue();
        String answer = answerRating.getReviewerID();
        assertTrue(answer.equals(expected));
        Rating moonRating2 = new Rating("Bob", 1);
        LinkedListNode<Rating> bobNode = new LinkedListNode<Rating>(moonRating2, null);
        freezingMoon.addRating(bobNode.getValue());
        list.add(bobNode.getValue());
        expected = "Bob";
        answerRating = (Rating)freezingMoon.getRatings().getNext().getValue();
        answer = answerRating.getReviewerID();
        assertTrue("Expected: " + expected + " Actual: " + answer , expected.equals(answer));
        Rating moonRating3 = new Rating("Steve", 6);
        freezingMoon.addRating(moonRating3);
        int expectedSize = 3;
        int actualSize = freezingMoon.getRatings().size();
        assertTrue("" + actualSize , actualSize == expectedSize);
        expected = "Steve";
        answerRating = (Rating)freezingMoon.getRatings().getNext().getNext().getValue();
        answer = answerRating.getReviewerID();
        assertTrue(answer.equals(expected));
        Rating negRating = new Rating("Stupid Joe", -100);
        freezingMoon.addRating(negRating);
        expectedSize = 4;
        actualSize = freezingMoon.getRatings().size();
        assertTrue(expectedSize == actualSize);
        expected = "Stupid Joe";
        Rating actualRating = (Rating)freezingMoon.getRatings().getNext().getNext().getNext().getValue();
        String actual = actualRating.getReviewerID();
        assertTrue(expected.equals(actual));
        Rating nullRating = null;
        freezingMoon.addRating(nullRating);
        expectedSize = 5;
        actualSize = freezingMoon.getRatings().size();
        assertTrue("" + actualSize,expectedSize == actualSize);
    }

    @Test
    public void testAverageRatings(){
        // averageRating() tests
        Song leaningWithIntentToFall = new Song("Leaning With Intent to Fall", "Dystopia", "How");
        Rating stopiaRating1 = new Rating("James", 1);
        Rating ogStopiaRating = new Rating("Pames", 5);
        Rating stopiaRating2 = new Rating("John", 2);
        Rating stopiaRating3 = new Rating("Evan", 3);
        //leaningWithIntentToFall.addRating(ogStopiaRating);
        leaningWithIntentToFall.addRating(stopiaRating1);
        leaningWithIntentToFall.addRating(stopiaRating2);
        leaningWithIntentToFall.addRating(stopiaRating3);
        double expectedAvg = 2.0;
        double actualAvg = leaningWithIntentToFall.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
        expectedAvg = 2.0;
        actualAvg = leaningWithIntentToFall.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
        Song nowAndForever = new Song("Now and Forever", "Dystopia", "Ez");
        Rating foreverRating1 = new Rating("Anthony", 2);
        Rating foreverRating2 = new Rating("Tyler", -100);
        Rating foreverRating3 = new Rating("Will", 4);
        Rating foreverRating4 = new Rating("Dylan", 7);
        nowAndForever.addRating(foreverRating1);
        nowAndForever.addRating(foreverRating2);
        nowAndForever.addRating(foreverRating3);
        nowAndForever.addRating(foreverRating4);
        // ratings for nowAndForever are now [rating1, rating2, rating3, rating4] or [2, -100, 4, 7]
        expectedAvg = 3.0;
        actualAvg = nowAndForever.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
        Song underground = new Song("Bad Song", "Bad Band", "Bad");
        expectedAvg = 0.0;
        actualAvg = underground.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
        Song song = new Song("song", "band", "songID");
        Rating songRating1 = new Rating("Joe", -100);
        Rating songRating2 = new Rating("Steve", 100);
        Rating songRating3 = new Rating("Don", 0);
        Rating songRating4 = new Rating("Blake", 6);
        song.addRating(songRating1);
        song.addRating(songRating2);
        song.addRating(songRating3);
        song.addRating(songRating4);
        expectedAvg = 0.0;
        actualAvg = song.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
        Song oneRating = new Song("one", "oneBand", "oneID");
        Rating oneRate = new Rating("oneGuy", 4);
        oneRating.addRating(oneRate);
        expectedAvg = 4.0;
        actualAvg = oneRating.averageRating();
        assertEquals(expectedAvg, actualAvg, 0.001);
    }

    @Test
    public void testSetRatings(){
        // setRatings() tests
        Song leaningWithIntentToFall = new Song("Leaning With Intent to Fall", "Dystopia", "How");
        Rating stopiaRating1 = new Rating("James", 1);
        Rating ogStopiaRating = new Rating("Pames", 5);
        Rating stopiaRating2 = new Rating("John", 2);
        Rating stopiaRating3 = new Rating("Evan", 3);
        LinkedListNode crazyNode = new LinkedListNode(stopiaRating1, null);
        leaningWithIntentToFall.addRating(ogStopiaRating);
        leaningWithIntentToFall.addRating(stopiaRating2);
        leaningWithIntentToFall.addRating(stopiaRating3);
        // list structure rn is [og, rating2, rating3] or [5, 2, 3]
        leaningWithIntentToFall.setRatings(leaningWithIntentToFall.getRatings().getNext());
        int expectedSize = 2;
        int actualSize = leaningWithIntentToFall.getRatings().size();
        assertTrue(expectedSize == actualSize);
        leaningWithIntentToFall.setRatings(crazyNode);
//        Rating newSecondNode = (Rating)leaningWithIntentToFall.getRatings().getNext().getValue();
        // now structure is [rating1, rating2, rating3] or [1, 2, 3]
        String expected = "James";
        Rating answerRating = (Rating)leaningWithIntentToFall.getRatings().getValue();
        String answer = answerRating.getReviewerID();
        assertTrue(expected.equals(answer));
//        assertTrue(oldSecondNode == newSecondNode);
        int expectedRating = 1;
        int actualRating = answerRating.getRating();
        assertTrue(expectedRating == actualRating);
        //LinkedListNode nullNode = null;
        //leaningWithIntentToFall.setRatings(nullNode);

    }

    @Test
    public void testRemoveRatingByReviewer(){
        // removeRatingByReviewer() tests
        Song nowAndForever = new Song("Now and Forever", "Dystopia", "Ez");
        Rating foreverRating1 = new Rating("Anthony", 2);
        Rating foreverRating2 = new Rating("Tyler", -100);
        Rating foreverRating3 = new Rating("Will", 4);
        Rating foreverRating4 = new Rating("Dylan", 7);
        nowAndForever.addRating(foreverRating1);
        nowAndForever.addRating(foreverRating2);
        nowAndForever.addRating(foreverRating3);
        nowAndForever.addRating(foreverRating4);
        Song song2 = nowAndForever;
        Reviewer anthony = new Reviewer("Anthony");
        nowAndForever.removeRatingByReviewer(anthony);
        String expected = "Tyler";
        String answer = "";
        if(nowAndForever.getRatings() != null){
            Rating answerRating = (Rating)nowAndForever.getRatings().getValue();
            answer = answerRating.getReviewerID();
            assertTrue(expected.equals(answer));
            int expectedSize = 3;
            int actualSize = nowAndForever.getRatings().size();
            assertTrue(expectedSize == actualSize);
            Reviewer will = new Reviewer("Will");
            nowAndForever.removeRatingByReviewer(will);
            expected = "Dylan";
            answerRating = (Rating)nowAndForever.getRatings().getNext().getValue();
            answer = answerRating.getReviewerID();
            assertTrue(answerRating.getReviewerID() ,expected == answer);
            expected = "Tyler";
            answerRating = (Rating)nowAndForever.getRatings().getValue();
            answer = answerRating.getReviewerID();
            assertTrue(expected == answer);
            expectedSize = 2;
            actualSize = nowAndForever.getRatings().size();
            assertTrue(expectedSize == actualSize);
            Reviewer dylan = new Reviewer("Dylan");
            nowAndForever.removeRatingByReviewer(dylan);
            expectedSize = 1;
            actualSize = nowAndForever.getRatings().size();
            assertTrue("" + actualSize ,expectedSize == actualSize);
        }
    }

    @Test
    public void testSong4(){
        Song johnSong = new Song("I Love People Named John", "John", "Joe (John)");
        Reviewer john = new Reviewer("John");
        Rating johnRating1 = new Rating("John", 5);
        Rating johnRating2 = new Rating("John", 4);
        Rating johnRating3 = new Rating("Steve", 3);
        johnSong.addRating(johnRating1);
        johnSong.addRating(johnRating2);
        johnSong.addRating(johnRating3);
        johnSong.removeRatingByReviewer(john);
        String expected = "John";
        String answer = "";
        if(johnSong.getRatings() != null){
            Rating answerRating = (Rating)johnSong.getRatings().getValue();
            answer = answerRating.getReviewerID();
            int expectedRating = 4;
            int actualRating = answerRating.getRating();
            assertTrue("" + actualRating ,expectedRating == actualRating);
        }
        assertTrue(expected.equals(answer));
        int expectedSize = 2;
        int actualSize = johnSong.getRatings().size();
        assertTrue(expectedSize == actualSize);
    }
}
