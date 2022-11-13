package figure;

import main.Main;
import map.Matrix;

import java.util.Random;
import java.util.logging.Level;

public class GhostFigure implements Runnable{


    @Override
    public void run() {

        while(!Main.GAME_FINISHED){

            synchronized (Matrix.LOCK){

                while(Main.GAME_PAUSE){
                    try {
                        Matrix.LOCK.wait();
                    } catch (InterruptedException ex) {
                        Main.LOGGER.log(Level.WARNING,ex.fillInStackTrace().toString(), ex);
                    }
                }


                for( int i = 0; i < Matrix.MATRIX_SIZE; i++){
                    for (int j = 0; j < Matrix.MATRIX_SIZE; j++){
                        Matrix.FIELDS[i][j].setDiamond(false);
                    }
                }

                Random rand = new Random();
                int n = rand.nextInt( Matrix.MATRIX_SIZE - 1) + 1;
                int position;
                int m = 0;
                System.out.println("GHOST PUTS " + n + " DIAMONDS.");

                while(m < n){

                    position = rand.nextInt(Matrix.PATH.size());

                    if (Matrix.PATH.get(position).getDiamond()) {
                        continue;
                    }
                    Matrix.PATH.get(position).setDiamond(true);
                    m++;
                }

                Matrix.LOCK.notifyAll();
            }

            try{
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Main.LOGGER.log( Level.WARNING, ex.fillInStackTrace().toString(), ex);
            }

        }
    }
}
