package figure;

public class RegularFigure extends Figure{

    public RegularFigure( Colour colour){
        super( colour);
    }

    @Override
    public String getType(){
        return " RegularFigure";
    }
}
