public class Highscore {
    private int spielerID;
    private int spielID;
    private int score;
    private int highscoreRekord;


    public Highscore(int spielerID, int spielID, int highscoreRekord) {
        this.spielerID = spielerID;
        this.spielID = spielID;
        this.score = 0;
        this.highscoreRekord = highscoreRekord;
    }

    public Highscore() {
    }

    public int getSpielerID() {
        return spielerID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int highscore) {
        this.score = highscore;
    }

    public int getHighscoreRekord() {
        return highscoreRekord;
    }

    public void setHighscoreRekord(int highscoreRekord) {
        this.highscoreRekord = highscoreRekord;
    }
}