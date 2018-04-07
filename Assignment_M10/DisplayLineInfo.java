package Assignment_M10;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Line;

@FunctionalInterface

public interface DisplayLineInfo {

    public static final String TWO_POINT_MESSAGE = "Two points are required to make this calculation";
    public static final Alert TWO_POINT_ALERT = new Alert(Alert.AlertType.WARNING, TWO_POINT_MESSAGE);


    String getInfo(Line line);

    public static enum InfoType {
        SLOPE, DISTANCE, MIDPOINT, VERTHORZ;
    }

    public static DisplayLineInfo createDisplayLineInfo(InfoType type)
    {

        try
        {
            switch (type)
            {

                case SLOPE:
                    return (Line line) ->
                    {
                        double slope = 0;

                        slope = (line.getEndY() - line.getStartY()) / (line.getEndX() - line.getStartX()) * -1 ;
                        //This conditional statement is used to prevent '-0.00'.  I thought 0 * -1 = 0
                        if (slope == 0)
                        {
                            return "\t0";
                        }
                        else
                        {
                            //When you divide by zero, usually an ArithmeticException is thrown.
                            //But in this case, it returns an infinity.  Why?
                            return (Double.isInfinite(slope)) ? "\tundefined" : "\t"+String.format( "%,.2f" , slope );
                        }
                    };

                case DISTANCE:
                    return (Line line) ->
                    {

                        double differenceOfXSquared = Math.pow(line.getEndX() - line.getStartX(), 2);
                        double differenceOfYSquared = Math.pow(line.getEndY() - line.getStartY(), 2);
                        String distance = String.format("%,.2f", Math.sqrt(differenceOfXSquared + differenceOfYSquared));
                        return "\t"+distance;
                    };

                case MIDPOINT:
                    return line -> {

                        String xMidpoint = String.format( "%,.2f" , (line.getStartX() + line.getEndX()) / 2);
                        String yMidpoint = String.format( "%,.2f" , (line.getStartY() + line.getEndY()) / 2);
                        return "Midpoint: ( "+xMidpoint+", "+yMidpoint+" )";
                    };


                case VERTHORZ:
                    return line ->
                    {

                        boolean isVertical = false, isHorizontal = false;
                        if(line.getStartX() == line.getEndX())
                        {
                            isVertical = true;
                        }
                        if(line.getStartY() == line.getEndY())
                        {
                            isHorizontal = true;
                        }
                        return "\tHorizontal: "+isHorizontal+", Vertical: "+isVertical;
                    };
            }
        }
        catch (ArithmeticException exception)
        {
            exception.getLocalizedMessage();
        }
        catch (NullPointerException exception)
        {
            exception.getLocalizedMessage();
            TWO_POINT_ALERT.showAndWait()
                    .filter(   response -> response == ButtonType.OK)
                    .ifPresent(response -> System.err.println(TWO_POINT_MESSAGE) );
        }

        return null;
    }

}
