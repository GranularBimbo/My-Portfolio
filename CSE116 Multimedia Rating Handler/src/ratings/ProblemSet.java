package ratings;

import java.util.ArrayList;
import java.util.HashMap;

public class ProblemSet {


    public static double average(ArrayList<Double> numbers) {
        double sum = 0.0;
        double avg = 0.0;

        for(int i = 0; i < numbers.size(); i++){
            sum += numbers.get(i);
        }

        if(sum != 0.0)
            avg = sum/numbers.size();

        return avg;
        // TODO: Implement this method to return the average of all the numbers in the input ArrayList
        //       If the ArrayList is empty, return 0.0
        //
        // Examples
        // [1.0,2.0,3.0] returns 2.0
        // [-5.0,5.0] returns 0.0
        // [6.5,6.5,8.5,8.5] returns 7.5
        // [] returns 0.0

    }


    public static int sumOfDigits(int num){
        int sum = 0;
        String numWord = "" + num;
        ArrayList<Integer> digits = new ArrayList<Integer>();

        for(int i = 0; i < numWord.length(); i++){
            try{
                digits.add(Integer.parseInt(numWord.substring(i, i+1)));
            }
            catch(Exception e){
                // wasn't an int
            }
        }

        for(int i = 0; i < digits.size(); i++){
            sum += digits.get(i);
        }

        return sum;
    }
    // TODO: Write a public static method named sumOfDigits that takes an int as a parameter and
    //       returns the sum of the digits of the input as an int
    //
    // Examples
    // 123 returns 6
    // 57 returns 12
    // -36 returns 9


    public static String bestKey(HashMap<String, Integer> data){
        String key = "";
        int highestNum = Integer.MIN_VALUE;

        for(String k : data.keySet()){
            if(data.get(k) > highestNum){
                highestNum = data.get(k);
                key = k;
            }
        }

        return key;
    }
    // TODO: Write a public static method named bestKey that takes a HashMap of String to Integer
    //       as a parameter and returns a key mapping to the largest Integer. Ties can be broken arbitrarily.
    //       If the HashMap is empty, return the empty String
    //
    // Examples
    // {"CSE": 100, "MTH": 90, "MGT": 10} returns "CSE"
    // {"cat": 5, "dog": 5, "fox": 4} can return either "cat" or "dog"
    // {} returns ""

}
