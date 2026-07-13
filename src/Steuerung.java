import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Steuerung {
    private int anzSpieler;
    private int anzSpiele;
    private int anzHighscores;
    private int aktSpielerID;
    private int aktSpielID;

    private ArrayList<SnakeSpiel> snakeSpiel;
    private ArrayList<Highscore> highscoreDB;


    //GUI-Zeug
    private GUI dieGUI;

    public Steuerung(GUI dieGUI) {
        snakeSpiel = new ArrayList<>();
        highscoreDB = new ArrayList<>();
        this.dieGUI = dieGUI;

    }

    private Snake_Original getAktuellesSpiel() {
        if (snakeSpiel == null || snakeSpiel.isEmpty()) {
            return null; // Verhindert den Absturz, wenn noch kein Spiel gestartet wurde
        }
        return (Snake_Original) snakeSpiel.getFirst();
    }

    Snake_Original ss = new Snake_Original("ADFPasi", 67, 2);
    public void doLaufen() {
        Snake_Original aktuellesSpiel = getAktuellesSpiel();
        if (aktuellesSpiel != null) {
            aktuellesSpiel.laufen();
        }
    }

    // Wird von der GUI aufgerufen, sobald eine Taste gedrückt wurde
    public void checkInput(int keyCode) {
        Snake_Original aktuellesSpiel = getAktuellesSpiel();
        if (aktuellesSpiel != null) {
            aktuellesSpiel.verarbeiteTastendruck(keyCode);
        }
    }

    public void addSpiel(SnakeSpiel spiel) {
        snakeSpiel.add(spiel);
        anzSpiele++;
    }

    public void addHighscore(Highscore hs) {
        highscoreDB.add(hs);
        anzHighscores++;
    }

    private int getIndexHighscore(int spielerID, int spielID) {

        for (int i = 0; i < highscoreDB.size(); i++) {

            Highscore hs = highscoreDB.get(i);

            if (hs.getSpielerID() == spielerID &&
                    hs.getSpielID() == spielID) {

                return i;
            }
        }

        return -1;
    }

    //public void login(String name, String passwort){

   // }

    //private int getSpielerID(String name, String passwort){

   // }


    public void anzeigenHighscore(){
        int hs =  highscoreDB.get(0).getHighscore();
        String hsS = Integer.toString(hs);
        dieGUI.panel.text(hsS, 100, 40);
    }
}


