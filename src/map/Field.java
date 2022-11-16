package map;

import figure.Figure;

public class Field {
    private static int COUNT = 0;
    private int number;

    private final int X;

    private final int Y;

    public Figure figure;

    private boolean diamond;

    private boolean hole;

    public Field (){
        this.number = COUNT++;
        this.X = number / Matrix.MATRIX_SIZE;
        this.Y = number % Matrix.MATRIX_SIZE;
    }
    public int getX(){
        return X;
    }

    public int getY(){
        return Y;
    }

    public int getNumber(){
        return number;
    }


    public void setFigure(Figure figure){
        this.figure = figure;
    }

    public Figure getFigure(){
        return figure;
    }

    public void setDiamond( boolean diamond){
        this.diamond = diamond;
    }

    public boolean getDiamond(){
        return diamond;
    }

    public boolean takeDiamond(){
        if(getDiamond()){
            diamond = false;
            return true;
        }else
            return false;
    }

    public void setHole(boolean hole){
        this.hole = hole;
    }

    public boolean getHole(){
        return hole;
    }

}
