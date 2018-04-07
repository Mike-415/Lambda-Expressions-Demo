package Assignment_M10;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class LineGUI extends Application implements DisplayLineInfo {

    private Pane pane;
    private BorderPane borderPane;
    private Circle startPoint, endPoint;
    private Line line;
    private Button slopeButton, distanceButton, midpointButton, vertHorzButton;
    private Text slopeText, distanceText, midpointText, vertHorxText;
    private TilePane slopePane, distancePane, midpointPane, vertHorzPane;

    private static final String TWO_POINT_MESSAGE = "Two points are required to make this calculation";
    private static final Alert TWO_POINT_ALERT = new Alert(Alert.AlertType.WARNING, TWO_POINT_MESSAGE);
    private static final int CIRCLE_RADIUS = 5;

    public void start(Stage primaryStage)
    {
        borderPane = new BorderPane();

        pane = new Pane();
        pane.setOnMouseClicked(this::handleMouseClicks);
        borderPane.setCenter(pane);

        slopeText = new Text("");
        slopeButton = new Button("Calculate Slope");
        slopeButton.setOnAction( event ->
        {
            double slope = 0;
            try
            {
                System.out.println(getInfo(line)); //Testing getInfo()
                slope = (line.getEndY() - line.getStartY()) / (line.getEndX() - line.getStartX()) * -1 ;
                //This conditional statement is used to prevent '-0.00', since 0 x -1 = 0
                if (slope == 0)
                {
                    slopeText.setText("\t0");
                }
                else
                {
                    //When you divide by zero, usually an ArithmeticException is thrown.
                    //But in this case, it returns an infinity.  Why?
                    slopeText.setText( (Double.isInfinite(slope)) ? "\tundefined" : "\t"+doubleF(slope) );
                }
            }
            catch (ArithmeticException exception)
            {
                exception.getLocalizedMessage();
            }
            catch (NullPointerException exception)
            {
                displayWarning(exception);

            }
        });
        slopePane = new TilePane(slopeButton, slopeText);
        slopePane.setAlignment(Pos.CENTER);

        distanceText = new Text("");
        distanceButton = new Button("Calculate Distance");
        // YOUR CODE HERE- ADD A STATEMENT TO SET THE ACTION OF THE BUTTON
         distanceButton.setOnAction( event ->
         {
             try
             {
                 System.out.println(getInfo(line));
                 double differenceOfXSquared = Math.pow(line.getEndX() - line.getStartX(), 2);
                 double differenceOfYSquared = Math.pow(line.getEndY() - line.getStartY(), 2);
                 double distance = Math.sqrt(differenceOfXSquared + differenceOfYSquared);
                 distanceText.setText("\t"+doubleF(distance));
             }
             catch (NullPointerException exception)
             {
                 displayWarning(exception);
             }
         });


        distancePane = new TilePane(distanceButton, distanceText);
        distancePane.setAlignment(Pos.CENTER);

        midpointText = new Text("");
        midpointButton = new Button("Calculate Midpoint");
        // YOUR CODE HERE- ADD A STATEMENT TO SET THE ACTION OF THE BUTTON
         midpointButton.setOnAction( event ->
         {
             try
             {
                 System.out.println(getInfo(line));
                 double xMidpoint = (line.getStartX() + line.getEndX()) / 2;
                 double yMidpoint = (line.getStartY() + line.getEndY()) / 2;
                 midpointText.setText("\t( "+doubleF(xMidpoint)+", "+doubleF(yMidpoint)+" )");
             }
             catch (NullPointerException exception)
             {
                 displayWarning(exception);
             }
         });

        midpointPane = new TilePane(midpointButton, midpointText);
        midpointPane.setAlignment(Pos.CENTER);

        vertHorxText = new Text("");
        vertHorzButton = new Button("Determine Vertical/Horizontal");
        // YOUR CODE HERE- ADD A STATEMENT TO SET THE ACTION OF THE BUTTON

        vertHorzButton.setOnAction( event ->
        {
            try
            {
                System.out.println(getInfo(line)); 
                boolean isVertical = false, isHorizontal = false;
                if(line.getStartX() == line.getEndX())
                {
                    isVertical = true;
                }
                if(line.getStartY() == line.getEndY())
                {
                    isHorizontal = true;
                }
                vertHorxText.setText("\tHorizontal: "+isHorizontal+", Vertical: "+isVertical);
            }
            catch (NullPointerException exception)
            {
                displayWarning(exception);
            }
        });

        vertHorzPane = new TilePane(vertHorzButton, vertHorxText);
        vertHorzPane.setAlignment(Pos.CENTER);



        VBox controlBox = new VBox(slopePane,distancePane,midpointPane,vertHorzPane);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setSpacing(10);
        borderPane.setBottom(controlBox);

        Scene scene = new Scene(borderPane, 500, 500, Color.WHITE);
        primaryStage.setTitle("Line Characteristics");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMouseClicks(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        
        if(startPoint==null ) { // no start point yet
            startPoint = new Circle(x,y,CIRCLE_RADIUS);
            pane.getChildren().add(startPoint);
            addCoordinateString(x,y);
            line = null;
        } else if(endPoint==null) { // start point, but not end point yet
            endPoint = new Circle(x,y,CIRCLE_RADIUS);
            pane.getChildren().add(endPoint);
            addCoordinateString(x,y);
            
            line = new Line(startPoint.getCenterX(), startPoint.getCenterY(), endPoint.getCenterX(), endPoint.getCenterY());
            pane.getChildren().add(line);
            
        } else { // startPoint != null && endPoint !=null
            // both start and end are there, so this is starting a new line

            // clear out everything from the last line
            pane.getChildren().clear();
            endPoint = null;
            line = null;
            distanceText.setText("");
            midpointText.setText(""); 
            vertHorxText.setText("");
            
            // start the new line
            startPoint = new Circle(x,y,CIRCLE_RADIUS);
            pane.getChildren().add(startPoint);        
            addCoordinateString(x,y);
        }

    }

    private String doubleF(double dbl)
    {
        return String.format("%,.2f", dbl);
    }

    private void displayWarning(Exception exception)
    {
        exception.getLocalizedMessage();
        TWO_POINT_ALERT.showAndWait()
                .filter(   response -> response == ButtonType.OK)
                .ifPresent(response -> System.err.println(TWO_POINT_MESSAGE) );
    }

    
    private void addCoordinateString(double x, double y) {
        String coordinateString = "(" + x + ", " + y + ")";
        Text coordinates = new Text(x-CIRCLE_RADIUS, y-CIRCLE_RADIUS-2, coordinateString);
        pane.getChildren().add(coordinates);
    }
  

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public String getInfo(Line line) {
        //DisplayLineInfo slope = DisplayLineInfo.createDisplayLineInfo(InfoType.SLOPE);
        //DisplayLineInfo distance = DisplayLineInfo.createDisplayLineInfo(InfoType.DISTANCE);
        //DisplayLineInfo midpoint = DisplayLineInfo.createDisplayLineInfo(InfoType.MIDPOINT);
        DisplayLineInfo vertHorz = DisplayLineInfo.createDisplayLineInfo(InfoType.VERTHORZ);
        return vertHorz.getInfo(line);
    }
}
