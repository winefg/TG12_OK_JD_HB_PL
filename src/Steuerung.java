import db.MyJDBC;

import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Steuerung {
    private int anzSpieler;
    private int anzSpiele;
    private int anzHighscores;
    private int aktSpielerID;
    private int aktSpielID;

    private ArrayList<SnakeSpiel> snakeSpiel;

    Highscore highscore;

    //GUI-Zeug
    private GUI dieGUI;

    public Steuerung(GUI dieGUI) {
        snakeSpiel = new ArrayList<>();
        this.dieGUI = dieGUI;

    }

    public Snake_Original getAktuellesSpiel() {
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

            if (aktuellesSpiel.checkApfel()){
                dieGUI.geschwindigkeitMs -=2;
                highscore.setScore(highscore.getScore()+1);
                if (highscore.getHighscoreRekord()<highscore.getScore()){
                    highscore.setHighscoreRekord(highscore.getScore());
                }
            }

            if (aktuellesSpiel.checkVerloren()) {
                spielBeendet();
            }
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
        highscore = new Highscore(aktSpielerID, aktSpielID, MyJDBC.getScoreAusDatenbank(aktSpielerID));
    }

  /*  private int getIndexHighscore(int spielerID, int spielID) {

        for (int i = 0; i < highscore.size(); i++) {

            Highscore hs = highscore.get(i);

            if (hs.getSpielerID() == spielerID &&
                    hs.getSpielID() == spielID) {

                return i;
            }
        }

        return -1;
    }
    */

    public void anzeigenHighscore() {
        String hsS = "Highscore: " + Integer.toString(highscore.getHighscoreRekord());

        // Wichtig: Auf dem 'panel'-Objekt zeichnen und Styling setzen
        dieGUI.panel.fill(0); // Schwarze Textfarbe
        dieGUI.panel.textAlign(dieGUI.CENTER); // Zentriert (oder LEFT, je nach Wunsch)
        dieGUI.panel.text(hsS, 100, 40); // Text an Position x=100, y=40 schreiben
    }

    public void anzeigenScore(){
        String score = "Score: " + Integer.toString(highscore.getScore());

        dieGUI.panel.text(score, 100, 90);
    }

    public void spielBeendet() {
        System.out.println("Spiel vorbei");
        dieGUI.state = 4;
    }

}


