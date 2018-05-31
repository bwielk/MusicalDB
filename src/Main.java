import model.Artist;
import model.DataSource;

import java.util.List;
import java.util.Map;

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
            List<String> caroleKingAlbums = dataSource.queryAlbumsForArtist("Carole King", 1);
            List<String> zzTopAlbums = dataSource.queryAlbumsForArtist("ZZ Top", 1);
            System.out.println(caroleKingAlbums.toString());
            System.out.println(zzTopAlbums.toString());
            dataSource.close();
        }
    }
}
