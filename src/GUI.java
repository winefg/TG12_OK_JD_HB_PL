import db.MyJDBC;
import processing.core.PApplet;
import processing.core.PGraphics;
import static db.MyJDBC.*;



public class GUI extends PApplet {
    Steuerung steuerung;
    int state = 0; // 0 for first screen, 1 for second screen
    PGraphics spielFeld; //Variable fürs Spielfeld erstellen
    PGraphics panel;
    PGraphics login;
    PGraphics schlange;
    PGraphics apfel;
    PGraphics endScreen;
    PGraphics gameOver;
    int letzterSchrittZeit = 0;
    int geschwindigkeitMs = 400; // Alle 400 Millisekunden ein Schritt (kleiner = schneller)

    public void settings() {
        size(1000, 1000);
        pixelDensity(1);
    }

    public void setup(){
        steuerung = new Steuerung(this);
        spielFeld = createGraphics(760, 760); //Größe in SETUP zuweisen
        panel = createGraphics(1000, 1000);
        steuerung.addSpiel(steuerung.ss);
        login = createGraphics(500, 350);
        schlange = createGraphics(760, 760);
        apfel = createGraphics(760, 760);
        gameOver = createGraphics(760, 760);
        endScreen = createGraphics(1000, 1000);
    }

    @Override
    public void keyPressed() {
        // 'keyCode' ist eine eingebaute globale Variable in Processing
        if (steuerung != null) {
            steuerung.checkInput(keyCode);
        }
    }

    public void draw() {
        if (state==0){                  //Startbildschirm
            drawStartPage();
        } else if (state == 1) {                //Hintergrund zeichnen
            rect(0,0,1000,1000);
            drawPanelSpielfeld();
            drawSpielFeld();
            state=2;
        } else if (state==2) {          //Spiel konstant zeichnen
            if (millis()-letzterSchrittZeit >= geschwindigkeitMs){
                letzterSchrittZeit = millis();
                steuerung.doLaufen();
            }
            drawPanelSpielfeld();
            drawSpielFeld();
            drawSnake();
            drawApfel();
        } else if (state==3) {          //Login-Bildschirm
            drawPanelLogin();
            image(login, 250, 600);
        }
        else if (state == 4) {          //Game-Over-Bildschirm
            drawGameOver();
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
        //spielFeld.stroke(0, 100, 0);
        //spielFeld.strokeWeight(0);
        spielFeld.noStroke();
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
        image(spielFeld, 120, 120); //Das gezeichnete anzeigen, mit Koordinaten auf dem Uebergeordneten Spielfeld
    }

    int focusedField = 0;

    String nickname = " ";
    boolean nicknameActive = false;
    String nicknameRegister = "";

    String password = "";
    boolean passwordActive = false;
    String passwordRegister = "";

    boolean register = false;
    boolean loginSucces = false;

    void drawPanelLogin() {
        login.beginDraw();
        login.background(200);

        //if (focusedField == 1 || focusedField == 11) {}

        // Nickname Text
        login.textSize(30);
        login.text("Nickname: ", 40, 70);
        // Nickname Field
        login.fill( 0);
        login.rect(260, 30, 220, 60);
        login.fill(255);

        if (register == true) {
            login.text(nicknameRegister, 270, 70);
        } else {
            login.text(nickname, 270, 70);
        }




        // Password Text
        login.text("Password: ", 40, 150);
        // Pasword Field
        login.fill(0);
        login.rect(260, 110, 220, 60);
        login.fill(255);
        login.textSize(30);
        if (register == true) {
            login.text(passwordRegister, 270, 150);
        } else {
            login.text(password, 270, 150);
        }


        // Submit Button
        login.fill(100);
        login.rect(125,270, 250, 60);
        login.fill(255);
        login.textSize(45);
        login.text("SUBMIT", 170, 315);


        // Not registered
        login.fill(100);
        login.rect(125, 200, 250, 20);
        login.fill(255);
        login.textSize(15);
        login.text("Not registered yet?", 190, 215);
        login.endDraw();


    }


    void drawPanelSpielfeld(){
        // Panel-Zeichnung starten
        panel.beginDraw();
        panel.background(220); // Empfohlen: Hintergrund säubern, sonst clippt der Text beim Neuzeichnen

        panel.textAlign(CENTER);
        panel.textSize(30);
        panel.fill(0);

        // Ruft die Steuerung auf, die den Highscore auf das Panel zeichnet
        steuerung.anzeigenHighscore();

        steuerung.anzeigenScore();
        // Normaler Score (etwas nach unten versetzt bei y=90, damit es nicht kollidiert)
        //panel.text("Score: 0", 100, 90);

        // Button Zurück
        panel.fill(100);
        panel.rect(width - 170, 25, 140, 50, 10);
        panel.fill(255);
        panel.textAlign(CENTER, CENTER);
        panel.text("Zurück", width - 100, 50);

        panel.endDraw();

        // Das fertige Panel auf den Hauptbildschirm bringen
        image(panel, 0, 0);
    }

    void drawStartPage() {
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

// Login-Button
        fill(0);
        rect(400, 500, 200, 60);
        fill(255);
        textAlign(CENTER);
        textSize(30);
        text("LOGIN", 500, 540);

    }

    public void chooseStroke() {
        login.stroke(255,0, 0 );
    }

    void drawSnake(){
        schlange.beginDraw();
        schlange.clear();
        schlange.noStroke();
        schlange.circle(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+20, 40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+20, 36);
        schlange.rect(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+20+(20*(steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getX()-steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX())),40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+2, 20*(steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()-steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getX()), 36);
        schlange.rect(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+2, 40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+20+(20*(steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getY()-steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY())), 36, 20*(steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()-steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getY()));
        for (int i = 1; i < steuerung.getAktuellesSpiel().getZelleArrayList().size(); i++) {
            schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 2, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 2, 36, 36); //Quadrat 36x36 Körper
            schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 18 + (steuerung.getAktuellesSpiel().getZelleArrayList().get(i - 1).getX() - steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) * 20, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 2, 4, 36);
            schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 2, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 18 + (steuerung.getAktuellesSpiel().getZelleArrayList().get(i - 1).getY() - steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) * 20, 36, 4);
        }
        schlange.endDraw();
        image(schlange, 120,120);
    }

    public void drawApfel(){
        apfel.beginDraw();
        apfel.clear();
        apfel.noStroke();
        apfel.fill(213, 83, 47);
        apfel.circle(40*steuerung.getAktuellesSpiel().getApfel().getX()+20, 40*steuerung.getAktuellesSpiel().getApfel().getY()+20,38);
        apfel.endDraw();
        image(apfel, 120,120);
    }

    public void drawWinScreen(){
        endScreen.beginDraw();
        endScreen.fill(0, 180);
        endScreen.endDraw();
        image(endScreen, 0,0);
    }

    public void mousePressed() {
        if (mouseX > 400 &&
                mouseX < 600 &&
                mouseY > 250 &&
                mouseY < 310 &&
                state == 0) {
            println("Start gedrückt!");
            steuerung.ss.spiel_Start();
            steuerung.highscore.setScore(0);
            geschwindigkeitMs = 400;
            state = 1;
        }
        if (mouseX > width - 170 &&
                mouseX < width - 30 &&
                mouseY > 25 &&
                mouseY < 75 &&
                state == 2) {
            println("Zurück gedrückt!");
            state = 0;
        }
        if (mouseX > 400 &&
                mouseX < 600 &&
                mouseY > 500 &&
                mouseY < 560 &&
                state == 0) {
            println("Login gedrückt");
            state = 3;
        }
        if (state == 3) {
            if (register == true) {
                if (mouseX > 510 &&
                        mouseX < 730 &&
                        mouseY > 620 &&
                        mouseY < 690) {
                    nicknameActive = true;
                    focusedField = 11;

                    println("usernameRegister gedrückt!");
                }
                if (mouseX > 510 &&
                        mouseX < 730 &&
                        mouseY > 710 &&
                        mouseY < 780) {
                    passwordActive = true;
                    focusedField = 12;
                    println("passwordRegister gedrückt!");
                }
            } else if (register == false) {
                if (mouseX > 510 &&
                        mouseX < 730 &&
                        mouseY > 620 &&
                        mouseY < 690) {
                    nicknameActive = true;
                    focusedField = 1;

                    println("username gedrückt!");

                }
                if (mouseX > 510 &&
                        mouseX < 730 &&
                        mouseY > 710 &&
                        mouseY < 780) {
                    passwordActive = true;
                    focusedField = 2;
                    println("password gedrückt!");
                }
            }

            if(mouseX > 375 &&
                    mouseX < 625 &&
                    mouseY > 800 &&
                    mouseY < 820) {
                focusedField = 3;
                register = true;
                println("not registered gedrückt!");
            }
        }

        if (state == 4) {

            // Ragequit
            if (mouseX > 300 && mouseX < 700 &&
                    mouseY > 400 && mouseY < 470) {

                println("Ausloggen");

                nickname = "";
                password = "";

                state = 0;
            }

            // Neustart
            if (mouseX > 300 && mouseX < 700 &&
                    mouseY > 510 && mouseY < 580) {

                println("Neustart");

                steuerung.ss.spiel_Start();

                letzterSchrittZeit = millis();

                state = 1;
            }
        }

    }

    public void keyTyped() {
        if (focusedField == 1) {
            if (key == BACKSPACE) {
                if (nickname.length() > 0) {
                    nickname = nickname.substring(0, nickname.length() - 1);  // Entfernt das letzte Zeichen.
                }
            } else if (key == ENTER) {
                focusedField = 2;
            } else if (key != ' ') {    // löschen Space vor Eingabe
                nickname += key;
            }
        } else if (focusedField == 2) {
            if (key == BACKSPACE) {         // Delete Login-Pass
                if (password.length() > 0) {
                    password = password.substring(0, password.length() - 1);  // Entfernt das letzte Zeichen.
                }
            } else if (focusedField == 2 && key == ENTER) {
               MyJDBC.checkUser(nickname, password);

            } else {
                password += key;
            }
        }
        if (register == true && focusedField == 11) {   // Delete Register-Nick
            if (key == BACKSPACE) {
                if (nicknameRegister.length() > 0) {
                    nicknameRegister = nicknameRegister.substring(0, nicknameRegister.length() - 1);  // Entfernt das letzte Zeichen.
                }
            } else if (key == ENTER) {
                focusedField = 12;
            } else if (key != ' ') {     // löschen Space vor Eingabe
                nicknameRegister += key;
            }
        } else if (register == true && focusedField == 12) {    // Delete Register-Pass
            if (key == BACKSPACE) {
                if (passwordRegister.length() > 0) {
                    passwordRegister = passwordRegister.substring(0, passwordRegister.length() - 1);  // Entfernt das letzte Zeichen.
                }
            } else if (register == true && focusedField == 12 && key == ENTER) {

                if (MyJDBC.checkUser(nicknameRegister, passwordRegister) == false) {
                    MyJDBC.register(nicknameRegister, passwordRegister);            // MyJDBC-Register-Funktion
                    println("Registered:");
                    println("Nickname:" + nicknameRegister);
                    println("Password:" + passwordRegister);
                } else {
                    println("Please login");
                }
            } else {
                passwordRegister += key;
            }
        }

    }
    void drawGameOver() {

        gameOver.beginDraw();
        gameOver.background(40);

        gameOver.textAlign(CENTER, CENTER);

        // Titel
        gameOver.fill(255, 0, 0);
        gameOver.textSize(55);
        gameOver.text("Leider verkackt", 380, 80);

        // Highscore
        gameOver.fill(255);
        gameOver.textSize(35);
        gameOver.text("Highscore: 6767", 380, 170);

        // Ragequit-Button
        gameOver.fill(180, 40, 40);
        gameOver.rect(180, 280, 400, 70, 15);

        gameOver.fill(255);
        gameOver.textSize(30);
        gameOver.text("Ragequit", 380, 315);

        // Neustart-Button
        gameOver.fill(40, 180, 40);
        gameOver.rect(180, 390, 400, 70, 15);

        gameOver.fill(255);
        gameOver.text("Neustart", 380, 425);

        gameOver.endDraw();

        image(gameOver, 120, 120);
    }

    public static void main(String[] args) {
        PApplet.main("GUI"); // Launch sketch
    }

}