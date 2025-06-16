package tests;

import org.junit.Assert;
import org.junit.Test;
import ratings.ProblemSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestProblemSet {

    // TODO: Write testing for all 3 methods of the ratings.ProblemSet class
    @Test
    public void testAverage(){
        ArrayList<Double> first = new ArrayList<Double>();
        double answer = 0.0;
        double expected = 2.0;

        first.add(1.0);
        first.add(2.0);
        first.add(3.0);

        answer = ProblemSet.average(first);

        assertEquals(expected, answer, 0.001);

        ArrayList<Double> second = new ArrayList<Double>();
        expected = 0.0;

        second.add(-5.0);
        second.add(5.0);

        answer = ProblemSet.average(second);

        assertEquals(expected, answer, 0.001);

        ArrayList<Double> third = new ArrayList<Double>();
        expected = 7.5;

        third.add(6.5);
        third.add(6.5);
        third.add(8.5);
        third.add(8.5);

        answer = ProblemSet.average(third);

        assertEquals(expected, answer, 0.001);

        ArrayList<Double> fourth = new ArrayList<Double>();
        expected = 0.0;

        answer = ProblemSet.average(fourth);

        assertEquals(expected, answer, 0.001);

        ArrayList<Double> fifth = new ArrayList<Double>();
        expected = 0.0;

        fifth.add(0.0);
        fifth.add(0.0);
        fifth.add(0.0);

        answer = ProblemSet.average(fifth);

        assertEquals(expected, answer, 0.001);

        ArrayList<Double> sixth = new ArrayList<Double>();
        expected = 1.0;

        sixth.add(1.0);

        answer = ProblemSet.average(sixth);

        assertEquals(expected, answer, 0.001);
    }

    @Test
    public void testSumOfDigits(){
        int first = 123;
        int expected = 6;
        int answer = ProblemSet.sumOfDigits(first);
        assertTrue(expected == answer);
        int second = 57;
        expected = 12;
        answer = ProblemSet.sumOfDigits(second);
        assertTrue(expected == answer);
        int third = -36;
        expected = 9;
        answer = ProblemSet.sumOfDigits(third);
        assertTrue(expected == answer);
    }

    @Test
    public void testBestKey(){
        HashMap<String, Integer> first = new HashMap<String, Integer>();
        first.put("CSE", 100);
        first.put("MTH", 90);
        first.put("MGT", 10);
        String expected = "CSE";
        String answer = ProblemSet.bestKey(first);
        assertTrue(answer.equals(expected));
        HashMap<String, Integer> second = new HashMap<String, Integer>();
        second.put("cat", 5);
        second.put("dog", 5);
        second.put("fox", 4);
        expected = "cat";
        answer = ProblemSet.bestKey(second);
        assertTrue(answer.equals(expected) || answer.equals("dog"));
        HashMap<String, Integer> third = new HashMap<String, Integer>();
        third.put("fox", 4);
        third.put("dog", 5);
        third.put("cat", 5);
        expected = "dog";
        answer = ProblemSet.bestKey(third);
        assertTrue(answer.equals(expected) || answer.equals("cat"));
        HashMap<String, Integer> fourth = new HashMap<String, Integer>();
        expected = "";
        answer = ProblemSet.bestKey(fourth);
        assertTrue(answer.equals(expected));
        HashMap<String, Integer> fifth = new HashMap<String, Integer>();
        fifth.put("bees", -5);
        fifth.put("cheese", -1);
        fifth.put("tease", -10);
        expected = "cheese";
        answer = ProblemSet.bestKey(fifth);
        assertTrue(answer.equals(expected));
    }
}
