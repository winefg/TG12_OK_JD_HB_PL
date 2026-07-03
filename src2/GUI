import processing.core.PApplet;
import processing.core.PGraphics;

public class GUI extends PApplet {
    int state = 0; // 0 for first screen, 1 for second screen
    PGraphics spielFeld; //Variable fürs Spielfeld erstellen

    public void settings() {
        size(1000, 1000);
    }

    public void setup(){
        spielFeld = createGraphics(760, 760); //Größe in SETUP zuweisen
    }

    public void draw() {
        background(255);
// Name des Spiels
        fill(0);
        textAlign(CENTER);
        textSize(80);
        text("Alex lange Schlange", 500, 100);

// Start-Button
        fill(0);
        rect(400, 250, 200, 60);
        fill(255);
        textAlign(CENTER);
        textSize(30);
        text("START", 500, 290);

        if (state == 1) {
            drawSpielFeld();
        }

// Login
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

        // Panel
        fill(0);
        text("Highscore: 2048", 100, 40);
        text("Score: 0", 100, 80);
        // Button Zurück
        fill(100);
        rect(width - 170, 25, 140, 50, 10);
        fill(255);
        textAlign(CENTER, CENTER);
        text("Zurück", width - 100, 50);
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
