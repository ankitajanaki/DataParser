/* * *
* Main class for data parsers
* @author: Ankita Janakiraman
*/


import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Test of utils
        String data=Utils.readFileAsString("data/2016_Presidential_Results.csv");
        ArrayList <ElectionResult> results= Utils.parse2016ElectionResults(data);
        for(int i=0; i<results.size(); i++){
            System.out.println(results.get(i).toString());
        }
    }
}
