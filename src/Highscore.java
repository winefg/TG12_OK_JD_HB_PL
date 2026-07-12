public class Highscore {

    private int spielerID;
    private int spielID;
    private int highscore;          // aktueller Highscore des Spiels
    private int highscoreRekord;    // Rekord aus der Datenbank

    public Highscore(int spielerID, int spielID, int highscore) {
        this.spielerID = spielerID;
        this.spielID = spielID;
        this.highscore = highscore;
        this.highscoreRekord = 0;
    }

    public int getSpielerID() {
        return spielerID;
    }

    public int getSpielID() {
        return spielID;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public int getHighscoreRekord() {
        return highscoreRekord;
    }


    public void setHighscoreRekord(int highscoreRekord) {
        this.highscoreRekord = highscoreRekord;
    }
}