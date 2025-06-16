package ratings.datastructures;

import ratings.Song;

public class SongTitleComparator extends Comparator<Song> {
    @Override
    public boolean compare(Song a, Song b) {
        boolean answer;
        int answerInt = a.getTitle().compareToIgnoreCase(b.getTitle());

        if(answerInt < 0)
            answer = true;
        else
            answer = false;

        return answer;
    }
}
