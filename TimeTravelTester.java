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

         //Check if elements are added to priority queue in order:
         A*
         100 100
         2020 14 14
         2020 15 15
         0

         // Check if the switch is happening in UCS
         UCS
         2 2
         3011 0 0
         3023 0 0
         4
         3011 0 0 3013
         3011 0 0 3010
         3013 0 0 3023
         3010 0 0 3023

         // Check if switch is happening in A*

         // Multichannel


         //Base Case- Already at goal
         BFS
         100 100
         2020 10 10
         2020 10 10
         1
         2020 10 10 2012

         UCS
         100 100
         2020 10 10
         2020 10 10
         1
         2020 10 10 2012

         A*
         100 100
         2020 10 10
         2020 10 10
         1
         2020 10 10 2012

         //Stress Test
         BFS
         100 100
         2020 0 0
         2040 100 100
         4
         2020 100 100 3000
         3000 0 0 2000
         2030 100 100 2000
         2030 0 0 2040

         UCS different than A*


         // Same Plane
         BFS
         100 100
         2020 0 0
         2020 10 10
         0

         UCS
         100 100
         2020 0 0
         2020 10 10
         0

         A*
         100 100
         2020 0 0
         2020 10 10
         0

         // Order of paths : UCS vs A*

         // 10s come before 14s

         UCS
         100 100
         2020 0 0
         2020 11 16
         0


         // 14s come before 10s

         A*
         100 100
         2020 0 0
         2020 11 16
         0

         UCS
         100 100
         2020 49 49
         2030 0 0
         15
         2020 0 0 2021
         2021 100 100 2029
         2029 100 100 2020
         2020 80 80 2031
         2031 0 0 2019
         2019 10 99 2028
         2028 99 10 2021
         2021 1 1 2032
         2032 100 100 2030
         2030 50 50 2050
         2050 100 100 2100
         2100 0 0 2000
         2000 0 0 2089
         2089 10 100 2030
         2030 0 0 1996

         **/

    }
}

