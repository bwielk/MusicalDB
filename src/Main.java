import model.Artist;
import model.DataSource;
import model.SongArtist;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Scanner;

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

            List<SongArtist> songArtists;
            songArtists = dataSource.queryArtistForSong("Heartless");
            System.out.println(songArtists.toString());
            for(SongArtist songArtist : songArtists){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                "\nALBUM: " + songArtist.getAlbumName());
            }

            songArtists = dataSource.queryArtistForSong("Going Under");
            for(SongArtist songArtist : songArtists){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                        "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                        "\nALBUM: " + songArtist.getAlbumName());
            }

            songArtists = dataSource.queryArtistForSong("Too Late");
            for(SongArtist songArtist : songArtists){
                System.out.println("\nARTIST: " + songArtist.getArtistName() +
                        "\nTRACK NUMBER: " + songArtist.getTrackNumber() +
                        "\nALBUM: " + songArtist.getAlbumName());
            }

            dataSource.querySongsMetadata();
            System.out.println("Number of results " + dataSource.getCount(DataSource.TABLE_SONGS));

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a song title: ");
            String title = scanner.nextLine();
            System.out.println(dataSource.createViewSongArtists());

            songArtists = dataSource.querySongInfoView(title);
            for(SongArtist sa : songArtists){
                System.out.println("\n" + sa.getArtistName() + " // " + sa.getAlbumName() + "// " + sa.getTrackNumber());
            }
            dataSource.close();
        }
    }
}
