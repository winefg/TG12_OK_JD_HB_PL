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

    public Steuerung() {
        snakeSpiel = new ArrayList<>();
        highscoreDB = new ArrayList<>();
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
}