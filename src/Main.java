import model.Artist;
import model.DataSource;
import model.SongArtist;

import javax.xml.crypto.Data;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(!dataSource.open()){
            System.out.println("Cannot open the data source");
            return;
        }else{
            List<Artist> results = dataSource.queryArtists(dataSource.ORDER_BY_ASC);
            for(Artist a : results){
                System.out.println("ID : " + a.getId() + " NAME :" + a.getName());
            }

            List<String> caroleKingAlbums = dataSource.queryAlbumsForArtist("Carole King", DataSource.ORDER_BY_DESC);
            List<String> zzTopAlbums = dataSource.queryAlbumsForArtist("ZZ Top", 1);
            System.out.println(caroleKingAlbums.toString());
            System.out.println(zzTopAlbums.toString());

            List<SongArtist> songArtistsHeartless;
            songArtistsHeartless = dataSource.queryArtistForSong("Heartless");
            System.out.println(songArtistsHeartless.toString());
            for(SongArtist songArtist : songArtistsHeartless){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                "\nALBUM: " + songArtist.getAlbumName());
            }

            songArtistsHeartless = dataSource.queryArtistForSong("Going Under");
            for(SongArtist songArtist : songArtistsHeartless){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                        "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                        "\nALBUM: " + songArtist.getAlbumName());
            }

            songArtistsHeartless = dataSource.queryArtistForSong("Too Late");
            for(SongArtist songArtist : songArtistsHeartless){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                        "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                        "\nALBUM: " + songArtist.getAlbumName());
            }

            dataSource.querySongsMetadata();
            System.out.println("Number of results " + dataSource.getCount(DataSource.TABLE_SONGS));

            System.out.println(dataSource.createViewSongArtists());
            dataSource.close();
        }
    }
}
