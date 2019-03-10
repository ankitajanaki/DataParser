import java.util.ArrayList;

public class DataManager {
    private ArrayList<State> states;

    public DataManager() {
        states=new ArrayList<>();
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }
}
