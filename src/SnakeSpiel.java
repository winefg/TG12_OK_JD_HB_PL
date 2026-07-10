public abstract class SnakeSpiel {
    private int id;
    private String name;
    private int schwierigkeit;
    private static int spieleNr = 0;

    public SnakeSpiel(String name, int schwierigkeit) {
        this.name = name;
        this.schwierigkeit = schwierigkeit;
        this.id = spieleNr;
        spieleNr++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSchwierigkeit() {
        return schwierigkeit;
    }
}