package max;

import java.util.ArrayList;
import java.util.TimerTask;
import java.net.URLEncoder;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Separator;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PopulationTreeApp extends Application {

    private Stage stage;
    private Scene scene;
    private VBox root;
    private Text worldPopulation;
    private Pane canvas;
    private Timeline loop;
    private Timeline resetLoop;
    private double canvasSize = 1000;

    private ArrayList<Point> points = new ArrayList<>();

    private int currentBirths = 0;
    private int currentDeaths = 0;
    private int db = 0;
    private int dd = 0;

    public void init() {
        buildContent();
        root = new VBox(canvas);
        scene = new Scene(root);
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setMinWidth(canvasSize);
        stage.setMinHeight(canvasSize);
        stage.setTitle("Population Tree");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        runNow(() -> trackCount());

        loop.play();
    }

    private void buildContent() {

        try {
            currentBirths = PopulationCount.getBirths();
            currentDeaths = PopulationCount.getDeaths();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loop = new Timeline();
        KeyFrame updateFrame = new KeyFrame(Duration.millis(100.0), e -> update());
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(updateFrame);

        worldPopulation = new Text("test");

        canvas = new Pane();
        canvas.setStyle("-fx-background-color: black;");
        canvas.setPrefSize(canvasSize, canvasSize);

        canvas.setOnMouseClicked((e) -> reset());

        points.add(new Point(canvasSize / 2, canvasSize / 2, 45, Color.CYAN));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 135, Color.LIGHTGREEN));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 225,
                Color.YELLOW));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 315, Color.SALMON));
    }

    private void update() {
        int sway = 80;

        int bi = db;
        for (int i = 0; i < bi; i++) {
            int pointsIndex = (int) (Math.random() * points.size());
            Point p1 = points.get(pointsIndex);
            if (points.size() > 1) {
                double a = p1.angle;
                p1.angle = clampAngle(a + Math.random() * sway - sway / 2);
                Color color = p1.color;

                p1.color = new Color(clampColor(color.getRed() + Math.random() * .2 - .1),
                        clampColor(color.getGreen() + Math.random() * .2 - .1),
                        clampColor(color.getBlue() + Math.random() * .2 - .1), .7);
                Point p2 = new Point(p1.x, p1.y, clampAngle(a + Math.random() * sway - sway / 2),
                        new Color(clampColor(color.getRed() + Math.random() * .2 - .1),
                                clampColor(color.getGreen() + Math.random() * .2 - .1),
                                clampColor(color.getBlue() + Math.random() * .2 - .1), .7));
                points.add(p2);
            }
        }

        int di = dd;
        for (int i = 0; i < di; i++) {
            int pointsIndex = (int) (Math.random() * points.size());
            if (points.size() > 2)
                points.remove(pointsIndex);
        }

        points.forEach((p) -> {
            p.x += Math.cos(Math.toRadians(p.angle)) * 2;
            p.y += Math.sin(Math.toRadians(p.angle)) * 2;
            Circle c = new Circle(p.x, p.y, 1, p.color);
            canvas.getChildren().add(c);
        });
    }

    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    }

    private void trackCount() {
        while (true) {
            try {
                int newBirths = PopulationCount.getBirths();
                db = newBirths - currentBirths;
                currentBirths = newBirths;
                int newDeaths = PopulationCount.getDeaths();
                dd = newDeaths - currentDeaths;
                currentDeaths = newDeaths;

                Thread.sleep(500);
            } catch (Exception e) {
                db = 8;
                dd = 4;
                e.printStackTrace();
            }
        }
    }

    private void reset() {
        loop.stop();

        points.clear();
        canvas.getChildren().clear();

        points.add(new Point(canvasSize / 2, canvasSize / 2, 45, Color.LIGHTBLUE));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 135, Color.LIGHTGREEN));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 225,
                Color.LIGHTYELLOW));
        points.add(new Point(canvasSize / 2, canvasSize / 2, 315, Color.PINK));

        db = 0;
        dd = 0;

        loop.play();
    }

    private double clampColor(double value) {
        if (value < 0)
            return 0;
        if (value > 1)
            return 1;
        return value;
    }

    private double clampAngle(double value) {
        if (value < 0)
            return 360 + value;
        if (value > 360)
            return value - 360;
        return value;
    }
}

class Point {
    public double x, y, angle;
    public Color color;

    Point(double x, double y, double angle, Color color) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.color = color;
    }

}
