import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Snake_Original extends SnakeSpiel {
    private ArrayList<Zelle> zelleArrayList = new ArrayList<>();
    private int anzKoerperZellen;
    private Spielfeld spielfeld = new Spielfeld(17, 17);
    private char input = 'r';
    private Random random = new Random();
    private Zelle kopfzelle;
    private Aepfel apfel;

    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            switch (keyCode){
                case KeyEvent.VK_UP:
                    input = 'o';
                    break;
                case KeyEvent.VK_DOWN:
                    input = 'u';
                    break;
                case KeyEvent.VK_LEFT:
                    input = 'l';
                    break;
                case KeyEvent.VK_RIGHT:
                    input = 'r';
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };


    public Snake_Original(String name, int schwierigkeit, int anzKoerperZellen) {
        super(name, schwierigkeit);
        this.anzKoerperZellen = anzKoerperZellen;
    }

    public void spiel_Start(){
        kopfzelle = new Zelle(4, 7);
        zelleArrayList.add(kopfzelle);

        Zelle koerperZelle1 = new Zelle(3,7);
        Zelle koerperZelle2 = new Zelle(2,7);
        zelleArrayList.add(koerperZelle1);
        zelleArrayList.add(koerperZelle2);
        apfel = new Aepfel(12, 7);
    }

    public void laufen(){
        switch (input){
            case 'r':
                zelleArrayList.get(0).setX(zelleArrayList.get(0).getX()+1);
                laufenKoerper();
                System.out.println("Rechts laufen!");
                break;

            case 'l':
                zelleArrayList.get(0).setX(zelleArrayList.get(0).getX()-1);
                laufenKoerper();
                System.out.println("Links laufen!");
                break;

            case 'o':
                zelleArrayList.get(0).setY(zelleArrayList.get(0).getY()-1);
                laufenKoerper();
                System.out.println("Oben laufen!");
                break;

            case 'u':
                zelleArrayList.get(0).setY(zelleArrayList.get(0).getY()+1);
                laufenKoerper();
                System.out.println("Unten laufen!");
                break;
        }
    }

    private void laufenKoerper(){
        for (int i = 1; i<anzKoerperZellen; i++){
            zelleArrayList.get(i).setX(zelleArrayList.get(i-1).getX());
            zelleArrayList.get(i).setY(zelleArrayList.get(i-1).getY());
        }
    }

    public boolean checkVerloren(){
        for (int i=zelleArrayList.size()-1; i>0; i--){
            if (zelleArrayList.get(0).getX()==zelleArrayList.get(i).getX()&&zelleArrayList.get(0).getY()==zelleArrayList.get(i).getY()){
                System.out.println("trifft sich selbst");
                return true;
            } else if (zelleArrayList.get(0).getX()<0||zelleArrayList.get(0).getX()>spielfeld.getBreite()||zelleArrayList.get(0).getY()<0||zelleArrayList.get(0).getY()> spielfeld.getHoehe()) {
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
            for(int i = 0; i < zelleArrayList.size()-1; i++){
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
        boolean essen = false;
        if (apfel.getX() == zelleArrayList.get(0).getX() && apfel.getY() == zelleArrayList.get(0).getY()){
            essen = true;
        }
        return essen;
    }
}