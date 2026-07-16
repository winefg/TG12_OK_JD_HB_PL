import db.MyJDBC;
import java.util.ArrayList;

public class Steuerung {
    private int anzSpiele;
    private int aktSpielerID;
    private int aktSpielID;

    private ArrayList<SnakeSpiel> snakeSpiel;
    public Highscore highscore;
    private GUI dieGUI;


    public Steuerung(GUI dieGUI) {
        snakeSpiel = new ArrayList<>();
        this.dieGUI = dieGUI;
    }

    public void setAktSpielerID(int id) {
        this.aktSpielerID = id;
    }

    public int getHighscore() {
        if (highscore == null) {
            return 0;
        }
        return highscore.getHighscoreRekord();
    }

    public int getScore() {
        if (highscore == null) {
            return 0;
        }
        return highscore.getScore();

        public Snake_Original getAktuellesSpiel () {
            if (snakeSpiel == null || snakeSpiel.isEmpty()) {
                return null; // Verhindert den Absturz, wenn noch kein Spiel gestartet wurde
            }
            return (Snake_Original) snakeSpiel.getFirst();
        }

        public void doLaufen () {
            Snake_Original aktuellesSpiel = getAktuellesSpiel();
            if (aktuellesSpiel != null) {
                aktuellesSpiel.laufen();
                if (aktuellesSpiel.checkApfel()) {
                    if (dieGUI.geschwindigkeitMs > 300) {
                        dieGUI.geschwindigkeitMs -= 2;
                    } else if (dieGUI.geschwindigkeitMs <= 300) {
                        if ((aktuellesSpiel.getAnzKoerperZellen() - 2) % 4 == 0) dieGUI.geschwindigkeitMs -= 1;
                    }
                    highscore.setScore(highscore.getScore() + 1);
                }

                if (aktuellesSpiel.checkVerloren()) {
                    spielBeendet(4);
                }
                if (aktuellesSpiel.getSpielfeld().getHoehe() * aktuellesSpiel.getSpielfeld().getBreite() == aktuellesSpiel.getZelleArrayList().size()) {
                    spielBeendet(5);
                }
            }
        }

        // Wird von der GUI aufgerufen, sobald eine Taste gedrückt wurde
        public void checkInput ( int keyCode){
            Snake_Original aktuellesSpiel = getAktuellesSpiel();
            if (aktuellesSpiel != null) {
                aktuellesSpiel.verarbeiteTastendruck(keyCode);
            }
        }

        public void addSpiel (SnakeSpiel spiel){
            snakeSpiel.clear();
            snakeSpiel.add(spiel);
            anzSpiele++;
            int alterHighscore = MyJDBC.getHighscore(aktSpielerID);     // Highscore aus DB nehmen
            System.out.println("Loaded Highscore from DB: " + alterHighscore);
            highscore = new Highscore(
                    aktSpielerID,
                    aktSpielID,
                    alterHighscore);
        }

        public void anzeigenHighscore () {
            if (highscore == null) {
                return;
            }
            String highscoreString = "Highscore: " + Integer.toString(highscore.getHighscoreRekord());

            // Wichtig: Auf dem 'panel'-Objekt zeichnen und Styling setzen
            dieGUI.panel.fill(0); // Schwarze Textfarbe
            dieGUI.panel.textAlign(dieGUI.CENTER); // Zentriert (oder LEFT, je nach Wunsch)
            dieGUI.panel.text(highscoreString, 100, 40); // Text an Position x=100, y=40 schreiben
        }

        public void anzeigenScore () {
            String score = "Score: " + Integer.toString(highscore.getScore());

            dieGUI.panel.text(score, 100, 90);
        }

        public void spielBeendet ( int state){
            // Überprüfen, was in Highscore gibt
            System.out.println("Player ID: " + highscore.getSpielerID());
            System.out.println("Current score: " + highscore.getScore());
            System.out.println("Old highscore: " + highscore.getHighscoreRekord());

            if (highscore.getScore() > highscore.getHighscoreRekord()) {
                MyJDBC.updateHighscore(     // neuer Highscore wenn Score > alter Highscore
                        highscore.getSpielerID(),
                        highscore.getScore()
                );
                // update Highscore in Java-Programm
                highscore.setHighscoreRekord(highscore.getScore());
            }
            System.out.println("Spiel vorbei");
            dieGUI.state = state;
        }
    }
}


