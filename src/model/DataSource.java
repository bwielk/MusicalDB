package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Blazej W\\IdeaProjects\\MusicDB\\" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMNS_ID_ALBUMS = "_id";
    public static final String COLUMNS_NAME_ALBUMS = "name";
    public static final String COLUMNS_ARTIST_ALBUMS = "artist";

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMNS_ID_ARTIST = "_id";
    public static final String COLUMNS_ARTIST_NAME = "name";

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMNS_ID_SONG = "_id";
    public static final String COLUMNS_TRACK_SONG = "track";
    public static final String COLUMNS_TITLE_SONG = "title";
    public static final String COLUMNS_ALBUM_SONG = "album";

    private Connection connection;

    public boolean open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try{
            if(connection != null){
                connection.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Artist> queryArtists(){
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);){
            List<Artist> artists = new ArrayList<>();
            while(results.next()){
                Artist artist = new Artist();
                artist.setId(results.getInt(COLUMNS_ID_ARTIST));
                artist.setName(results.getString(COLUMNS_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;
        }catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
