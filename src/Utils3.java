import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils3 {
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

    private static String removeCharAt(String s, int n) {
        String beginning=s.substring(0, n);
        String end=s.substring(n+1);
        return beginning+end;
    }

    private static String replaceChars(String s, int start, int end, char oldC, char newC){
        String str=s.substring(start, end);
        String clean=str.replace(oldC, newC);
        return s.substring(0, start)+clean+s.substring(end+1);
    }

    private static String cleanRow(String rowFromArray){
        //clean between quotes --> remove quotes and commas between the quotes
        int firstQuote= rowFromArray.indexOf("\"");
        int secondQuote=rowFromArray.indexOf("\"", firstQuote+1);

        while(firstQuote!=-1 && secondQuote!=-1){
            rowFromArray=cleanBetweenQuotes(rowFromArray, firstQuote, secondQuote);

            firstQuote= rowFromArray.indexOf("\"");
            secondQuote=rowFromArray.indexOf("\"", firstQuote+1);
        }
        return rowFromArray;
    }

    private static String cleanBetweenQuotes(String rowFromArray, int firstQuote, int secondQuote) {
        String beginning=rowFromArray.substring(0, firstQuote);
        String end= rowFromArray.substring((secondQuote+1));

        String toClean= rowFromArray.substring(firstQuote+1, secondQuote);

        return beginning+ cleanChars(toClean)+ end;
    }

    private static String cleanChars(String toClean) {
        toClean=toClean.replace("%", "");
        toClean=toClean.replace(",", "").trim();
        return toClean;
    }

    public static ArrayList<State> parseElection2016DataForStates(String electionData){
        ArrayList<State> electionStates=new ArrayList<>();
        String []electionDataRows=electionData.split("\n");
        for(int row=1; row<electionDataRows.length; row++){
            String cleanedRow=cleanRow(electionDataRows[row]);
            String[]values=cleanedRow.split(",");
            State state=new State(values[8]);
        }
        return removeRepeats(electionStates);
    }

    public static ArrayList<County>parseElection2016DataForCounties()

}