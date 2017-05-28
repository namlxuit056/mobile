package com.opensourceassignment.singkaraoke;

import java.io.Serializable;

public class Song implements Serializable {
    private int _songId;
    private String _title;
    private String _artist;
    private boolean _favorite;

    public Song(int songId, String title, String artist, boolean favorite){
        _songId = songId;
        _title = title;
        _artist = artist;
        _favorite = favorite;
    }

    public int getSongId(){
        return  _songId;
    }
    public String getTitle(){
        return _title;
    }
    public  String getArtist(){
        return _artist;
    }
    
    public boolean getFavorite(){
        return _favorite;
    }
    
    public void setFavorite(boolean value){
        _favorite = value;
    }
}
