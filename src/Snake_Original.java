import java.util.ArrayList;
import java.util.Random;

public class Snake_Original extends SnakeSpiel {
    private ArrayList<Zelle> zelleArrayList = new ArrayList<>();
    private int anzKoerperZellen;
    private Spielfeld spielfeld = new Spielfeld(19, 19); // Auf 19x19 angepasst, passend zur GUI!
    private char input = 'r';
    private Random random = new Random();
    private Zelle kopfzelle;
    private Aepfel apfel;

    // Verarbeitet den Tastendruck direkt aus Processing
    public void verarbeiteTastendruck(int keyCode) {
        // Wichtig: Verhindere, dass die Schlange direkt in sich selbst zurückläuft!
        switch (keyCode) {
            case 38, 87: // Code für Pfeiltaste OBEN (UP) oder W
                if (input != 'u') input = 'o';
                break;
            case 40, 83: // Code für Pfeiltaste UNTEN (DOWN) oder S
                if (input != 'o') input = 'u';
                break;
            case 37, 65: // Code für Pfeiltaste LINKS (LEFT) oder A
                if (input != 'r') input = 'l';
                break;
            case 39, 68: // Code für Pfeiltaste RECHTS (RIGHT) oder D
                if (input != 'l') input = 'r';
                break;
        }
    }

    public ArrayList<Zelle> getZelleArrayList() {
        return zelleArrayList;
    }

    public Snake_Original(String name, int schwierigkeit, int anzKoerperZellen) {
        super(name, schwierigkeit);
        this.anzKoerperZellen = anzKoerperZellen;
    }

    public void spiel_Start(){
        zelleArrayList.clear(); // Liste leeren, falls davor schon ein Spiel lief!

        kopfzelle = new Zelle(4, 7);
        zelleArrayList.add(kopfzelle);

        Zelle koerperZelle1 = new Zelle(3,7);
        Zelle koerperZelle2 = new Zelle(2,7);
        zelleArrayList.add(koerperZelle1);
        zelleArrayList.add(koerperZelle2);
        apfel = new Aepfel(12, 7);
        input = 'r'; // Startrichtung zurücksetzen
    }

    public void laufen(){
        laufenKoerper();

        switch (input){
            case 'r':
                zelleArrayList.get(0).setX(zelleArrayList.get(0).getX()+1);
                System.out.println("Rechts laufen!");
                break;

            case 'l':
                zelleArrayList.get(0).setX(zelleArrayList.get(0).getX()-1);
                System.out.println("Links laufen!");
                break;

            case 'o':
                zelleArrayList.get(0).setY(zelleArrayList.get(0).getY()-1);
                System.out.println("Oben laufen!");
                break;

            case 'u':
                zelleArrayList.get(0).setY(zelleArrayList.get(0).getY()+1);
                System.out.println("Unten laufen!");
                break;
        }
    }

    private void laufenKoerper() {
        for (int i = zelleArrayList.size() - 1; i > 0; i--) {
            zelleArrayList.get(i).setX(zelleArrayList.get(i - 1).getX());
            zelleArrayList.get(i).setY(zelleArrayList.get(i - 1).getY());
        }
    }


    public boolean checkVerloren(){
                Zelle kopf = zelleArrayList.get(0);

        // aus dem spielfeld (wand)
        if (kopf.getX() < 0 || kopf.getX() >= spielfeld.getBreite() ||
                kopf.getY() < 0 || kopf.getY() >= spielfeld.getHoehe()) {
            System.out.println("aus spielfeld");
            return true;
        }

        // körper berühren
        for (int i = 1; i < zelleArrayList.size(); i++) {
            Zelle koerperTeil = zelleArrayList.get(i);
            if (kopf.getX() == koerperTeil.getX() && kopf.getY() == koerperTeil.getY()) {
                System.out.println("crash");
                return true;
            }
        }
        return false;
    }



    public void generateApfel() {
        boolean collision;
        int newApfelX;
        int newApfelY;
        do {
            collision = false;
            newApfelX = random.nextInt(spielfeld.getBreite());
            newApfelY = random.nextInt(spielfeld.getHoehe());
            for (int i = 0; i < zelleArrayList.size(); i++) {
                if (newApfelX == zelleArrayList.get(i).getX() && newApfelY == zelleArrayList.get(i).getY()) {
                    collision = true;
                    break;
                }
            }
        } while (collision);
        apfel.setX(newApfelX);
        apfel.setY(newApfelY);
    }

    public void checkApfel(){
        if (apfel.getX() == zelleArrayList.get(0).getX() && apfel.getY() == zelleArrayList.get(0).getY()){
            Zelle letzteZelle = zelleArrayList.get(zelleArrayList.size() - 1);
            Zelle neueZelle = new Zelle(letzteZelle.getX(), letzteZelle.getY());
            zelleArrayList.add(neueZelle);
            anzKoerperZellen++;
            generateApfel();
        }
    }
}