package figure;

import javafx.scene.layout.GridPane;

public class HoveringFigure extends Figure{

    public HoveringFigure( Colour colour){
        super( colour);
    }

    @Override
    public String getType(){
        return " HoveringFigure";
    }
}
