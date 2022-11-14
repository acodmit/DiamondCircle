package player;

import card.RegularCard;
import figure.*;
import main.Main;
import map.Matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import static java.lang.System.currentTimeMillis;

public class Player extends Thread {

    private static int COUNT = 0;

    private static final int NUMBER_OF_FIGURES = 4;

    private final int id;

    private final String name;

    private Colour colour;

    public ArrayList<Figure> figures;

    public boolean playingStatus;

    private int order;

    private long startTime;
    private  Figure currentFigure;

    private static boolean HOLES_SET = false;

    public Player() {
        id = COUNT++;
        playingStatus = true;
        name = "";
        figures = new ArrayList<>();
        startTime = 0;
        setColour();
        setFigures();
    }

    private void setColour(){
        if(id == 0)
            colour = Colour.RED;
        else if( id == 1)
            colour = Colour.YELLOW;
        else if( id == 2)
            colour = Colour.GREEN;
        else
            colour = Colour.BLUE;
    }

    private void setFigures() {
        Random rand = new Random();
        for (int i = 0; i < NUMBER_OF_FIGURES; i++) {
            int decide = rand.nextInt(3);
            if (decide == 0) {
                figures.add(new RegularFigure( colour));
            } else if (decide == 1) {
                figures.add(new HoveringFigure( colour));
            } else {
                figures.add(new SuperFastFigure( colour));
            }
        }
    }

    public void setOrder( int order) {
        this.order = order;
    }

    public String getPlayerName() {
        return name;
    }

    public int getID(){
        return id;
    }

    @Override
    public String toString() {
        return "Player " + id + " - " + this.getPlayerName();
    }

    public void updateGameFile(){

        try {
            Matrix.OUT.write(toString());
            Matrix.OUT.newLine();

            for (int j = 0; j < 4; j++) {
                Figure figure = this.figures.get(j);
                Matrix.OUT.write("        " + figure.toString());
                Matrix.OUT.newLine();
            }

            if (Matrix.NUMBER_OF_PLAYERS_CURRENT == 0) {
                long totalTime = (currentTimeMillis() - startTime) / 1000;
                Matrix.OUT.write("Total playing time: " + totalTime + "s");
            }

        }catch (IOException ex){
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }
    }


    @Override
    public void run() {

        System.out.println("PLAYER "+ id + " STARTED PLAYING.");

        for (int i = 0; i < NUMBER_OF_FIGURES; i++) {
            System.out.println("PLAYER "+ id + " PLAYING WITH " + figures.get(i).getId() +". FIGURE." );

            currentFigure = figures.get(i);
            Main.CURRENT_FIGURE = currentFigure;
            int step;
            int totalSteps = -1;

            while (totalSteps < Matrix.PATH.size()) {

                synchronized (Matrix.LOCK) {

                    while ( order != (Matrix.TURN % Matrix.NUMBER_OF_PLAYERS_CURRENT) || Main.GAME_PAUSE) {
                        try {
                            Matrix.LOCK.wait();
                        } catch (InterruptedException ex) {
                            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
                        }
                    }
                }


                if(HOLES_SET) {
                    synchronized (Matrix.LOCK) {
                        for (int i1 = 0; i1 < Matrix.MATRIX_SIZE; i1++) {
                            for (int j1 = 0; j1 < Matrix.MATRIX_SIZE; j1++) {
                                Matrix.FIELDS[i1][j1].setHole(false);
                            }
                        }
                        HOLES_SET = false;
                    }
                }

                System.out.println();
                System.out.println( "PLAYER " + this.id);


                Main.CURRENT_FIGURE = currentFigure;

                if (Main.CURRENT_FIGURE.isFinished() || Main.CURRENT_FIGURE.isFallen())
                    break;

                synchronized (Matrix.LOCK) {
                    if (totalSteps == -1) {
                        totalSteps = 0;
                        Main.CURRENT_FIGURE.setPosition(0);
                    }
                }

                Main.CURRENT_PLAYER = this;
                Main.CURRENT_CARD = Matrix.getCard();

                if (Main.CURRENT_CARD instanceof RegularCard)
                    step = ((RegularCard) Main.CURRENT_CARD).getNumber();
                else {
                    Main.MATRIX.setHoles();
                    HOLES_SET = true;
                    continue;
                }



                if(currentFigure instanceof SuperFastFigure) {
                    for (int j = 0; j < step; j++) {
                        synchronized (Matrix.LOCK) {
                            Main.CURRENT_FIGURE.moveByStep();
                            Main.CURRENT_FIGURE.moveByStep();
                        }
                    }
                }else{
                    for (int j = 0; j < step; j++) {
                        synchronized (Matrix.LOCK) {
                            Main.CURRENT_FIGURE.moveByStep();
                        }
                    }
                }

                for(int j = Main.CURRENT_FIGURE.getDiamonds(); j > 0; j--){
                    synchronized (Matrix.LOCK) {
                        Main.CURRENT_FIGURE.moveByStep();
                        step++;
                    }
                }

                totalSteps += step;


                synchronized (Matrix.LOCK){

                    Matrix.TURN++;
                    Matrix.LOCK.notifyAll();
                }

                }

            if(Main.CURRENT_FIGURE.isFinished())
                System.out.println("FIGURE " + Main.CURRENT_FIGURE.getId() + " HAS FINISHED.");
            else if(Main.CURRENT_FIGURE.isFallen())
                System.out.println("FIGURE " + Main.CURRENT_FIGURE.getId() + " HAS FALLEN.");

            long totalTime = System.currentTimeMillis() - startTime;
            Main.CURRENT_FIGURE.setTotalTime(totalTime);

        }

        synchronized(Matrix.LOCK){

            updateGameFile();
            Main.MATRIX.deletePlayer(this);
            Matrix.TURN++;
            Matrix.LOCK.notifyAll();
        }

    }
}