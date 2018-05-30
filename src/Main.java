import model.Artist;
import model.DataSource;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(dataSource.open()){
            System.out.println("Cannot open the data source");
            return;
        }else{
            dataSource.close();
        }

        List<Artist> results = dataSource.queryArtists();
        for(Artist a : results){
            a.toString();
        }
    }
}
