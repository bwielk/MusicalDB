package model;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

public class DataSource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Blazej W\\IdeaProjects\\MusicDB\\" + DB_NAME;
    private Connection connection;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMNS_ID_ALBUMS = "_id";
    public static final String COLUMNS_NAME_ALBUMS = "name";
    public static final String COLUMNS_ARTIST_ALBUMS = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMNS_ID_ARTIST = "_id";
    public static final String COLUMNS_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMNS_ID_SONG = "_id";
    public static final String COLUMNS_TRACK_SONG = "track";
    public static final String COLUMNS_TITLE_SONG = "title";
    public static final String COLUMNS_ALBUM_SONG = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " + TABLE_ARTISTS + "." + COLUMNS_ARTIST_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMNS_NAME_ALBUMS +
            ", " + TABLE_SONGS + "." + COLUMNS_TRACK_SONG +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMNS_ALBUM_SONG + " = " + TABLE_ALBUMS + "." + COLUMNS_ID_ALBUMS +
            " INNER JOIN " + TABLE_ARTISTS  + " ON " + TABLE_ALBUMS + "." + COLUMNS_ARTIST_ALBUMS + " = " + TABLE_ARTISTS + "." + COLUMNS_ID_ARTIST +
            " WHERE " + TABLE_SONGS + "." + COLUMNS_TITLE_SONG + " = \"";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS +
            "." + COLUMNS_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMNS_NAME_ALBUMS + " AS " + COLUMNS_ALBUM_SONG + ", " +
            TABLE_SONGS + "." + COLUMNS_TRACK_SONG + ", " + TABLE_SONGS + "." + COLUMNS_TITLE_SONG +
            " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMNS_ALBUM_SONG + " = " +TABLE_ALBUMS + "." + COLUMNS_ID_ALBUMS +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMNS_ARTIST_ALBUMS + " = " + TABLE_ARTISTS + "." + COLUMNS_ID_ARTIST;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMNS_ARTIST_NAME + ", " + COLUMNS_ALBUM_SONG + ", " + COLUMNS_TRACK_SONG + " FROM " +
            TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMNS_TITLE_SONG + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMNS_ARTIST_NAME + ", " + COLUMNS_ALBUM_SONG + ", " +
            COLUMNS_TRACK_SONG + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMNS_TITLE_SONG + " = ?";

    private PreparedStatement querySongInfoView;

    //CONSTANTS FOR A TRANSACTION
    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMNS_ARTIST_NAME + ") VALUES (?)";
    public static final String INSERT_ALBUM = "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMNS_NAME_ALBUMS + ", " + COLUMNS_ARTIST_ALBUMS + ") VALUES (?,?)";
    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS + "(" + COLUMNS_TRACK_SONG + ", " + COLUMNS_TITLE_SONG + ", " + COLUMNS_ALBUM_SONG + ") VALUES (?,?,?)";
    
    public boolean open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = connection.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try{
            if(querySongInfoView != null){
                querySongInfoView.close();
            }
            if(connection != null){
                connection.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Artist> queryArtists(int sortOrder){
        StringBuilder startQuery = new StringBuilder("SELECT * FROM ");
        startQuery.append(TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE){
            startQuery.append(" ORDER BY ");
            startQuery.append(COLUMNS_ARTIST_NAME);
            startQuery.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                startQuery.append(" DESC ");
            }else{
                startQuery.append(" ASC ");
            }
        }
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(startQuery.toString())){
            List<Artist> artists = new ArrayList<>();
            while(results.next()){
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;
        }catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder){
        StringBuilder startQuery = new StringBuilder("SELECT ");
        startQuery.append(TABLE_ALBUMS);
        startQuery.append(".");
        startQuery.append(COLUMNS_NAME_ALBUMS);
        startQuery.append(" FROM ");
        startQuery.append(TABLE_ALBUMS);
        startQuery.append(" INNER JOIN ");
        startQuery.append(TABLE_ARTISTS);
        startQuery.append(" ON ");
        startQuery.append(TABLE_ALBUMS);
        startQuery.append(".");
        startQuery.append(COLUMNS_ARTIST_ALBUMS);
        startQuery.append(" = ");
        startQuery.append(TABLE_ARTISTS);
        startQuery.append(".");
        startQuery.append(COLUMNS_ID_ARTIST);
        startQuery.append(" WHERE " + TABLE_ARTISTS + "." + COLUMNS_ARTIST_NAME + " = \"" + artistName + "\"");
        if(sortOrder != ORDER_BY_NONE){
            startQuery.append(" ORDER BY " + TABLE_ALBUMS + "." + COLUMNS_NAME_ALBUMS + " COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                startQuery.append("DESC");
            }else{
                startQuery.append("ASC");
            }
        }
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(startQuery.toString())){
            List<String> artistToalbum = new LinkedList<>();
            while(results.next()){
                artistToalbum.add(results.getString(1));
            }
            return artistToalbum;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<SongArtist> queryArtistForSong(String songName){
        StringBuilder startQuery = new StringBuilder(QUERY_ARTIST_FOR_SONG_START + songName + "\"");
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(startQuery.toString())){
            List<SongArtist> artists = new ArrayList<>();
            while(results.next()){
                SongArtist sa = new SongArtist();
                sa.setArtistName(results.getString(1));
                sa.setAlbumName(results.getString(2));
                sa.setTrackNumber(results.getInt(3));
                artists.add(sa);
            }
            return  artists;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void querySongsMetadata(){
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql)){

            ResultSetMetaData meta = results.getMetaData();
            int columnns = meta.getColumnCount();
            for(int i=1; i<=columnns; i++){
                System.out.println("Columns %d in the songs table is name %s\n" + i + " => " + meta.getColumnName(i));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getCount(String table){
        String sql = "SELECT COUNT(*), MIN(_id), MAX(_id) AS maximum FROM " + table;
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql)){
            int count = results.getInt(1);
            int min = results.getInt(2);
            int max = results.getInt("maximum");
            System.out.format("MINIMAL NUM => %d MAXIMUM NUM => %d\n", min, max);
            return count;
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public boolean createViewSongArtists(){
        try(Statement statement = connection.createStatement()){
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title){
       List<SongArtist> songArtists = new ArrayList<>();
        try{
            querySongInfoView.setString(1, title);
            ResultSet results = querySongInfoView.executeQuery();
            while(results.next()){
                SongArtist sa = new SongArtist();
                sa.setArtistName(results.getString(1));
                sa.setAlbumName(results.getString(2));
                sa.setTrackNumber(results.getInt(3));
                songArtists.add(sa);
            }
            return songArtists;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
