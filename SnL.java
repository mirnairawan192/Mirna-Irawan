import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnL {
    private ArrayList<Player> players;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    public int boardSize;
    public int gameStatus;
    private int nowPlaying;

    public SnL(int boardSize) {
        this.boardSize = boardSize;
        this.players = new ArrayList<>();
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
        this.gameStatus = 0;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getGameStatus() {
        return this.gameStatus;
    }

    public void play(String[] namaPemain, int[][] posisiSnake, int[][] posisiLadder) {
        for (String namaPemain1 : namaPemain) {
            Player player = new Player(namaPemain1);
            addPlayer(player);
        }

        for (int[] snake : posisiSnake) {
            addSnake(new Snake(snake[0], snake[1]));
        }

        // Add ladders
        for (int[] ladder : posisiLadder) {
            addLadder(new Ladder(ladder[0], ladder[1]));
        }
    }

    public void playying() {
        Player playerInTurn = getWhoseTurn();
        boolean rolledSix;
        do {
            int x = playerInTurn.rollDice();
            rolledSix = (x == 6);
            movePlayerAround(playerInTurn, x);
            GUI.logLabel2.setText(playerInTurn.getName());

            if (rolledSix) {
                GUI.logLabel12.setText(""+x);
                GUI.logLabel13.setText(""+playerInTurn.getPosition());;
                GUI.logLabel11.setText(playerInTurn.getName() + " gets another roll for rolling a 6!");
            } else {
                GUI.logLabel12.setText(""+x);
                GUI.logLabel13.setText(""+playerInTurn.getPosition());
                GUI.logLabel11.setText("");
            }
        } while (rolledSix);

        if (playerInTurn.getPosition() == boardSize) {
            gameStatus = 2;
            GUI.logLabel11.setText("Winner" + playerInTurn.getName());
        }
        displayBoard();
        Player nextPlayer = this.players.get((this.nowPlaying + 1) % this.players.size());
        GUI.logLabel2.setText(nextPlayer.getName());
    }

    public void addPlayer(Player s) {
        this.players.add(s);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void addSnake(Snake s) {
        this.snakes.put(s.getHeadPosition(), s.getTailPosition());
    }

    public void addLadder(Ladder l) {
        this.ladders.put(l.getBottomPosition(), l.getTopPosition());
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public Player getWhoseTurn() {
        if (this.gameStatus == 0) {
            this.gameStatus = 1;
            this.nowPlaying = (int) (Math.random() * this.players.size());
            return this.players.get(this.nowPlaying);
        } else {
            this.nowPlaying = (this.nowPlaying + 1) % this.players.size();
            return this.players.get(this.nowPlaying);
        }
    }

    public void movePlayerAround(Player p, int x) {
        p.moveAround(x, this.boardSize);
        if (ladders.containsKey(p.getPosition())) {
            GUI.logLabel11.setText(p.getName() + " mendapat tangga dari: " + p.getPosition() + " ke: " + ladders.get(p.getPosition()));
            p.setPosition(ladders.get(p.getPosition()));
        }
        if (snakes.containsKey(p.getPosition())) {
            GUI.logLabel11.setText(p.getName() + " terkena ular dari " + p.getPosition() + " turun ke " + snakes.get(p.getPosition()));
            p.setPosition(snakes.get(p.getPosition()));
        }
        if (p.getPosition() == this.boardSize) {
            this.gameStatus = 2;
        }
    }

    public void displayBoard() {
        String htmlText = "<html>";
        for (Player p : this.players) {
            htmlText += p.getName() + " berada di posisi " + p.getPosition() + "<br>";
        }
        htmlText += "</html>";
        GUI.logLabel9.setText(htmlText);
    }

//    public void displayBoard() {
//        String positions = "";
//        for (Player p : this.players) {
//            positions += p.getName() + " berada di posisi " + p.getPosition() + "\n";
//        }
//        GUI.logLabel9.setText(positions);
//    }

    public void resetGame() {
        for (Player p : this.players) {
            p.setPosition(0);
        }
        this.gameStatus = 0;
    }

}