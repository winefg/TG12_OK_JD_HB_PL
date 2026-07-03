import processing.core.PApplet;
import processing.core.PGraphics;

public class GUI extends PApplet {
    int state = 0; // 0 for first screen, 1 for second screen
    PGraphics spielFeld; //Variable fürs Spielfeld erstellen
    PGraphics panel;

    public void settings() {
        size(1000, 1000);
    }

    public void setup(){
        spielFeld = createGraphics(760, 760); //Größe in SETUP zuweisen
        panel = createGraphics(1000, 1000);
    }

    public void draw() {
        if (state==0){
            drawStartPage();
        } else if (state == 1) {
            rect(0,0,1000,1000);
            drawPanelSpielfeld();
            drawSpielFeld();
            state=2;
        } else if (state==2) {
            drawSnake();
        }
    }


    void drawSpielFeld() {
        spielFeld.beginDraw(); //bevor man beginnt in den Layer zu zeichnen
        spielFeld.background(70, 84, 40); //immer die normalen Funktionen von Processing verwenden + Name der Variable davor

        int cellSize = 40;
        int gridSize = 19;

        int fieldSize = gridSize * cellSize;

// Stroke um Feld
        spielFeld.stroke(0);
        spielFeld.strokeWeight(2);
        spielFeld.fill(179, 214, 101);
        spielFeld.rect(0, 0, fieldSize, fieldSize);


        // Kästchen
        spielFeld.stroke(0, 100, 0);
        spielFeld.strokeWeight(0);
        spielFeld.fill(172, 208, 94);


        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {

                if ((x + y) % 2 == 1) {
                    spielFeld.rect(
                            x * cellSize,
                            y * cellSize,
                            cellSize,
                            cellSize
                    );
                }
            }
        }
        spielFeld.endDraw(); //Wenn fertig mit Zeichnen in diesem Layer
        image(spielFeld, 120, 120); //Das gezeichnete anzeigen, mit Koordinaten auf dem Übergeordneten Spielfeld
    }
    void drawPanelSpielfeld(){

        // Panel
        panel.beginDraw();
        panel.textAlign(CENTER);
        panel.textSize(30);
        panel.fill(0);
        panel.text("Highscore: 2048", 100, 40);
        panel.text("Score: 0", 100, 80);
        // Button Zurück
        panel.fill(100);
        panel.rect(width - 170, 25, 140, 50, 10);
        panel.fill(255);
        panel.textAlign(CENTER, CENTER);
        panel.text("Zurück", width - 100, 50);
        panel.endDraw();
        image(panel, 0,0);
    }

    void drawStartPage() {
        background(255);
// Name des Spiels
        fill(0);
        textAlign(CENTER);
        textSize(80);
        text("Schlange", 500, 100);

// Start-Button
        fill(0);
        rect(400, 250, 200, 60);
        fill(255);
        textAlign(CENTER);
        textSize(30);
        text("START", 500, 290);
    }

    void drawSnake(){
        rect(405,405,30,30);
    }

    public void mousePressed() {
        if (mouseX > 400 &&
                mouseX < 600 &&
                mouseY > 250 &&
                mouseY < 310) {
            println("Start gedrückt!");
            state = 1;
        }
        if (mouseX > width - 170 &&
                mouseX < width - 30 &&
                mouseY > 25 &&
                mouseY < 75) {
            println("Zurück gedrückt!");
            state = 0;
        }
    }

    public static void main(String[] args) {
        PApplet.main("GUI"); // Launch sketch
    }
}
