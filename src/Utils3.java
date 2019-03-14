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

    private static boolean checkStringLengthZero(String s) {
        if (s.trim().length() == 0)
            return true;
        return false;
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

    public static ArrayList<State> parse2016Data(String electionData, String educationData, String employmentData){
        ArrayList<State> allStates = new ArrayList<>();
        ArrayList<String> stateNames = new ArrayList<>(), electionStates = getElectionStates(electionData), educationStates = getEducationStates(educationData), employmentStates = getEmploymentStates(employmentData);

        addStates(electionStates, stateNames);
        addStates(educationStates, stateNames);
        addStates(employmentStates, stateNames);

        for (String stateName : stateNames) {
            State s = new State(stateName);
            allStates.add(s);
        }

        for (int j = 0; j < allStates.size(); j++) {
            getElectionCounties(electionData, educationData, employmentData, allStates.get(j));
        }

        for (int j = 0; j < allStates.size(); j++) {
            State s = allStates.get(j);
            getEmploymentCounties(employmentData, s);
        }

        for (int j = 0; j < allStates.size(); j++) {
            State s = allStates.get(j);
            getEducationCounties(educationData, s);
        }

        return allStates;
    }

    private static void getEducationCounties(String educationData, State s) {
        String[] values;
        String[] educationRows = educationData.split("\n");


        for (int row = 6; row < 3288; row++) {
            values = educationRows[row].split(",");
            if ( (!values[1].equalsIgnoreCase(s.getName())) || (values[3].length() == 0) || (values.length < 47)) {
                continue;
            }

            String cleanedRow = cleanRow(educationRows[row]);
            values = cleanedRow.split(",");

            if (values.length < 47) {
                continue;
            }

            String countyName = values[2];

            if (checkStringLengthZero(values[40]) || checkStringLengthZero(values[41])
                    || checkStringLengthZero(values[42]) || checkStringLengthZero(values[43])) {
                continue;
            }

            Education2016 education2016 = makeEducation2016ObjectForCounty(values);
            County countyForState = s.getCounty(countyName);
            if (countyForState != null) {
                countyForState.setEduc2016(education2016);
            } else {
                County c = new County(countyName, 0, null, education2016, null);
                s.getCounties().add(c);
            }

        }
    }

    private static void getEmploymentCounties(String employmentData, State s) {
        String[] values;
        String[] employmentRows = employmentData.split("\n");

        for (int row = 9; row < employmentRows.length; row++) {
            values = employmentRows[row].split(",");

            if ( (!values[1].equalsIgnoreCase(s.getName())) || (values[3].length() == 0) || (values.length < 46)) {
                continue;
            }

            String cleanedRow = cleanRow(employmentRows[row]);

            values = cleanedRow.split(",");

            String countyNameAndStateCombined = values[2];
            String[] countyNames = countyNameAndStateCombined.split(" ");
            String finalCountyName = "";
            if (countyNames.length > 1) {
                for (int k = 0; k < countyNames.length - 1; k++) {
                    finalCountyName += countyNames[k];
                    finalCountyName += " ";
                }
                finalCountyName = finalCountyName.trim();
            }

            Employment2016 employ2016 = makeEmployment2016ObjectForCounty(values);

            County countyForState = s.getCounty(finalCountyName);
            if (countyForState != null) {
                countyForState.setEmploy2016(employ2016);
            } else {
                County c = new County(finalCountyName, 0, null, null, employ2016);
                s.getCounties().add(c);
            }
        }

    }

    private static void addStates(ArrayList<String> specificStates, ArrayList<String>allStateNames) {
        for (String specificState : specificStates) {
            if (!allStateNames.contains(specificState.toUpperCase())) {
                allStateNames.add(specificState.toUpperCase());
            }
        }
    }

    private static ArrayList<String> getEmploymentStates(String employmentData) {
        String[] values;
        ArrayList<String> returnStates = new ArrayList<>();
        String[] employmentRows = employmentData.split("\n");
        for (int row = 9; row < employmentRows.length; row++) {
            values = employmentRows[row].split(",");
            returnStates.add(values[1]);
        }
        return returnStates;
    }

    private static ArrayList<String> getEducationStates(String educationData) {
        String[] values;
        ArrayList<String> returnStates = new ArrayList<>();
        String[] educationRows = educationData.split("\n");
        for (int row = 6; row < 3288; row++) {
            values = educationRows[row].split(",");
            returnStates.add(values[1]);
        }
        return returnStates;
    }

    private static ArrayList<String> getElectionStates(String electionData) {
        String[] values;
        ArrayList<String> returnStates = new ArrayList<>();
        String[] electionRows = electionData.split("\n");
        for (int row = 1; row < electionRows.length; row++) {
            values = electionRows[row].split(",");
            returnStates.add(values[values.length - 3]);
        }
        return returnStates;
    }

    private static void getElectionCounties(String electionData, String educationData, String employmentData, State s) {
        String[] values;
        String[] electionRows = electionData.split("\n");
        String stateName = null, countyName = null;
        Election2016 election2016 = null;
        int fips = -1;

        for (int row = 1; row < electionRows.length; row++) {
            String cleanedRow=cleanRow(electionRows[row]);
            values = cleanedRow.split(",");
            stateName = values[values.length - 3];
            countyName = values[values.length - 2];
            fips = Integer.parseInt(values[values.length - 1]);
            if (stateName.equalsIgnoreCase(s.getName())) {
                if (!s.doesStateContainCounty(countyName)) {
                    election2016 = makeElection2016ObjectForNewCounty(values);
                    County c = new County(countyName, fips, election2016, null, null);
                    s.getCounties().add(c);
                }
            }
        }
    }

    private static Election2016 makeElection2016ObjectForNewCounty(String[] values) {
        double demVotes = Double.parseDouble(values[1]);
        double gopVotes = Double.parseDouble(values[2]);
        double totalVotes = Double.parseDouble(values[3]);

        Election2016 election2016 = new Election2016(demVotes, gopVotes, totalVotes);
        return election2016;
    }

    private static Employment2016 makeEmployment2016ObjectForCounty(String[] values) {
        int totalLaborForce = Integer.parseInt(values[42].trim());
        int employedLaborForce = Integer.parseInt(values[43].trim());
        int unemployedLaborForce = Integer.parseInt(values[44].trim());
        double unemployedPercent = Double.parseDouble(values[45].trim());

        Employment2016 employ2016 = new Employment2016(totalLaborForce, employedLaborForce, unemployedLaborForce,
                unemployedPercent);
        return employ2016;
    }

    private static Education2016 makeEducation2016ObjectForCounty(String[] values) {
		double noHighSchool = Double.parseDouble(values[40].trim());
		double onlyHighSchool = Double.parseDouble(values[41].trim());
		double someCollege = Double.parseDouble(values[42].trim());
		double bachelorsOrMore = Double.parseDouble(values[43].trim());

		Education2016 education2016 = new Education2016(noHighSchool, onlyHighSchool, someCollege, bachelorsOrMore);
		return education2016;
	}

}