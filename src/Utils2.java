import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils2 {
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

    public static ArrayList<State> parse2016Data(String electionData, String educationData, String employmentData) {
        ArrayList<State>states=new ArrayList<>();
        ArrayList<String>stateNames=new ArrayList<>();
        ArrayList<String> electionStates= getElectionStates(electionData);
        ArrayList<String> educationStates=getEducationStates(educationData);
        ArrayList<String> employmentStates=getEmploymentStates(employmentData);

        for(int i=0; i<electionStates.size(); i++){
            if (!stateNames.contains(electionStates.get(i).toUpperCase())) {
                stateNames.add(electionStates.get(i).toUpperCase());
            }
        }

        for(int i=0; i<educationStates.size(); i++){
            if (!stateNames.contains(educationStates.get(i).toUpperCase())) {
                stateNames.add(educationStates.get(i).toUpperCase());
            }
        }

        for(int i=0; i<employmentStates.size(); i++){
            if (!stateNames.contains(employmentStates.get(i).toUpperCase())) {
                stateNames.add(employmentStates.get(i).toUpperCase());
            }
        }


        for(int j=0; j<stateNames.size(); j++ ){
            State s=new State(stateNames.get(j));
            states.add(s);
        }

        for(int j=0; j<states.size(); j++ ){
            getElectionCounties(electionData,educationData,employmentData,states.get(j));
        }

        /**
         for (int j=0; j<states.size(); j++) {
         State s = states.get(j);
         getEmploymentCounties(employmentData,  s);
         }
         **/



        for(int j=0; j<states.size(); j++ ){
            State s = states.get(j);
            List<County>counties = s.getCounties();
            for (int i=0; i<counties.size(); i++) {
                County c = counties.get(i);
                System.out.println("State: " + s.getName() + " County: " + c.getName() + " fips: " + c.getFips()
                        + " demVotes: " + c.getVote2016().getDemVotes()
                        + " gopVotes: " + c.getVote2016().getGopVotes()
                        + " totalVotes: " + c.getVote2016().getTotalVotes());

            }

        }

        return states;
    }



    private static ArrayList<String> getEmploymentStates(String employmentData) {
        String [] values;
        ArrayList <String> returnStates=new ArrayList<>();
        String [] employmentRows=employmentData.split("\n");
        for(int row=9; row<employmentRows.length; row++){
            values=employmentRows[row].split(",");
            returnStates.add(values[1]);
        }
        return returnStates;
    }

    private static ArrayList<String> getEducationStates(String educationData) {
        String [] values;
        ArrayList <String> returnStates=new ArrayList<>();
        String [] educationRows=educationData.split("\n");
        for(int row=6; row<3288; row++){
            values=educationRows[row].split(",");
            returnStates.add(values[1]);
        }
        return returnStates;
    }

    private static ArrayList<String> getElectionStates(String electionData) {
        String [] values;
        ArrayList <String> returnStates=new ArrayList<>();
        String [] electionRows=electionData.split("\n");
        for(int row=1; row<electionRows.length; row++){
            values=electionRows[row].split(",");
            returnStates.add(values[values.length-3]);
        }
        return returnStates;
    }


    private static void getElectionCounties(String electionData,
                                            String educationData, String employmentData, State s) {
        String [] values;
        String [] electionRows=electionData.split("\n");
        String stateName=null, countyName=null;
        Election2016 election2016 = null;


        int fips=-1;
        for(int row=1; row<electionRows.length; row++){
            values=electionRows[row].split(",");
            stateName=values[values.length-3];
            countyName=values[values.length-2];
            fips=Integer.parseInt(values[values.length-1]);
            if (stateName.equalsIgnoreCase(s.getName())) {
                if(!s.getCounties().contains(countyName)) {
                    double demVotes=Double.parseDouble(values[1]);
                    double gopVotes=Double.parseDouble(values[2]);
                    double totalVotes=Double.parseDouble(values[3]);
                    election2016 = new Election2016(demVotes, gopVotes, totalVotes);
                    County c = new County(countyName,fips,election2016,null,null);
                    s.getCounties().add(c);
                }
            }
        }
    }


    private static ArrayList<String> getEmploymentCounties(String employmentData, State s) {
        String [] values;
        ArrayList <String> returnStates=new ArrayList<>();
        String countyName="";
//        String [] electionRows=electionData.split("\n");
//        String stateName=null, countyName=null;
//        Election2016 election2016 = null;
//        Employment2016 employment2016 = null;
//
//        int fips=-1;
//        for(int row=1; row<electionRows.length; row++){
//            values=electionRows[row].split(",");
//            stateName=values[values.length-3];
//            countyName=values[values.length-2];
//            fips=Integer.parseInt(values[values.length-1]);
//            if (stateName.equalsIgnoreCase(s.getName())) {
//            	if(!s.getCounties().contains(countyName)) {
//            		double demVotes=Double.parseDouble(values[1]);
//            		double gopVotes=Double.parseDouble(values[2]);
//            		double totalVotes=Double.parseDouble(values[3]);
//            		election2016 = new Election2016(demVotes, gopVotes, totalVotes);
//            		County c = new County(countyName,fips,election2016,null,null);
//            		s.getCounties().add(c);
//            	}
//            }
//        }



        String [] employmentRows=employmentData.split("\n");
        String employstr ="", begin="", end="";
        System.out.println("employmentRows: " + employmentRows.length);
        for(int row=9; row<employmentRows.length; row++){
            //for(int row=9; row<11; row++){
            employstr="";
            // Clean data
            String origstr=employmentRows[row];
            //System.out.println("Origstr: " + origstr);
            //System.out.println("LEn Origstr: " + origstr.length());
            values=employmentRows[row].split(",");
            if (values[3].length()==0)
                continue;
            for (int i=0; i<values.length; i++) {
                //System.out.println("values["+i + "]=" + values[i]);
                if (values[i].startsWith("\"")) {
                    begin = removeCharAt(values[i],0);
                    end= removeCharAt(values[i+1],values[i+1].length()-1);
                    employstr += begin;
                    employstr += end;
                    i++;
                } else {
                    employstr += values[i];
                }
                if (i < values.length -1 )
                    employstr+=",";
            }
            //System.out.println("StateName: " + stateName + " county: " + countyName + " row: " + row);
            //System.out.println(employstr);

            values = employstr.split(",");
            //System.out.println("Values. length " + values.length);
            String stateAbbr = values[1];
            countyName = values[2];
            String[] countyNames = countyName.split(" ");
            if (countyNames.length > 1) {
                for (int k=0; k<countyNames.length-1; k++) {
                    countyName += countyNames[k];
                    countyName += " ";
                }
            }
            //System.out.println("...." + stateAbbr + " ..." + countyName + "..." + values[42]);
            int totalLaborForce = Integer.parseInt(values[42].trim());
            //System.out.println("...." + totalLaborForce );
            int employedLaborForce = Integer.parseInt(values[43].trim());
            int unemployedLaborForce = Integer.parseInt(values[44].trim());
            double unemployedPercent = Double.parseDouble(values[45].trim());
            System.out.println(" " + stateAbbr + " " + countyName + " " + totalLaborForce + " " + employedLaborForce
                    + " " + unemployedLaborForce + " " + unemployedPercent);
        }

        return returnStates;
    }


    private static String removeCharAt(String s, int n) {
        String beginning=s.substring(0, n);
        String end=s.substring(n+1);
        return beginning+end;
    }

    private static String cleanData(String s, int start, int end, char oldC, char newC){
        String str=s.substring(start, end);
        String clean=str.replace(oldC, newC);
        return s.substring(0, start)+clean+s.substring(end+1);
    }

    /**
     private static ArrayList<String>getElectionCounties(State state, ArrayList<String>countyNames){
     String name=state.getName();


     }

     private static ArrayList<String>getEducationCounties(State state, ArrayList<String>countyNames){

     }

     private static ArrayList<String>getEmploymentCounties(State state, ArrayList<String>countyNames){

     }
     **/


    private static void cleanLine(String employmentData, State s) {
        String [] values;
        ArrayList <String> returnStates=new ArrayList<>();

        String stateName=null, countyName=null;
        Election2016 election2016 = null;
        Employment2016 employment2016 = null;
        String [] employmentRows=employmentData.split("\n");
        String str ="", begin="", end="";
        for(int row=10; row<11; row++){
            String origstr=employmentRows[row];
            System.out.println(origstr);
            values=employmentRows[row].split(",");
            for (int i=0; i<values.length; i++) {
                //System.out.println("values["+i + "]=" + values[i]);
                if (values[i].startsWith("\"")) {
                    begin = removeCharAt(values[i],0);
                    end= removeCharAt(values[i+1],values[i+1].length()-1);
                    str += begin;
                    str += end;
                    i++;
                } else {
                    str += values[i];
                }
                str+=",";
            }
//        	str = str.replaceAll("\",", "");
//
//            values=employmentRows[row].split(",");
//            stateName = values[1];
//            countyName= values[2];
//            if (stateName.equals("AL")) {
//            	for (int i=0; i<values.length; i++) {
//            		System.out.println("values[" +i + " ]=" + values[i]);
//            	}
//            }
        }
        System.out.println(str);
    }
}

