/* * *
* Main class for data parsers
* @author: Ankita Janakiraman
*/


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String electionData=Utils3.readFileAsString("data/2016_Presidential_Results.csv");
        String educationData=Utils3.readFileAsString("data/Education.csv");
        String employmentData=Utils3.readFileAsString("data/Unemployment.csv");


        ArrayList <State> states= Utils3.parse2016Data(electionData, educationData, employmentData);

        for (int j = 0; j < states.size(); j++) {
            State s = states.get(j);
            List<County> counties = s.getCounties();

            for (int i = 0; i < counties.size(); i++) {
                County c = counties.get(i);
                System.out.println("\nState: " + s.getName() + " County: " + c.getName());
                System.out.println();
                if (c.getVote2016() != null) {
                    System.out.println( "fips: " + c.getFips()
                            + " demVotes: " + c.getVote2016().getDemVotes() + " gopVotes: "
                            + c.getVote2016().getGopVotes() + " totalVotes: " + c.getVote2016().getTotalVotes());
                }

                if (c.getEmploy2016() != null) {
                    System.out.println("employed: "
                            + c.getEmploy2016().getEmployedLaborForce() + " unemployed: "
                            + c.getEmploy2016().getUnemployedLaborForce() + " total labor: "
                            + c.getEmploy2016().getTotalLaborForce() + " unemploy% : "
                            + c.getEmploy2016().getUnemployedPercent());
                }

                if (c.getEduc2016() != null) {
                    System.out.println( "noHighSchool: "
                            + c.getEduc2016().getNoHighSchool() + " onlyHighSchool: "
                            + c.getEduc2016().getOnlyHighSchool() + " someCollege: " + c.getEduc2016().getSomeCollege()
                            + " bachelorsOrMore : " + c.getEduc2016().getBachelorsOrMore());
                }

            }
        }
    }

}
