import model.DataSource;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(dataSource.open()){
            System.out.println("Cannot open the data source");
            return;
        }else{
            dataSource.close();
        }
    }
}
