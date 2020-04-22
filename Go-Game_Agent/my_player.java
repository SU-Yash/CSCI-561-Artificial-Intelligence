//>>>>>>>> // Submission 86 :: Alpha-Beta -> double : 0, 4, 4


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class player {
    int color; // 1 is black, 2 is white
    int[][] prev;
    int[][] curr;

    player(int c, int[][] p, int[][] cur){
        color = c;
        prev = p;
        curr = cur;
    }

    //
    public int totalOponentLiberty(Board_State board, int player){
        int totalL = 0;
        for(int i=0; i< 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(board.curr_board[i][j] == player){
                    totalL += calculatecumuLiberty(board, new Action(i, j), player);
                }
            }
        }
        return totalL;
    }

    public int calculatecumuLiberty(Board_State board, Action action, int player){
        int liberty = 0;
        int[][] temp = deepCopy(board.curr_board);
        List<Action> ally = allyDFS(board, player, action); //(n2)

        for(Action al: ally){
            List<Action> neighbours = getNeighbours(al);
            for(Action neigh : neighbours){
                if(temp[neigh.x][neigh.y] == 0){
                    temp[neigh.x][neigh.y] = -1;
                    liberty+=1;
                }

            }
        }
        return liberty;
    }

    //
    public int kills(Board_State board, int player){
        int killed = 0;
        for(int i=0; i< 5; i++){
            for(int j=0; j< 5; j++){
                if(board.curr_board[i][j] == player){
                    if(board.curr_board != prev){
                        killed += 1;
                    }
                }
            }
        }
        return killed;
    }


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Supporting functions  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public boolean checkIfStart(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (curr[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkForPass(player agent, Action action){
        if(agent.utility(agent.result(new Board_State(agent.prev, agent.curr), action, agent.color), agent.color) < agent.utility(new Board_State(new int[][]{}, agent.prev), agent.color)){
            return true;
        }
        return false;
    }

    public double partialArea(Board_State board, int player){
        int count = 0;
        for(int i =0; i<5;i++){
            for(int j=0; j< 5; j++){
                if(board.curr_board[i][j] == player){
                    count++;
                }
            }
        }
        return count;
    }


    public List<Action> getAllPossiblePoints(Board_State board, int player){
        List<Action> ans = new ArrayList<>();
        List<Action> corner = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board.curr_board[i][j] == 0) {
                    ans.add(new Action(i, j));
                }
            }
        }

        return ans;
    }

    public List<Action> getPlayerCoins(Board_State board, int player){
        List<Action> ans = new ArrayList<>();

        for(int i =0; i<board.curr_board.length;i++){
            for(int j=0; j< board.curr_board[0].length; j++){
                if(board.curr_board[i][j] == player){
                    ans.add(new Action(i, j));
                }
            }
        }
        return ans;
    }

    public List<Action> filterPoints(Board_State board, List<Action> allPoints, int player){
        List<Action> ans = new ArrayList<>();

        for(Action action: allPoints){

            // Make the move
            Board_State temp = new Board_State(board.prev_board, board.curr_board);
            temp.curr_board[action.x][action.y] = player;

            // Remove any opponent coins if required
            int killed = removeOponentCoins(temp, 3-player);

            // Check if it is a valid state
            if(!zeroLiberty(temp, player) && !KOViolation(temp, player)){
                //partialArea(temp, player);
                ans.add(action);
                //
                calculateLiberty(temp, action, player);
                //action.liberty += 10*killed;
                action.liberty += utility(temp, player);
                action.liberty -= totalOponentLiberty(temp, 3-player);
            }
            // Add to list if valid
        }
        Collections.sort(ans, (a, b)-> (int) (b.liberty - a.liberty));
        return ans;
    }

    public boolean zeroLiberty(Board_State board, int player){
        List<Action> coins = getPlayerCoins(board, player);

        for(Action action: coins){
            if(!findLiberty(board, action, player)){
                return true;
            }
        }
        return false;
    }

    public boolean KOViolation(Board_State board, int player){
        for(int i=0; i<5;i++){
            for(int j=0;j<5;j++) {
                if (board.curr_board[i][j] != board.prev_board[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public int removeOponentCoins(Board_State temp, int player){
        List<Action> allOponentCoins = getPlayerCoins(temp, player);

        List<Action> ans = new ArrayList<>();

        for(Action coin: allOponentCoins){
            if(!findLiberty(temp, coin, player)){
                ans.add(coin);
            }
        }
        int count = 0;
        //remove zero liberty coins
        for(Action coin: ans){
            temp.curr_board[coin.x][coin.y] = 0;
            count+=1;
        }
        return count;
    }

    public boolean findLiberty(Board_State board, Action p,  int player){
        int[][] temp = deepCopy(board.curr_board); //(n2)

        List<Action> ally = allyDFS(board, player, p); //(n2)

        for(Action coin: ally){
            List<Action> neighbours = getNeighbours(coin);
            for(Action neigh : neighbours){
                if(temp[neigh.x][neigh.y] == 0){
                    return true;
                }

            }
        }
        return false;
    }

    public List<Action> getNeighbours(Action p){
        List<Action> neighbours = new ArrayList<>();
        if(p.x>0) neighbours.add(new Action(p.x-1, p.y));
        if(p.x<4) neighbours.add(new Action(p.x+1, p.y));
        if(p.y>0) neighbours.add(new Action(p.x, p.y-1));
        if(p.y<4) neighbours.add(new Action(p.x, p.y+1));
        return neighbours;
    }

    public List<Action> getNeighboursAlly(Board_State board, Action p, int player){
        List<Action> ans = new ArrayList<>();
        List<Action> neighbours = getNeighbours(p);

        for (Action neighbour : neighbours){
            if(board.curr_board[neighbour.x][neighbour.y] == player){
                ans.add(neighbour);
            }
        }
        return ans;
    }

    public List<Action> allyDFS(Board_State board, int player, Action position){
        Stack<Action> stack = new Stack<>();
        List<Action> ally = new ArrayList<>();
        stack.add(position);

        while(!stack.isEmpty()){
            Action p = stack.pop();
            ally.add(p);
            for(Action neighbour : getNeighboursAlly(board, p, player)){
                if(!stack.contains(neighbour) && !ally.contains(neighbour)){
                    stack.add(neighbour);
                }
            }
        }
        return ally;
    }

    public void calculateLiberty(Board_State board, Action p, int player){
        int liberty = 0;
        int[][] temp = deepCopy(board.curr_board); //(n2)
        temp[p.x][p.y] = player;
        List<Action> ally = allyDFS(board, player, p); //(n2)

        for(Action al: ally){
            List<Action> neighbours = getNeighbours(al);
            for(Action neigh : neighbours){
                if(temp[neigh.x][neigh.y] == 0){
                    temp[neigh.x][neigh.y] = -1;
                    liberty+=1;
                }

            }
        }
        p.liberty = liberty;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< Supporting functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>> MinMax immediate supporting functions >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    // Return most fruitful operations for a given board and a given player
    public List<Action> goodActions(Board_State board, int player){
        // All possible actions
        List<Action> allPoints = getAllPossiblePoints(board, player);

        // Filter - invalid moves
        List<Action> filteredPoints = filterPoints(board, allPoints, player);

        // Choose Top 5 moves
        List<Action> ans = new ArrayList<>();


        for(int i=0; i < filteredPoints.size(); i++){
            ans.add(filteredPoints.get(i));
        }
        return ans;
    }

    // Return the updated board given a prev board and action
    public Board_State result(Board_State board, Action action, int player){
        Board_State updated = new Board_State(board.prev_board, board.curr_board);
        updated.prev_board = board.curr_board;
        updated.curr_board[action.x][action.y] = player;
        updated.level = board.level;
        removeOponentCoins(updated, 3-player);
        return updated;
    }

    public boolean is_terminal(Board_State board){
        if(board.level == 5) return true;
        return false;
    }

    public double utility(Board_State board, int player){
        //Partial Area Score + Full Area Score
        double black, white;
        double score = 0;
        black = partialArea(board, 1);
        white = partialArea(board, 2) + 2.5;
        if(player == 1){ // We are black
            score = (black - white);
        }
        else{
            score = (white - black);
        }
        //score += 10 * kills(board, 3-player);
        return score;
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< MinMax immediate supporting functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  MinMax  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
/*
    public Action minMaxDecision(Board_State board){
        int max_score = Integer.MIN_VALUE;
        int score = 0;
        Action bestAction = new Action(-1, -1);
        HashMap<Action, Integer> map = new HashMap<Action, Integer>();
        List<Action> goodAct = goodActions(board, color);
        for(Action action: goodAct){
            score = minValue(result(board, action, color));
            map.put(action, score);
            if(score > max_score){
                max_score = score;
                bestAction = action;
            }
        }
        return bestAction;
    }
    public int maxValue(Board_State board){
        if(is_terminal(board)) return utility(board, 3-color);
        board.level +=1;
        int v = Integer.MIN_VALUE;
        List<Action> goodAct = goodActions(board, color);
        for(Action action : goodAct){
            v = Math.max(v, minValue(result(board, action, color)));
        }
        return v;
    }
    public int minValue(Board_State board){
        if(is_terminal(board)) return utility(board, color);
        board.level +=1;
        int v = Integer.MAX_VALUE;
        List<Action> goodAct = goodActions(board, 3-color);
        for(Action action : goodAct){
            v = Math.min(v, maxValue(result(board, action, 3-color)));
        }
        return v;
    }
*/
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< MinMax <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  MinMax Alpha-Beta >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public Action alphaBetaSearch(Board_State board){
        double max_score = Integer.MIN_VALUE;
        double score = 0;
        Action bestAction = new Action(-1, -1);
        HashMap<Action, Double> map = new HashMap<Action, Double>();
        List<Action> goodAct = goodActions(board, color);
        for(Action action: goodAct){
            score = minValueAlphaBeta(result(board, action, color), Integer.MIN_VALUE, Integer.MAX_VALUE);
            map.put(action, score);
            if(score > max_score){
                max_score = score;
                bestAction = action;
            }
        }
        return bestAction;
    }

    public double maxValueAlphaBeta(Board_State board, double alpha, double beta){
        if(is_terminal(board)) return utility(board, 3-color);
        board.level +=1;
        double v = Integer.MIN_VALUE;
        for(Action action : goodActions(board, color)){
            v = Math.max(v, minValueAlphaBeta(result(board, action, color), alpha, beta));
            if(v >= beta) return v;
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    public double minValueAlphaBeta(Board_State board, double alpha, double beta){
        if(is_terminal(board)) return utility(board, color);
        board.level +=1;
        double v = Integer.MAX_VALUE;
        for(Action action : goodActions(board, 3 - color)){
            v = Math.min(v, maxValueAlphaBeta(result(board, action, 3-color), alpha, beta));
            if(v <= alpha) return v;
            beta = Math.min(beta, v);
        }
        return v;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< MinMax  Alpha-Beta <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static int[][] deepCopy(int[][] a){
        int[][] ans = new int[a.length][a[0].length];
        for(int i=0; i< a.length; i++){
            for(int j =0; j< a[0].length; j++){
                ans[i][j] = a[i][j];
            }
        }
        return ans;
    }
}

public class my_player{
    public static int[][] readArray(Scanner sc){
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

    public static player readInput() throws FileNotFoundException {
        File intput_file = new File("input.txt");
        Scanner sc = new Scanner(intput_file);

        int color = Integer.parseInt(sc.nextLine());
        int[][] prev = readArray(sc);
        int[][] curr = readArray(sc);

        player agent = new player(color, prev, curr);
        return agent;
    }

    public static void writeOutput(Action action) throws IOException {
        FileWriter myWriter = new FileWriter("output.txt");

        if(action.x == -1 && action.y == -1){
            myWriter.write("PASS");
            myWriter.close();
            return;
        }
        myWriter.write(action.x + "," + action.y);
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {

        boolean alphabeta = true;

        player agent = readInput();

        Action action = new Action(-1, -1);


        if(agent.checkIfStart()){ // Black player
            action = new Action(2, 2);

        }

        else {
            //if(!alphabeta) action = agent.minMaxDecision(new Board_State(agent.prev, agent.curr));
            action = agent.alphaBetaSearch(new Board_State(agent.prev, agent.curr));
        }

        /*
        if(agent.checkForPass(agent, action)){
            action = new Action(-1, -1);
        }
        */

        writeOutput(action);

    }
}


class Board_State{
    public int[][] prev_board;
    public int[][] curr_board;
    public int utility;
    public int level;

    Board_State(int[][] prev_board, int[][] curr_board){
        this.prev_board = player.deepCopy(prev_board);
        this.curr_board = player.deepCopy(curr_board);
        this.utility = 1;
        this.level = 1;
    }
}

class Action{
    int x;
    int y;
    double liberty;

    Action(int x, int y){
        this.x = x;
        this.y = y;
        liberty=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action position = (Action) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}