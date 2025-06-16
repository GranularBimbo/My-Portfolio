package ratings;

import java.util.ArrayList;

public class MediaLibrary {
    ArrayList<Song> songs;
    ArrayList<Movie> movies;
    ArrayList<Movie> ratedMovies;
    ArrayList<Ratable> sortedList;

    public MediaLibrary(){
        sortedList = new ArrayList<Ratable>();
    }

    public void populateLibrary(String songsFile, String moviesFile, String movieRatingsFile){
        songs = FileReader.readSongs(songsFile);
        movies = FileReader.readMovies(moviesFile);
        ratedMovies = FileReader.readMovieRatings(movies, movieRatingsFile);
    }

    public ArrayList<Ratable> topKRatables(int k){
        ArrayList<Ratable> topRatables = new ArrayList<Ratable>();
        ArrayList<Ratable> ratables = new ArrayList<Ratable>();

        for(int i = 0; i < songs.size(); i++){
            ratables.add(songs.get(i));
        }

        for(int i = 0; i < ratedMovies.size(); i++){
            ratables.add(ratedMovies.get(i));
        }

        sortList(ratables);

        if(k > sortedList.size())
            topRatables = sortedList;
        else{
            for(int c = 0; c < k; c++){
                topRatables.add(sortedList.get(c));
            }
        }

        return topRatables;
    }

    public void sortList(ArrayList<Ratable> list){
        double highest = 0;
        int highestIndex = 0;

        for(int i = 0; i < list.size(); i++){
            double avg = list.get(i).bayesianAverageRating(2, 3);
            if(avg > highest){
                highest = avg;
                highestIndex = i;
            }
        }

        if(list.size() > 0){
            sortedList.add(list.get(highestIndex));
            list.remove(highestIndex);
            sortList(list);
        }
    }
}
