import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    public static String readFileAsString(String filepath) {
        StringBuilder output = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(filepath))) {

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                output.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public static ArrayList<ElectionResult> parse2016ElectionResults(String data) {
        ArrayList<ElectionResult>results=new ArrayList<>();
        double votes_dem=0,votes_gop=0, total_votes=0, per_dem=0, per_gop=0, per_point_diff=0;
        int diff=0, combined_fips=0;
        String state_abbr="", county_name="";
        String [] rows=data.split("\n");
        for(int row=1; row<rows.length; row++){
            String[] values=cleanData(rows, row);
            votes_dem=(Double.parseDouble(values[1]));
            votes_gop=(Double.parseDouble(values[2]));
            total_votes=(Double.parseDouble(values[3]));
            per_dem=(Double.parseDouble(values[4]));
            per_gop=(Double.parseDouble(values[5]));
            diff=Integer.parseInt(values[6]);
            per_point_diff=(Double.parseDouble(values[7]));
            state_abbr=values[8];
            county_name=values[9];
            combined_fips=(Integer.parseInt(values[10]));
            ElectionResult electionResult=new ElectionResult(votes_dem, votes_gop, total_votes, per_dem, per_gop, diff, per_point_diff, state_abbr, county_name, combined_fips);
            results.add(electionResult);
        }
        return results;
    }

    private static String[] cleanData(String [] rows, int row) {
        String [] values=null;
        String valueInQuotes="", withoutQuote="", currentRowValues=rows[row];
        String withoutChar=currentRowValues.replace("%", "");
        int startingQuoteIndex=withoutChar.indexOf("\""), endQuoteIndex=-1;
        if(startingQuoteIndex!=-1){
            endQuoteIndex=withoutChar.indexOf("\"", startingQuoteIndex+1);
            valueInQuotes=withoutChar.substring(startingQuoteIndex, endQuoteIndex);
            String withoutComma=valueInQuotes.replace(",", "");
            withoutQuote=withoutComma.replace("\"","");
            String first=withoutChar.substring(0,startingQuoteIndex);
            String end=withoutChar.substring(endQuoteIndex+1);
            String finalString= first+ withoutQuote+ end;
            values=finalString.split(",");
        }else{
            values=withoutChar.split(",");
        }
        return values;
    }

}
