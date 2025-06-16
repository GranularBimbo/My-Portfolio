package ratings;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class FileReader {
    public static ArrayList<Movie> readMovieRatings(ArrayList<Movie> movies, String filename){
        ArrayList<Movie> list = new ArrayList<Movie>();
        ArrayList<String> movieList = new ArrayList<String>();
        ArrayList<String> movieTitles = new ArrayList<String>();
        HashMap<String, ArrayList<String>> castListByTitle = new HashMap<String, ArrayList<String>>();

        for(int i = 0; i < movies.size(); i++){
            movieTitles.add(movies.get(i).getTitle());
            castListByTitle.put(movies.get(i).getTitle(), movies.get(i).getCast());
        }

        try{
            movieList = new ArrayList<String>(Files.readAllLines(Paths.get(filename)));
            for(int i = 0; i < movieList.size(); i++){
                ArrayList<String> splits = new ArrayList<String>(Arrays.asList(movieList.get(i).split(",")));
                if(!movieList.get(i).equals("")){
                    if(movieTitles.contains(splits.get(0))){
                        String title = splits.get(0);
                        String reviewerID = splits.get(1);
                        int ratingNum = Integer.parseInt(splits.get(2));
                        Rating rating = new Rating(reviewerID, ratingNum);
                        int timesInArray = 0;

                        for(int c = 0; c < list.size(); c++){
                            if(list.get(c) != null){
                                if(title.equals(list.get(c).getTitle())){
                                    timesInArray++;
                                    list.get(c).addRating(rating);
                                    break;
                                }
                            }
                        }

                        if(timesInArray < 1){
                            Movie movie = new Movie(title, castListByTitle.get(title));
                            movie.addRating(rating);
                            list.add(movie);
                        }
                    }
                }
            }
        }
        catch(IOException e){

        }
        return list;
    }

    public static ArrayList<Song> readSongs(String filename){
        ArrayList<String> songList = new ArrayList<String>();
        ArrayList<Song> songs = new ArrayList<Song>();

        try{
            songList = new ArrayList<String>(Files.readAllLines(Paths.get(filename)));
            for(int i = 0; i < songList.size(); i++){
                ArrayList<String> splits = new ArrayList<String>(Arrays.asList(songList.get(i).split(",")));
                if(!songList.get(i).equals("")){
                    String songId = splits.get(0);
                    String artist = splits.get(1);
                    String title = splits.get(2);
                    String reviewerID = splits.get(3);
                    int rating = Integer.parseInt(splits.get(4));
                    int timesInArray = 0;

                    for(int c = 0; c < songs.size(); c++){
                        if(songs.get(c) != null){
                            if(songId.equals(songs.get(c).getSongID())){
                                timesInArray++;
                                Rating songRating = new Rating(reviewerID, rating);
                                songs.get(c).addRating(songRating);
                                break;
                            }
                        }
                    }

                    if(timesInArray < 1){
                        Song song = new Song(title, artist, songId);
                        Rating songRating = new Rating(reviewerID, rating);
                        song.addRating(songRating);
                        songs.add(song);
                    }
                }
            }
        }
        catch(IOException e){

        }

        return songs;
    }

    public static ArrayList<Movie> readMovies(String filename){
        ArrayList<String> movieList = new ArrayList<String>();
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try{
            movieList = new ArrayList<String>(Files.readAllLines(Paths.get(filename)));
            for(int i = 0; i < movieList.size(); i++){
                ArrayList<String> splits = new ArrayList<String>(Arrays.asList(movieList.get(i).split(",")));
                if(!movieList.get(i).equals("")){
                    String title = splits.get(0);
                    ArrayList<String> actors = new ArrayList<String>();
                    for(int a = 1; a < splits.size(); a++){
                        actors.add(splits.get(a));
                    }

                    Movie movie = new Movie(title, actors);
                    movies.add(movie);
                }
            }
        }
        catch(IOException e){

        }

        return movies;
    }
}
