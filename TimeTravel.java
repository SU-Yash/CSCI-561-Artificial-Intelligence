import java.util.*;
public class TimeTravel {
    //private Problem problem;
    private int path_cost;
    private int grid_max_x;
    private int getGrid_max_y;
    private int num_channels;


    public static void readInput(){

    }
    public static void writeOutput(){
    }

    public static void BFS(Problem problem){
        Node node = problem.InitialState();
        int path_cost = 0;

        if(problem.GoalState().equals(node.NodeState())){
            //Return Solution(child)
        }

        Queue<Node> frontier = new LinkedList<>();
        ArrayList<Node> explored = new ArrayList<>();

        frontier.add(node);
        while(true){
            if(frontier.isEmpty()) break; //fail

            node = frontier.poll();
            explored.add(node);

            for(int action: problem.Actions(node)){
                Node child = problem.MoveOrJaunt(action);

                if(!(frontier.contains(child) || explored.contains(child))){
                    //
                    if(problem.GoalState().equals(child.NodeState())){
                        // Return Solution(child)
                    }
                    frontier.add(child);
                }
            }

        }

    }

    public ArrayList<State> Solution(){
        return new ArrayList<>();
    }

    public static void main(String[] args){
        readInput();
        Problem problem = new Problem(0, 0, 2020, 0, 0, 2021);
        BFS(problem);
        writeOutput();
    }
}


class Problem{
    private Node initial_state;
    private Node goal_state;
    private HashMap<State, State> channels;
    private static String[] actions;

    public Problem(int initial_x, int initial_y, int initial_year, int final_x, int final_y, int final_year){
        initial_state = new Node(initial_x, initial_y, initial_year);
        goal_state = new Node(final_x, final_y, final_year);
        channels = new HashMap<>();
        actions = new String[]{"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    }

    //Adding Jaunt locations to the problem
    public void AddChannels(int year_one, int x, int y, int year_two){
        channels.put(new State(x, y, year_one), new State(x, y, year_two));
        channels.put(new State(x, y, year_two), new State(x, y, year_one));
    }

    //Return a list of viable actions
    public ArrayList<Integer> Actions(Node node){
        ArrayList<String> actions_list = new ArrayList<>();
        for(int i=0; i< actions.length; i++){
            actions_list.add(actions[i]);
        }
        if(channels.containsKey(node.NodeState())){
            actions_list.add("J");
        }
        return new ArrayList<>();
    }

    public Node MoveOrJaunt(int action){
        return new Node(0, 0, 2020);
    }


    public boolean GoalReached(){
        return true;
    }

    public Node InitialState(){
        return initial_state;
    }
    public Node GoalState(){
        return goal_state;
    }
}

class Node{
    private State state;
    private Node parent;

    public Node(int x, int y, int year){
        state = new State(x,y,year);
        parent = null;
    }

    public String NodeString(){
        return Integer.toString(state.PositionX()) + Integer.toString(state.PositionY()) + Integer.toString(state.Year());
    }

    public State NodeState(){
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(state, node.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}


class Actions{

}

class State{
    private int position_x;
    private int position_y;
    private int year;

    public State(int x, int y, int year){
        position_x = x;
        position_y = y;
        this.year = year;
    }

    public int PositionX(){
        return position_x;
    }
    public int PositionY(){
        return position_y;
    }
    public int Year(){
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return position_x == state.position_x &&
                position_y == state.position_y &&
                year == state.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position_x, position_y, year);
    }
}
