package tests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ratings.*;
import ratings.datastructures.BinaryTreeNode;
import ratings.datastructures.LinkedListNode;
import ratings.datastructures.SongBayesianRatingComparator;
import ratings.datastructures.SongTitleComparator;

public class TestDataStructures2 {
    @Test
    public void testAddSongByTitle(){
        SongTitleComparator comparator = new SongTitleComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);
        String expected = "ant song";
        String actual = "";
        if(playlist.getSongTree().getLeft() != null)
            actual = playlist.getSongTree().getLeft().getValue().getTitle();
        assertTrue(actual,expected.equals(actual));
        expected = "car song";
        if(playlist.getSongTree().getRight() != null)
            actual = playlist.getSongTree().getRight().getValue().getTitle();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetSongListByTitle(){
        SongTitleComparator comparator = new SongTitleComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);
        String expected = "ant song";
        String actual = "";
        if(playlist.getSongList() != null)
            actual = playlist.getSongList().getValue().getTitle();
        assertTrue(actual ,expected.equals(actual));
        expected = "bandit song";
        if(playlist.getSongList().getNext() != null)
            actual = playlist.getSongList().getNext().getValue().getTitle();
        assertTrue(expected.equals(actual));
        expected = "car song";
        if(playlist.getSongList().getNext().getNext() != null)
            actual = playlist.getSongList().getNext().getNext().getValue().getTitle();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testPlaylistMissingASong(){
        SongTitleComparator comparator = new SongTitleComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        Song dogSong = new Song("dog song", "dog band", "dogID");
        Song eagerSong = new Song("eager song", "eager band", "eagerID");
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);
        playlist.addSong(dogSong);
        playlist.addSong(eagerSong);
        int expected = 5;
        int actual = playlist.getSongList().size();
        assertTrue(expected == actual);
    }

    @Test
    public void testPlaylistBiggerThanThree(){
        SongTitleComparator comparator = new SongTitleComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        Song dogSong = new Song("dog song", "dog band", "dogID");
        Song eagerSong = new Song("eager song", "eager band", "eagerID");
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);
        playlist.addSong(dogSong);
        playlist.addSong(eagerSong);
        int expectedSize = 5;
        int actualSize = playlist.getSongList().size();
        assertTrue(expectedSize == actualSize);
        String expected = "dog song";
        String actual = playlist.getSongList().getNext().getNext().getNext().getValue().getTitle();
        assertTrue(actual ,expected.equals(actual));
    }

    @Test
    public void testAddSongByAverage(){
        SongBayesianRatingComparator comparator = new SongBayesianRatingComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        Rating three = new Rating("three", 3);
        Rating one = new Rating("one", 1);
        Rating five = new Rating("five", 5);
        banditSong.addRating(three);
        antSong.addRating(one);
        carSong.addRating(five);
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);
        double expected = 11.0/3.0;
        double actual = 0.0;
        if(playlist.getSongTree().getLeft() != null)
            actual = playlist.getSongTree().getLeft().getValue().bayesianAverageRating(2, 3);
        assertEquals(expected, actual, 0.001);
        expected = 7.0/3.0;
        if(playlist.getSongTree().getRight() != null)
            actual = playlist.getSongTree().getRight().getValue().bayesianAverageRating(2, 3);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetSongListByAverage(){
        SongBayesianRatingComparator comparator = new SongBayesianRatingComparator();
        Playlist playlist = new Playlist(comparator);
        Song antSong = new Song("ant song", "ant band", "antID");
        Song banditSong = new Song("bandit song", "bandit band", "banditID");
        Song carSong = new Song("car song", "car band", "carID");
        Rating three = new Rating("three", 3);
        Rating one = new Rating("one", 1);
        Rating five = new Rating("five", 5);
        banditSong.addRating(three);
        antSong.addRating(one);
        carSong.addRating(five);
        playlist.addSong(banditSong);
        playlist.addSong(antSong);
        playlist.addSong(carSong);

    }
}
