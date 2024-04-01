package max;

import javafx.application.Application;

public class App {
    public static void main(String[] args) {
        try {
            Application.launch(PopulationTreeApp.class, args);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } // try
    }
}
