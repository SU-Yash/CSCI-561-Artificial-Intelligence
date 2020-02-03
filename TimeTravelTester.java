public class TimeTravelTester {
    public static void main(String[] args){
        //Test If Nodes are same
        Node nodeOne = new Node(0,0, 2020);
        Node nodeTwo = new Node(0, 0, 2020);
        System.out.println(nodeOne.equals(nodeTwo));

        //Test if the states are same
        State stateOne = new State(0, 0, 2020);
        State stateTwo = new State(0, 0, 2020);
        System.out.println(stateOne.equals(stateTwo));


    }
}
