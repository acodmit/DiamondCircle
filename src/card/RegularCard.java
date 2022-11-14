package card;

import main.Main;

public class RegularCard extends Card{

    private int number;

    public RegularCard(){
        super();
        setNumber();
    }

    private void setNumber(){
        if( id % 4 == 0){
            number = 1;
        }else if( id % 4 == 1){
            number = 2;
        }else if(id % 4 == 2){
            number = 3;
        }else{
            number = 4;
        }
    }
    public int getNumber(){
        return number;
    }

    public String toString(){
        return "Regular card with value " + getNumber() +
                ". Explanation: moves figure for " + getNumber() + " steps.";
    }

}
