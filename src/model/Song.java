package model;

public class Song {

    private int id;
    private int track;
    private String name;
    private int album_id;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public int getTrack() {
        return track;
    }
}
