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

        //Random tests
        Problem problem = new Problem(0, 0, 2020, 0, 0, 2020, 100, 100, 5, 1, 1, 1);
        Node node = problem.InitialNode();
        System.out.println(problem.InitialNode().GetState().equals(node.GetState()));
        System.out.println(node.GetState());
        System.out.println(problem.InitialNode().GetState());

        /**
         //Problem problem = new Problem(0, 0, 2020, 2, 2, 2021, 100, 100, 5, 1 , 1, 1);
         Problem UCS_problem = new Problem(0, 0, 2020, 1, 27, 2019, 100, 100, 1, 10, 14, 0);
         UCS_problem.AddChannels(2020, 1, 1, 2021);
         //UCS_problem.AddChannels(2020, 1, 1, 2019);
         UCS_problem.AddChannels(2021, 2, 20, 2040);
         UCS_problem.AddChannels(2031, 99,20,2040);
         UCS_problem.AddChannels(2031, 41, 90, 2002);
         UCS_problem.AddChannels(2002, 1, 27, 2019);
         long startTime = System.nanoTime();
         //BFS(UCS_problem);
         UCS(UCS_problem);
         long endTime = System.nanoTime();
         long diff = endTime - startTime;
         double diff_time = (double) diff / 1_000_000_000;
         System.out.println("Took "+ diff_time + " s");

         Problem BFS_problem = new Problem(0, 0, 2020, 50, 50, 2022, 100, 100, 5, 1 , 1, 1);
         BFS_problem.AddChannels(2020, 0, 0, 2021);
         BFS_problem.AddChannels(2021, 50, 50, 2023);
         BFS_problem.AddChannels(2023, 50,50,2024);
         BFS_problem.AddChannels(2024, 50, 50, 2022);
         BFS_problem.AddChannels(2020, 100, 100, 2022);
         startTime = System.nanoTime();
         //BFS(BFS_problem);
         //AStar(BFS_problem);
         endTime = System.nanoTime();
         diff = endTime - startTime;
         diff_time = (double) diff / 1_000_000_000;
         System.out.println("Took "+ diff_time + " s");


         Problem AStar_problem = new Problem(0, 0, 2020, 2, 2, 2021, 100, 100, 5, 10 , 14, 0);
         AStar_problem.AddChannels(2020, 1, 1, 2021);
         startTime = System.nanoTime();

         //AStar(AStar_problem);

         endTime = System.nanoTime();
         diff = endTime - startTime;
         diff_time = (double) diff / 1_000_000_000;
         System.out.println("Took "+ diff_time + " s");
         **/


    }
}
