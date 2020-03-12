import java.util.*;
import java.io.*;
import java.io.FileWriter;

public class my_player {
    Board previous_board;
    Board current_board;
    int player;

    class Position{
        int x;
        int y;
        int liberty;
        Position(int x, int y){
            this.x = x;
            this.y = y;
            liberty=0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    class Board{
        int[][] board;

        Board(int[][] board){
            this.board = board;
        }
    }

    my_player(){
        previous_board = null;
        current_board = null;
        player = 0;
    }

    my_player(Board prev, Board curr, int player){
        previous_board = prev;
        current_board = curr;
        this.player = player;
    }

    public void readInput(my_player agent) throws IOException {
        //int[][] prev = new int[][]{{0,0,0,0,0}, {0,0,0,0,0}, {0,0,0,0,0}, {0,0,0,0,0}, {0,0,0,0,0}};
        //int[][] curr = new int[][]{{0,0,0,0,0}, {0,0,0,0,0}, {0,0,1,0,0}, {0,0,0,0,0}, {0,0,0,0,0}};
        int[][] prev = new int[5][5];
        int[][] curr = new int[5][5];
        File intput_file = new File("input.txt");
        Scanner sc = new Scanner(intput_file);
        int player = Integer.parseInt(sc.nextLine());

        prev = readArray(sc);
        //sc.nextLine();
        curr = readArray(sc);
        /*print(prev);
        System.out.println();
        print(curr);
        System.out.println();*/

        agent = new my_player(new Board(prev), new Board(curr), player);

        List<Position> ans = agent.validMoves(agent.current_board, player);

        if(ans.size() != 0)
            writeOutput(ans.get(0), true);
        else
            writeOutput(new Position(0, 0), false);

        for(int i=0; i< ans.size(); i++){
            System.out.println(ans.get(i).x + " " + ans.get(i).y + " " + ans.get(i).liberty);
        }
    }

    public void print(int[][] arr){
        for(int i= 0; i< 5; i++){
            for(int j= 0; j< 5; j++){
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    public int[][] readArray(Scanner sc){
        int[][] ans = new int[5][5];
        for(int j= 0; j< 5; j++){
            String line = sc.nextLine();
            int[] row = new int[5];
            for(int i=0; i<line.length(); i++){
                row[i] = line.charAt(i) - '0';
            }
            ans[j] = row;
        }
        return ans;
    }

    public void writeOutput(Position p, boolean cont) throws IOException {
        FileWriter myWriter = new FileWriter("output.txt");
        if(cont == false){
            myWriter.write("PASS");
            myWriter.close();
            return;
        }
        myWriter.write(p.x + "," + p.y);
        myWriter.close();
    }

    public List<Position> validMoves(Board board, int player){
        if(checkIfStart()){
            return new ArrayList(Arrays.asList(new Position(2, 2)));
        }
        List<Position> ans = new ArrayList<>();
        List<Position> empty = getEmptyPlaces(board);
        setLiberties(empty, player);
        for(int i=0; i<5 && i<empty.size(); i++){
            ans.add(empty.get(i));
        }
        return ans;
    }

    public boolean checkIfStart(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (current_board.board[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setLiberties(List<Position> list, int player){
        for(Position elem : list){
            elem.liberty = calculateLiberty(elem, player);
        }
        Collections.sort(list, (a, b)-> b.liberty - a.liberty);
    }

    public int countDead(Board prev, Board curr) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (prev.board[i][j] != curr.board[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Position> getEmptyPlaces(Board curr){
        List<Position> ans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(curr.board[i][j] == 0){
                    ans.add(new Position(i, j));
                }
            }
        }
        return ans;
    }

    public int[][] deepCopy(int[][] a){
        int[][] ans = new int[a.length][a[0].length];
        for(int i=0; i< a.length; i++){
            for(int j =0; j< a[0].length; j++){
                ans[i][j] = a[i][j];
            }
        }
        return ans;
    }

    public int calculateLiberty(Position p, int player){
        int liberty = 0;
        int[][] temp = deepCopy(current_board.board); //(n2)
        temp[p.x][p.y] = player;
        List<Position> ally = allyDFS(player, p); //(n2)

        for(Position al: ally){
            List<Position> neighbours = getNeighbours(al);
            for(Position neigh : neighbours){
                if(temp[neigh.x][neigh.y] == 0){
                    temp[neigh.x][neigh.y] = -1;
                    liberty+=1;
                }

            }
        }
        return liberty;
    }

    public List<Position> allyDFS(int player, Position position){
        Stack<Position> stack = new Stack<>();
        List<Position> ally = new ArrayList<>();
        stack.add(position);

        while(!stack.isEmpty()){
            Position p = stack.pop();
            ally.add(p);
            for(Position neighbour : getNeighboursAlly(p, player)){
                if(!stack.contains(neighbour) && !ally.contains(neighbour)){
                    stack.add(neighbour);
                }
            }
        }
        return ally;
    }

    public List<Position> getNeighbours(Position p){
        List<Position> neighbours = new ArrayList<>();
        if(p.x>0) neighbours.add(new Position(p.x-1, p.y));
        if(p.x<4) neighbours.add(new Position(p.x+1, p.y));
        if(p.y>0) neighbours.add(new Position(p.x, p.y-1));
        if(p.y<4) neighbours.add(new Position(p.x, p.y+1));
        return neighbours;
    }

    public List<Position> getNeighboursAlly(Position p, int player){
        List<Position> ans = new ArrayList<>();
        List<Position> neighbours = getNeighbours(p);
        for (Position neighbour : neighbours){
            if(current_board.board[neighbour.x][neighbour.y] == player){
                ans.add(neighbour);
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        my_player agent = new my_player();

        agent.readInput(agent);

    }
}
