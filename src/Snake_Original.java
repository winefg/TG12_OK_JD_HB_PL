import java.util.ArrayList;
import java.util.Random;

public class Snake_Original extends SnakeSpiel {
    private int anzKoerperZellen;
    private char input;
    private char lastInput;

    private ArrayList<Zelle> zelleArrayList = new ArrayList<>();
    private Zelle kopfzelle;
    private Spielfeld spielfeld = new Spielfeld(17, 17);
    private Aepfel apfel;
    private Random random = new Random();


    public Snake_Original(String name, int schwierigkeit, int anzKoerperZellen) {
        super(name, schwierigkeit);
        this.anzKoerperZellen = anzKoerperZellen;
    }

    public ArrayList<Zelle> getZelleArrayList() {
        return zelleArrayList;
    }

    public Aepfel getApfel() {
        return apfel;
    }

    public Spielfeld getSpielfeld() {
        return spielfeld;
    }

    public void spiel_Start(){
        input = 'r';
        lastInput = 'r';
        zelleArrayList.clear();


        kopfzelle = new Zelle(4, 7);
        zelleArrayList.add(kopfzelle);

        Zelle koerperZelle1 = new Zelle(3,7);
        Zelle koerperZelle2 = new Zelle(2,7);
        zelleArrayList.add(koerperZelle1);
        zelleArrayList.add(koerperZelle2);
        apfel = new Aepfel(12, 7);
    }

    // Verarbeitet den Tastendruck direkt aus Processing
    public void verarbeiteTastendruck(int keyCode) {
        // Processing nutzt standardmäßig 'CODED' für Pfeiltasten
        // und stellt Variablen wie UP, DOWN, LEFT, RIGHT bereit (als int)

        // Wichtig: Verhindere, dass die Schlange direkt in sich selbst zurückläuft!
        switch (keyCode) {
            case 38, 87: // Code für Pfeiltaste OBEN (UP)
                if (lastInput != 'u') input = 'o';
                break;
            case 40, 83: // Code für Pfeiltaste UNTEN (DOWN)
                if (lastInput != 'o') input = 'u';
                break;
            case 37, 65: // Code für Pfeiltaste LINKS (LEFT)
                if (lastInput != 'r') input = 'l';
                break;
            case 39, 68: // Code für Pfeiltaste RECHTS (RIGHT)
                if (lastInput != 'l') input = 'r';
                break;
        }
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
        lastInput = input;
    }

    private void laufenKoerper() {
        for (int i = zelleArrayList.size() - 1; i > 0; i--) {
            zelleArrayList.get(i).setX(zelleArrayList.get(i - 1).getX());
            zelleArrayList.get(i).setY(zelleArrayList.get(i - 1).getY());
        }
    }

    public boolean checkVerloren(){
        for (int i=zelleArrayList.size()-1; i>0; i--){
            if (zelleArrayList.get(0).getX()==zelleArrayList.get(i).getX()&&zelleArrayList.get(0).getY()==zelleArrayList.get(i).getY()){
                System.out.println("trifft sich selbst");
                return true;
            } else if (zelleArrayList.get(0).getX()<0||zelleArrayList.get(0).getX()>spielfeld.getBreite()+1||zelleArrayList.get(0).getY()<0||zelleArrayList.get(0).getY()> spielfeld.getHoehe()+1) {
                System.out.println("geht aus Spielfeld");
                return true;
            }
        }
        System.out.println(zelleArrayList.get(1).getX());
        return false;
    }

    public void generateApfel() {
        boolean collision = true;
        int newApfelX;
        int newApfelY;
        do{
            collision = false;
            newApfelX = random.nextInt(spielfeld.getBreite());
            newApfelY = random.nextInt(spielfeld.getHoehe());
            for(int i = 0; i < zelleArrayList.size(); i++){
                if(newApfelX == zelleArrayList.get(i).getX() && newApfelY == zelleArrayList.get(i).getY()){
                    collision = true;
                    break;
                }
            }
        } while (collision);
        apfel.setX(newApfelX);
        apfel.setY(newApfelY);
    }

    public boolean checkApfel(){
        if (apfel.getX() == zelleArrayList.getFirst().getX() && apfel.getY() == zelleArrayList.getFirst().getY()){
            Zelle letzteZelle = zelleArrayList.getLast();
            Zelle neueZelle = new Zelle(letzteZelle.getX(), letzteZelle.getY());
            zelleArrayList.add(neueZelle);
            anzKoerperZellen++;
            generateApfel();
            return true;
        }
        return false;
    }
}
