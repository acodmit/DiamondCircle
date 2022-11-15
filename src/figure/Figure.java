package figure;

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

    public Figure( Colour colour){

        this.id = COUNT++;
        this.colour = colour;
        previousPath = new ArrayList<>();
        this.fallen = false;
        this.positionField = 0;
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
        if(isFinished() == true)
            finished = "( yes)";
        else
            finished = "( no)";
        return "Figure " + getId() + " ( " + getType() + ", " + getColour() +
                ") - previous path " + previousPath.toString() + "reached the end" + finished;
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


    public void moveByStep() {

        Matrix.PATH.get(positionIndex).setFigure(null);

        if (positionIndex + 1 < Matrix.PATH.size()) {

            if (Matrix.PATH.get(positionIndex).getFigure() == null) {
                Matrix.PATH.get(positionIndex).setFigure(this);
            }
            previousPath.add(positionField);
            positionIndex++;
            positionField = Matrix.PATH.get(positionIndex).getNumber();
            takeDiamondLocal(Matrix.PATH.get(positionIndex));
            System.out.println("CURRENT POSITION " + positionIndex + "." + " FIELD " + positionField);
            System.out.println("PLAYER " + Main.CURRENT_PLAYER.getID() + ". FIGURE "+ getId() + " MOVED FOR 1 STEP.");
        }

        if(positionIndex == Matrix.PATH.size() - 1)
            setFinished();

        try {
            Thread.sleep(1000);
        }catch (Exception ex) {
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }


    }
    private void takeDiamondLocal(Field field){
        if(field.takeDiamond()){
            diamonds++;
            System.out.println("FIGURE "+ getId() + " COLLECTED DIADMOND. NUMBER OF DIAMONDS "+ diamonds);
        }
    }

}
