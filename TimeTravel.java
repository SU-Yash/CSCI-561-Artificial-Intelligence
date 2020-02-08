import java.io.*;
import java.util.*;

public class TimeTravel {

    public static void readInput() throws IOException {

        File intput_file = new File("input.txt");
        Scanner in = new Scanner(intput_file);
        String search_algo = in.nextLine();
        int max_x, max_y, initial_year, initial_x, initial_y, final_x, final_y, final_year, number_of_channels, first_year, x, y, second_year, NSEW, DIAGONAL, JAUNT;
        max_x = in.nextInt();
        max_y = in.nextInt();

        initial_year = in.nextInt();
        initial_x = in.nextInt();
        initial_y = in.nextInt();

        final_year = in.nextInt();
        final_x = in.nextInt();
        final_y = in.nextInt();

        number_of_channels = in.nextInt();

        if (search_algo.equals("BFS")){
            NSEW = DIAGONAL = JAUNT = 1;
        }
        else {
            NSEW = 10;
            DIAGONAL = 14;
            JAUNT = 0;
        }

        Problem problem = new Problem(initial_x, initial_y, initial_year, final_x, final_y, final_year, max_x, max_y, number_of_channels, NSEW, DIAGONAL, JAUNT);

        for(int i = 0; i< number_of_channels; i++){
            first_year = in.nextInt();
            x = in.nextInt();
            y = in.nextInt();
            second_year = in.nextInt();
            problem.AddChannels(first_year, x, y, second_year);
        }

        // Start Search
        if(search_algo.equals("BFS")){ BFS(problem); }
        else if (search_algo.equals("UCS")){ UCS(problem); }
        else { AStar(problem); }

    }

    public static void AStar(Problem problem) throws IOException {
        Node node = problem.InitialNode();

        PriorityQueue<Node> frontier = new PriorityQueue<>(10, new AStarComparartor());
        HashSet<Node> explored = new HashSet<>();

        frontier.add(node);

        while(true){
            if(frontier.isEmpty()){
                Failed();
                System.out.println("Failed");//fail
                return;
            }

            node = frontier.poll();
            if(problem.GoalNode().GetState().equals(node.GetState())){
                Solution(node);
                System.out.println("Reached in loop: " + node.GetCost());
                return;
            }
            explored.add(node);

            for(Node child: problem.Actions(node)) {

                if(!(frontier.contains(child) || explored.contains(child))) {
                    child.SetParent(node);
                    child.SetTotalCost(problem.FutureCost(child));
                    child.SetCost(); // assign temp cost to cost
                    frontier.add(child); // based on the temp+future cost

                }
                else if (frontier.contains(child)){
                    Iterator<Node> iter = frontier.iterator();
                    Node c = null;
                    while(iter.hasNext()){
                        c = iter.next();
                        if(c.equals(child)){
                            break;
                        }
                    }
                    if(c.GetTotalCost() > (child.GetTempCost() + problem.FutureCost(child))) {
                        //probably remove the node from the queue first, modify cost and then add it again
                        child.SetParent(node);
                        frontier.remove(child);
                        child.SetCost();
                        frontier.add(child);
                    }
                }
            }
        }
    }

    public static void UCS(Problem problem) throws IOException {
        Node node = problem.InitialNode();

        PriorityQueue<Node> frontier = new PriorityQueue<>(10, new UCSComparator());
        HashSet<Node> explored = new HashSet<>();

        frontier.add(node);

        while(true){
            if(frontier.isEmpty()){
                Failed();
                System.out.println("Failed");//fail
                return;
            }

            node = frontier.poll();
            if(problem.GoalNode().GetState().equals(node.GetState())){
                Solution(node);
                System.out.println("Reached in loop: " + node.GetCost());
                return;
            }
            explored.add(node);

            for(Node child: problem.Actions(node)) {

                if(!(frontier.contains(child) || explored.contains(child))) {
                    child.SetParent(node);
                    child.SetCost();
                    //System.out.println(child.NodeString());
                    frontier.add(child);

                }
                else if (frontier.contains(child)){
                    Iterator<Node> iter = frontier.iterator();
                    Node c = null;
                    while(iter.hasNext()){
                        c = iter.next();
                        if(c.equals(child)){
                            break;
                        }
                    }
                    if(c.GetCost() > child.GetTempCost()) {
                        //probably remove the node from the queue first, modify cost and then add it again
                        child.SetParent(node);
                        frontier.remove(child);
                        child.SetCost();
                        frontier.add(child);
                    }
                }
            }
        }
    }
    public static void BFS(Problem problem) throws IOException {
        Node node = problem.InitialNode();

        if(problem.GoalNode().GetState().equals(node.GetState())){
            Solution(node);
            System.out.println("Reached");
            return;
        }

        Queue<Node> frontier = new LinkedList<>();
        ArrayList<Node> explored = new ArrayList<>();

        frontier.add(node);
        while(true){
            if(frontier.isEmpty()){
                Failed();
                System.out.println("Failed");//fail
                return;
            }

            node = frontier.poll();
            explored.add(node);

            for(Node child: problem.Actions(node)){

                if(!(frontier.contains(child) || explored.contains(child))){

                    child.SetParent(node);
                    child.SetCost();

                    if(problem.GoalNode().GetState().equals(child.GetState())){
                        Solution(child);
                        System.out.println("Reached in loop: " + child.GetCost());
                        return;
                    }
                    frontier.add(child);
                }
            }

        }

    }

    public static void Failed() throws IOException {
        File file = new File("output.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write("FAIL");
        out.close();
    }

    public static void Solution(Node node) throws IOException {
        File file = new File("output.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        out.write(Integer.toString(node.GetCost()));
        System.out.println(node.GetCost());
        out.newLine();

        List<Node> path = new ArrayList<>();
        while(node != null){
            path.add(node);
            node = node.GetParent();
        }

        //reverse
        Collections.reverse(path);

        out.write(Integer.toString(path.size()));
        System.out.println(path.size());
        out.newLine();

        for(int i = 0; i < path.size() - 1; i++){
            Node n = path.get(i);
            System.out.println(n.NodeString());
            out.write(n.NodeString());
            out.newLine();
        }
        out.write(path.get(path.size() - 1).NodeString());
        out.close();
    }

    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        readInput();
        long end = System.nanoTime();

        System.out.println("Time: " + (end - start)/1000000000);

    }
}


class Problem{
    private Node initial_node;
    private Node goal_node;
    private HashMap<Node, ArrayList<Node>> channels;
    private static String[] actions;
    private int grid_max_x;
    private int grid_max_y;
    private int num_channels;
    private int NSEW_COST;
    private int DIAGONAL_COST;
    private int JAUNT_COST;

    public Problem(int initial_x, int initial_y, int initial_year, int final_x, int final_y, int final_year, int grid_max_x, int grid_max_y, int num_channels, int NSEW, int DIAGONAL, int JAUNT){
        initial_node = new Node(initial_x, initial_y, initial_year);
        goal_node = new Node(final_x, final_y, final_year);
        channels = new HashMap<>();
        actions = new String[]{"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        this.grid_max_x = grid_max_x;
        this.grid_max_y = grid_max_y;
        this.num_channels = num_channels;
        NSEW_COST = NSEW;
        DIAGONAL_COST = DIAGONAL;
        JAUNT_COST = JAUNT;
    }

    //Adding Jaunt locations to the problem
    public void AddChannels(int year_one, int x, int y, int year_two){
        Node nodeOne = new Node(x, y, year_one);
        Node nodeTwo = new Node(x, y, year_two);
        ArrayList<Node> list;

        list = channels.getOrDefault(nodeOne, new ArrayList<Node>());
        list.add(nodeTwo);
        channels.put(nodeOne, list);

        list = channels.getOrDefault(nodeTwo, new ArrayList<Node>());
        list.add(nodeOne);
        channels.put(nodeTwo, list);
    }

    public boolean legal_position(int x, int y) {
        if (x >= 0 && x <= grid_max_x && y >= 0 && y <= grid_max_y) {
            return true;
        }
        return false;
    }

    //Return a list of viable actions
    public ArrayList<Node> Actions(Node node){
        ArrayList<Node> actions_list = new ArrayList<>();

        if(legal_position(node.GetState().GetX(), node.GetState().GetY() + 1)){
            actions_list.add(MoveOrJaunt("N", node));
        }
        if(legal_position(node.GetState().GetX() + 1, node.GetState().GetY() + 1)){
            actions_list.add(MoveOrJaunt("NE", node));
        }
        if(legal_position(node.GetState().GetX() + 1, node.GetState().GetY())){
            actions_list.add(MoveOrJaunt("E", node));
        }
        if(legal_position(node.GetState().GetX() + 1, node.GetState().GetY() - 1)){
            actions_list.add(MoveOrJaunt("SE", node));
        }
        if(legal_position(node.GetState().GetX(), node.GetState().GetY() - 1)){
            actions_list.add(MoveOrJaunt("S", node));
        }
        if(legal_position(node.GetState().GetX() - 1 , node.GetState().GetY() - 1)){
            actions_list.add(MoveOrJaunt("SW", node));
        }
        if(legal_position(node.GetState().GetX() - 1, node.GetState().GetY())){
            actions_list.add(MoveOrJaunt("W", node));
        }
        if(legal_position(node.GetState().GetX() - 1, node.GetState().GetY() + 1)){
            actions_list.add(MoveOrJaunt("NW", node));
        }

        if(channels.containsKey(node)){
            for(Node child : channels.get(node)){
                if(JAUNT_COST == 1 ){ child.SetTempCost(node.GetCost() + JAUNT_COST); }
                else{ child.SetTempCost(node.GetCost() + Math.abs(node.GetState().GetYear() - child.GetState().GetYear())); }
                actions_list.add(child);
            }
        }
        return actions_list;
    }

    public Node MoveOrJaunt(String action, Node node){
        Node child = null;
        switch(action){
            case "N":
                child = new Node(node.GetState().GetX(), node.GetState().GetY() + 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + NSEW_COST);
                break;
            case "S":
                child = new Node(node.GetState().GetX(), node.GetState().GetY() - 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + NSEW_COST);
                break;
            case "W":
                child = new Node(node.GetState().GetX() - 1, node.GetState().GetY(), node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + NSEW_COST);
                break;
            case "E":
                child = new Node(node.GetState().GetX() + 1, node.GetState().GetY(), node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + NSEW_COST);
                break;
            case "NE":
                child = new Node(node.GetState().GetX() + 1, node.GetState().GetY() + 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + DIAGONAL_COST);
                break;
            case "NW":
                child = new Node(node.GetState().GetX() - 1, node.GetState().GetY() + 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + DIAGONAL_COST);
                break;
            case "SE":
                child = new Node(node.GetState().GetX() + 1, node.GetState().GetY() - 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + DIAGONAL_COST);
                break;
            case "SW":
                child = new Node(node.GetState().GetX() - 1, node.GetState().GetY() - 1, node.GetState().GetYear());
                child.SetTempCost(node.GetCost() + DIAGONAL_COST);
                break;
        }
        return child;
    }

    public int FutureCost(Node node){
        int cost_x = 0, cost_y = 0, cost = 0;
        cost_x = Math.abs(node.GetState().GetX() - goal_node.GetState().GetX());
        cost_y = Math.abs(node.GetState().GetY() - goal_node.GetState().GetY());
        cost =  Math.min(cost_x, cost_y) * 14 + Math.abs(cost_x-cost_y) * 10 + Math.abs(node.GetState().GetYear() - goal_node.GetState().GetYear());
        return cost;
    }
    public Node InitialNode(){ return initial_node; }

    public Node GoalNode(){ return goal_node; }
}

class Node{
    private State state;
    private Node parent;
    private int cost;
    private int temp_cost;
    private int total_cost;

    public Node(int x, int y, int year){
        state = new State(x,y,year);
        parent = null;
        cost = 0;
        temp_cost =0;
        total_cost =0;
    }

    public String NodeString(){
        if(parent == null){
            return Integer.toString(state.GetYear()) + " " + Integer.toString(state.GetX()) + " " + Integer.toString(state.GetY()) + " " + Integer.toString(0);
        }
        return Integer.toString(state.GetYear()) + " " + Integer.toString(state.GetX()) + " " + Integer.toString(state.GetY()) + " " + Integer.toString(cost - parent.cost);
    }

    public State GetState(){
        return state;
    }

    public void SetParent(Node node){
        parent = node;
    }

    public Node GetParent(){ return parent; }

    public void SetTempCost(int cost){ this.temp_cost = cost; }

    public int GetTempCost(){ return temp_cost; }

    public void SetCost(){ this.cost = temp_cost; }

    public void SetTotalCost(int future_cost){ total_cost =  temp_cost + future_cost; }

    public int GetTotalCost(){ return total_cost; }

    public int GetCost(){ return cost; }

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

class State{
    private int position_x;
    private int position_y;
    private int year;

    public State(int x, int y, int year){
        position_x = x;
        position_y = y;
        this.year = year;
    }

    public int GetX(){
        return position_x;
    }
    public int GetY(){
        return position_y;
    }
    public int GetYear(){
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

class UCSComparator implements Comparator<Node>{
    @Override
    public int compare(Node n1, Node n2){
        if(n1.GetCost() > n2.GetCost()){ return 1; }
        return -1;
    }

}

class AStarComparartor implements Comparator<Node>{
    @Override
    public int compare(Node n1, Node n2){
        if(n1.GetTotalCost() > n2.GetTotalCost()){ return 1; }
        return -1;
    }
}