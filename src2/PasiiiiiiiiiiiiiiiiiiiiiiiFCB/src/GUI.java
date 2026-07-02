import processing.core.PApplet;

public class GUI extends PApplet {
    int state = 0; // 0 for first screen, 1 for second screen

    public void settings() {
        size(1000, 1000);

    }

    public void draw() {
        background(255f);
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


    public void drawSpielFeld() {
        background(70f, 84f, 40f);

        int cellSize = 40;
        int gridSize = 19;

        int fieldSize = gridSize * cellSize;
        // Anfang des Feldes
        int startX = (width - fieldSize) / 2;   // in der Mitte (X-Achse)
        int startY = 120;                        // oben Platz für Highscore machen

// Stroke um Feld
        stroke(0);        // schwarz рамка
        strokeWeight(2);    // толщина
        fill(179f, 214f, 101f);      // цвет фона поля
        rect(startX, startY, fieldSize, fieldSize);


        // Kästchen
        stroke(0f, 100f, 0f);        // белая рамка
        strokeWeight(0);    // толщина
        fill(172f, 208f, 94f);


        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {

                if ((x + y) % 2 == 1) {
                    rect(
                            startX + x * cellSize,
                            startY + y * cellSize,
                            cellSize,
                            cellSize
                    );
                }
            }
        }
        // Panel


        text("Highscore: 2048", 100, 40);

        text("Score: 0", 100, 80);

        // Button

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

    }







    public static void main(String[] args){
        PApplet.main("GUI");
    }
}
