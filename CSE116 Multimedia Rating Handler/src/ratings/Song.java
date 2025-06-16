package ratings;

import ratings.datastructures.LinkedListNode;

import java.util.LinkedList;

public class Song extends Ratable{
    private String artist, ID;

    public Song(String title, String artist, String ID){
        setTitle(title);
        this.artist = artist;
        this.ID = ID;
    }

    // Getters and setters

    public String getArtist(){
        return artist;
    }

    public void setArtist(String val){
        this.artist = val;
    }
    public String getSongID(){
        return ID;
    }
    public void setSongID(String ID){
        this.ID = ID;
    }
}
