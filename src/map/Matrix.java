package map;

import card.Card;
import card.RegularCard;
import card.SpecialCard;
import figure.GhostFigure;
import figure.HoveringFigure;
import main.GameController;
import main.Main;
import player.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import static java.lang.System.currentTimeMillis;

public class Matrix{

    public static Field[][] FIELDS;

    public static LinkedList<Card> CARDS;

    public static int MATRIX_SIZE;
    public static int NUMBER_OF_PLAYERS;

    public static int NUMBER_OF_PLAYERS_CURRENT;

    public static ArrayList<Field> PATH;

    public static int TURN = 0;

    public static BufferedWriter OUT;

    public ArrayList<Player> players;
    public ArrayList<Player> playersOrder;


    public static final Object LOCK = new Object();

    public Matrix(){

        NUMBER_OF_PLAYERS_CURRENT = NUMBER_OF_PLAYERS;
        FIELDS = new Field[MATRIX_SIZE][MATRIX_SIZE];

        for(int i = 0; i < MATRIX_SIZE; i++){
            for(int j = 0; j < MATRIX_SIZE; j++){
                FIELDS[i][j] = new Field();
            }
        }

        players = new ArrayList<>();
        for( int i = 0; i < NUMBER_OF_PLAYERS; i++){
            Player player = new Player();
            players.add(player);
        }


        playersOrder = (ArrayList<Player>)players.clone();
        Collections.shuffle(playersOrder);
        updateOrder();

        CARDS = new LinkedList<>();

        for(int i = 0; i < 40; i++){
            CARDS.add(new RegularCard()) ;
        }
        for(int i = 0; i < 12; i++){
            CARDS.add(new SpecialCard()) ;
        }
        Collections.shuffle(CARDS);

        PATH = new ArrayList<>();
        initializePath();

        System.out.println("MATRIX CREATED.");
    }


    public static List<String> readFileInList(String fileName) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }
        return lines;
    }

    public static void initializePath() {
        String pathFile;

        if(MATRIX_SIZE == 7){
            pathFile = "Resources/Paths/path_7.txt";
        }else if(MATRIX_SIZE == 8){
            pathFile = "Resources/Paths/path_8.txt";
        }else if(MATRIX_SIZE == 9){
            pathFile ="Resources/Paths/path_9.txt";
        } else {
            pathFile ="Resources/Paths/path_10.txt";
        }

        List<String> path;
        path = readFileInList(pathFile);

        for (String s : path) {
            String[] lines = s.split("#");
            PATH.add(FIELDS[Integer.parseInt(lines[0])][Integer.parseInt(lines[1])]);
        }

    }
    void updateOrder(){
        int count = 0;
        for(Player p : playersOrder){
            p.setOrder(count++);
        }
    }

    public void deletePlayer( Player player){
        NUMBER_OF_PLAYERS_CURRENT--;
        playersOrder.remove(player);
        updateOrder();
        if(NUMBER_OF_PLAYERS_CURRENT == 0){
            Main.GAME_FINISHED = true;
        }
    }

    public static Card getCard(){
        Card card = CARDS.pollFirst();
        CARDS.addLast(card);
        return card;
    }

    public void setHoles(){

        Random rand = new Random();
        int n = rand.nextInt(MATRIX_SIZE);

        while(n > 0){

            int position = rand.nextInt(Matrix.PATH.size());

            if(!Matrix.PATH.get(position).getHole()){

                Matrix.PATH.get(position).setHole(true);
                Matrix.PATH.get(position).setDiamond(false);

                if(Matrix.PATH.get(position).getFigure() != null){

                    if(!(Matrix.PATH.get(position).getFigure() instanceof HoveringFigure)){
                       Matrix.PATH.get(position).getFigure().setFallen();
                       Matrix.PATH.get(position).setFigure(null);
                    }
                }

                n--;
            }
        }
    }

    private void createGameFile() throws IOException {

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy -- HH-mm-ss") ;
        File file = new File(GameController.GAMES_FOLDER, dateFormat.format(date) + ".txt") ;
        OUT = new BufferedWriter(new FileWriter(file));

    }



    public void play(){

        long startTime = currentTimeMillis();
        try {
        createGameFile();
        }catch (IOException ex){
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }


        for(Player p : players){
            p.start();
        }

        Thread ghost = new Thread(new GhostFigure());
        //ghost.start();



    }

}
