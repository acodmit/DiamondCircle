package figure;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import main.Main;
import map.Field;
import map.Matrix;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract public class Figure {

    private static int COUNT = 0;

    private int id;

    private final Colour colour;

    private ArrayList<Integer> previousPath;

    private boolean fallen;


    private int positionField;

    private int positionIndex;

    private boolean finished ;

    private long totalTime;

    private int diamonds;


    public Figure(Colour colour){

        this.id = COUNT++;
        this.colour = colour;
        previousPath = new ArrayList<>();
        this.fallen = false;
        this.positionField = Matrix.PATH.get(0).getNumber();
        this.positionIndex = 0;
        this.finished = false;
        this.totalTime = 0;
        this.diamonds = 0;
    }

    abstract String getType();

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        String finished = "";
        if(isFinished())
            finished = "( yes )";
        else
            finished = "( no )";
        return "Figure " + getId() + " ( " + getType() + ", " + getColour() +
                ") - previous path " + previousPath.toString() + ", reached the end " + finished + ", has fallen ( " + fallen + ").";
    }

    public Colour getColour(){
        return colour;
    }

    public boolean isFinished(){
        return finished;
    }

    public void setFinished(){
        this.finished = true;
    }

    public void setFallen(){
        fallen = true;
    }

    public boolean isFallen(){
        return fallen;
    }

    public void setPosition(int position){
        if (Matrix.PATH.get(positionIndex).getFigure() == null) {
            Matrix.PATH.get(positionIndex).setFigure(this);

        this.positionField = Matrix.PATH.get(position).getNumber();
        this.positionIndex = position;
        }
    }

    public void setTotalTime(long totalTime){
        this.totalTime = totalTime;
    }

    public int getDiamonds(){
        return diamonds;
    }

    public boolean checkPath( int position){
        if( previousPath.contains( position))
            return true;
        else
            return false;
    }


    public void moveByStep() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }

        synchronized (Matrix.LOCK) {

        if( Main.GAME_PAUSE) {
            try {
                Matrix.LOCK.wait();
            } catch (InterruptedException ex) {
                Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
            }
        }

        if(Matrix.PATH.get(positionIndex).figure == this)
            Matrix.PATH.get(positionIndex).setFigure(null);
        if (positionIndex + 1 < Matrix.PATH.size()) {

            previousPath.add(positionField);
            positionIndex++;
            positionField = Matrix.PATH.get(positionIndex).getNumber();
            diamonds += Matrix.PATH.get(positionIndex).takeDiamond();
            System.out.println("CURRENT POSITION " + positionIndex + "." + " FIELD " + positionField);
            System.out.println("PLAYER " + Main.CURRENT_PLAYER.getID() + ". FIGURE " + getId() + " MOVED FOR 1 STEP.");

            if (Matrix.PATH.get(positionIndex).getFigure() == null) {
                Matrix.PATH.get(positionIndex).setFigure(this);
            }else
                moveByStep();
        }else
            setFinished();
        }

    }

}
