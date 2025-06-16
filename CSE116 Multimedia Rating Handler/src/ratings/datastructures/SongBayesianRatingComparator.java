package ratings.datastructures;

import ratings.Song;

public class SongBayesianRatingComparator extends Comparator<Song> {
    @Override
    public boolean compare(Song a, Song b){
        boolean answer;
        double avg1 = a.bayesianAverageRating(2, 3);
        double avg2 = b.bayesianAverageRating(2, 3);

        if(avg1 > avg2)
            answer = true;
        else
            answer = false;

        return answer;
    }
}
