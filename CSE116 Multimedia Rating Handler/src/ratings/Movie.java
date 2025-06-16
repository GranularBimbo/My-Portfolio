package ratings;

import ratings.datastructures.LinkedListNode;

import java.util.ArrayList;

public class Movie extends Ratable{
    private ArrayList<String> cast;

    public Movie(String title, ArrayList<String> cast){
        setTitle(title);
        this.cast = cast;
    }

    // Getters and setters
    public ArrayList<String> getCast(){
        return cast;

        /*
        ArrayList<String> castList = new ArrayList<String>();

        for(int i = 0; i < cast.size(); i++){
            if(cast.get(i) != null)
                castList.add(cast.get(i));
        }

        return castList;
         */
    }
}
