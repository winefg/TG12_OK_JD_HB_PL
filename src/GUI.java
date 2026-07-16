import db.MyJDBC;
import processing.core.PApplet;
import processing.core.PGraphics;
import static db.MyJDBC.*;



public class GUI extends PApplet {
    Steuerung steuerung;
    Highscore highscore;
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

    // Login/Register var'
    int focusedField = 0;

    String nickname = "";
    boolean nicknameActive = false;
    String nicknameRegister = "";

    String password = "";
    boolean passwordActive = false;
    String passwordRegister = "";

    boolean register = false;
    boolean loginSucces = false;

    boolean registerMode = true;
    boolean pleaseText = false;

    // vars damit Cursor blinkt
    boolean showCursor = true;
    int lastBlink = 0;
    int cursorNickname = 0;
    int cursorPassword = 0;
    int cursorNicknameRegister = 0;
    int cursorPasswordRegister = 0;


    public void settings() {
        size(1000, 1000);
        pixelDensity(1);
    }


    public void setup(){
        steuerung = new Steuerung(this);
        spielFeld = createGraphics(760, 760); //Größe in SETUP zuweisen
        panel = createGraphics(1000, 1000);
        login = createGraphics(500, 350);
        schlange = createGraphics(760, 760);
        apfel = createGraphics(760, 760);
        gameOver = createGraphics(760, 760);
        endScreen = createGraphics(1000, 1000);
    }


    @Override
    public void keyPressed() {
        // SHIFT + G - Spiel ohne Login starten (für Admin)
        if (keyEvent.isShiftDown() && (key == 'g' || key == 'G')) {
            loginSucces = true;
            nickname = "ADMIN";
            steuerung.startAsAdmin();
            steuerung.getAktuellesSpiel().spiel_Start();
            state = 1;
        }
        // 'keyCode' ist eine eingebaute globale Variable in Processing
        if (steuerung != null) {
            steuerung.checkInput(keyCode);
        }
        // Cursor mit Pfeilen bewegen
        if (focusedField == 1) {
            if (keyCode == LEFT && cursorNickname > 0) {
                cursorNickname--;
            }
            if (keyCode == RIGHT && cursorNickname < nickname.length()) {
                cursorNickname++;
            }
        }
        if (focusedField == 2) {
            if (keyCode == LEFT && cursorPassword > 0) {
                cursorPassword--;
            }
            if (keyCode == RIGHT && cursorPassword < password.length()) {
                cursorPassword++;
            }
        }
        if (focusedField == 11) {
            if (keyCode == LEFT && cursorNicknameRegister > 0) {
                cursorNicknameRegister--;
            }
            if (keyCode == RIGHT && cursorNicknameRegister < nicknameRegister.length()) {
                cursorNicknameRegister++;
            }
        }
        if (focusedField == 12) {
            if (keyCode == LEFT && cursorPasswordRegister > 0) {
                cursorPasswordRegister--;
            }
            if (keyCode == RIGHT && cursorPasswordRegister < passwordRegister.length()) {
                cursorPasswordRegister++;
            }
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
            isLoginSuccess();
        } else if (state==3) {          //Login-Bildschirm
            drawPanelLogin();
            image(login, 250, 600);
        }
        else if (state == 4) {          //Game-Over-Bildschirm
            drawGameOver();
        }
        else if (state == 5) {
            drawGameOver();
        }
    }


    void drawSpielFeld() {
        spielFeld.beginDraw(); //bevor man beginnt in den Layer zu zeichnen
        spielFeld.background(70, 84, 40); //immer die normalen Funktionen von Processing verwenden + Name der Variable davor

        int cellSize = 40;
        int gridSize = 19; //Muss mit Spielfeld übereinstimmen

        int fieldSize = gridSize * cellSize;

// Stroke um Feld
        spielFeld.stroke(0);
        spielFeld.strokeWeight(2);
        spielFeld.fill(179, 214, 101);
        spielFeld.rect(0, 0, fieldSize, fieldSize);

// Kästchen
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


    void drawCursorNickname(String option, int cursorPosition) {
        if (!showCursor) return;
        float cursorX = 270 + login.textWidth(option.substring(0, cursorPosition));
        login.stroke(255);
        login.strokeWeight(3);
        login.line(cursorX, 50, cursorX, 70);
        login.noStroke();
    }


    void drawCursorPassword(String option, int cursorPosition) {
        if (!showCursor) return;
        float cursorX = 270 + login.textWidth(option.substring(0, cursorPosition));
        login.stroke(255);
        login.strokeWeight(3);
        login.line(cursorX, 130, cursorX, 150);
        login.strokeWeight(1);  // zurücksetzen
        login.noStroke();
    }


    void drawPanelLogin() {
        login.beginDraw();
        login.background(200);

        //if (focusedField == 1 || focusedField == 11) {}
        if (millis() - lastBlink >= 500) {
            showCursor = !showCursor;
            lastBlink = millis();
        }
        // Nickname Text
        login.textSize(30);
        login.text("Nickname: ", 40, 70);
        // Nickname Field
        login.fill( 0);
        login.rect(260, 30, 220, 60);
        login.fill(255);

        if (register == true) {
            login.text(nicknameRegister, 270, 70);
            if (focusedField == 11){
                drawCursorNickname(nicknameRegister, cursorNicknameRegister);
            }
        } else {
            login.text(nickname, 270, 70);
            if (focusedField == 1) {
                // Cursor bei nickname
            drawCursorNickname(nickname, cursorNickname);
            }
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
            if (focusedField == 12) {
                drawCursorPassword(passwordRegister, cursorPasswordRegister);
            }

        } else {
            login.text(password, 270, 150);
            if (focusedField == 2) {
                    // Cursor bei password
                drawCursorPassword(password, cursorPassword);
            }
        }


        // Submit Button
        login.fill(100);
        login.rect(125,270, 250, 60);
        login.fill(255);
        login.textSize(20);
        login.text("Press ENTER to submit", 152, 307);

        // not/already registered Button
        login.fill(100);
        login.rect(125, 200, 250, 20);
        login.fill(255);
        login.textSize(15);
        if (registerMode) {
            // Not registered
            login.text("Not registered yet?", 190, 215);
            login.endDraw();
        } else {
            // Login if already registered
            login.text("Already registered", 190, 215);
            login.endDraw();
        }
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
        if (loginSucces) {
            anzeigenStartButton(0, 204, 0);
        } else{
            anzeigenStartButton(128, 128, 128);
        }

// "Please login"-Text anzeigen, falls sofort Start gedrückt
        if (pleaseText){
            pleaseLogin();
        }

// Login-Button
        fill(0);
        rect(400, 500, 200, 60);
        fill(255);
        textAlign(CENTER);
        textSize(30);
        text("LOGIN", 500, 540);

// Nickname anzeigen
        isLoginSuccess();
    }

    public void isLoginSuccess() {
        if (loginSucces){
            fill(0);
            textSize(30);
            text("Nickname: " + nickname, 500, 980);
        }
    }


    public void anzeigenStartButton(int r, int g, int b) {
        fill(r, g ,b);
        rect(400, 250, 200, 60);
        fill(255);
        textAlign(CENTER);
        textSize(30);
        text("START", 500, 290);
    }


    public void pleaseLogin() {
        fill(255, 0, 0);
        textAlign(CENTER);
        textSize(30);
        text("Please login", 500, 350);
    }


    public void chooseStroke() {
        login.stroke(255,0, 0 );
    }


    void drawSnake(){
        schlange.beginDraw();
        schlange.clear();
        schlange.noStroke();
        schlange.fill(79, 113, 223);
        for (int i = 0; i < steuerung.getAktuellesSpiel().getZelleArrayList().size(); i++) {
            if (i >= 1){
                schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 2, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 2, 36, 36); //Quadrat 36x36 Körper
                schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 18 + (steuerung.getAktuellesSpiel().getZelleArrayList().get(i - 1).getX() - steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) * 20, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 2, 4, 36);
                schlange.rect((40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getX()) + 2, (40 * steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) + 18 + (steuerung.getAktuellesSpiel().getZelleArrayList().get(i - 1).getY() - steuerung.getAktuellesSpiel().getZelleArrayList().get(i).getY()) * 20, 36, 4);
            } else {
                schlange.circle(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+20, 40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+20, 36);
                schlange.rect(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+20+(20*(steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getX()-steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX())),40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+2, 20*(steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()-steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getX()), 36);
                schlange.rect(40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getX()+2, 40*steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()+20+(20*(steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getY()-steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY())), 36, 20*(steuerung.getAktuellesSpiel().getZelleArrayList().getFirst().getY()-steuerung.getAktuellesSpiel().getZelleArrayList().get(1).getY()));
            }
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
        endScreen.background(40);
        endScreen.fill(0, 1);
        endScreen.rect(0,0,1000, 1000);
        endScreen.textAlign(CENTER, CENTER);
        endScreen.fill(255);
        endScreen.textSize(55);
        endScreen.text("GEWONNEN", 500,100);

        endScreen.textSize(35);
        endScreen.text("Highscore: ", 500, 290); //Oleksandr muss hier mit Datenbank carrien

        endScreen.fill(180, 40, 40);
        endScreen.rect(300, 400, 400, 70, 15);
        endScreen.fill(255);
        endScreen.textSize(30);
        endScreen.text("Ausloggen", 500, 435);

        endScreen.fill(40, 180, 40);
        endScreen.rect(300, 510, 400, 70, 15);
        endScreen.fill(255);
        endScreen.text("Neustart", 500, 545); //Konflikt in der Detektion mit Login vom Startpanel

        endScreen.endDraw();
        image(endScreen, 0,0);
    }


    public void mousePressed() {
        if (mouseX > 400 &&
            mouseX < 600 &&
            mouseY > 250 &&
            mouseY < 310 &&
            state == 0) {
            if (loginSucces) {
                println("Start gedrückt!");
                steuerung.getAktuellesSpiel().spiel_Start();
                steuerung.highscore.setScore(0);
                geschwindigkeitMs = 400;
                state = 1;
            } else {
                pleaseText = !pleaseText;
            }
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
            } if (registerMode) {
                if(mouseX > 375 &&
                    mouseX < 625 &&
                    mouseY > 800 &&
                    mouseY < 820) {
                    focusedField = 3;
                    register = true;
                    registerMode = !registerMode;
                    println("not registered gedrückt!");
                }
            }else {
                if(mouseX > 375 &&
                    mouseX < 625 &&
                    mouseY > 800 &&
                    mouseY < 820) {
                    focusedField = 3;
                    register = false;
                    registerMode = true;
                    println("already have account gedrückt!");
                }
            }
        }

        if (state == 4) {

            // Ragequit
            if (mouseX > 300 && mouseX < 700 &&
                    mouseY > 400 && mouseY < 470) {

                println("Ausloggen");
                state = 0;
            }

            // Neustart
            if (mouseX > 300 && mouseX < 700 && mouseY > 510 && mouseY < 580) {
                println("Neustart");
                steuerung.getAktuellesSpiel().spiel_Start();
                steuerung.highscore.setScore(0);
                letzterSchrittZeit = millis();

                state = 1;
            }
        }
    }


    public void keyTyped() {
        if (focusedField == 1) {
            if (key == BACKSPACE) {
                if (cursorNickname > 0) {   // // Position von Cursor speichern (aber -1)
                    nickname = nickname.substring(0, cursorNickname - 1)
                            + nickname.substring(cursorNickname);
                    cursorNickname--;
                }
            } else if (key == ENTER) {
                focusedField = 2;
            } else if (key != ' ') {    // löschen Space vor Eingabe
                nickname = nickname.substring(0, cursorNickname)
                        + key
                        + nickname.substring(cursorNickname); // Position von Cursor speichern
                cursorNickname++;
            }
        } else if (focusedField == 2) {
            if (key == BACKSPACE) {         // Delete Login-Pass
                if (cursorPassword > 0) {
                    password = password.substring(0, cursorPassword - 1)    // Entfernt das letzte Zeichen.
                            + password
                            .substring(cursorPassword);
                    cursorPassword--;
                }
            } else if (key == ENTER) {
               int id = MyJDBC.login(nickname, password);        // Check ob alles richtig
                if (id != -1){
                    steuerung.setAktSpielerID(id);
                    steuerung.addSpiel(steuerung.getAktuellesSpiel());
                    state = 0;                                  // zum Start
                    loginSucces = !loginSucces;
                    pleaseText = false;
                } else {
                    println("Wrong login");
                }
            } else {
                password = password.substring(0, cursorPassword)
                        + key
                        + password.substring(cursorPassword);
                cursorPassword++;
            }
        }
        if (register && focusedField == 11) {   // Delete Register-Nick
            if (key == BACKSPACE) {
                if (cursorNicknameRegister > 0) {
                    nicknameRegister = nicknameRegister.substring(0, cursorNicknameRegister - 1)
                                    + nicknameRegister.substring(cursorNicknameRegister);  // Entfernt das letzte Zeichen.
                    cursorNicknameRegister--;
                }
            } else if (key == ENTER) {
                focusedField = 12;
            } else if (key != ' ') {     // löschen Space vor Eingabe
                nicknameRegister = nicknameRegister.substring(0, cursorNicknameRegister)
                        + key
                        + nicknameRegister.substring(cursorNicknameRegister);
                cursorNicknameRegister++;
            }
        } else if (register && focusedField == 12) {    // Delete Register-Pass
            if (key == BACKSPACE) {
                if (cursorPasswordRegister > 0) {
                    passwordRegister = passwordRegister.substring(0, cursorPasswordRegister - 1)  // Entfernt das letzte Zeichen.
                                    + passwordRegister.substring(cursorPasswordRegister);
                    cursorPasswordRegister--;
                }
            } else if (key == ENTER) {
                int id = MyJDBC.register(nicknameRegister, passwordRegister);   // MyJDBC-Register-Funktion
                if (id != -1) {
                    steuerung.setAktSpielerID(id);            // id setzen
                    steuerung.addSpiel(steuerung.getAktuellesSpiel());
                    println("Registered:");
                    println("Nickname:" + nicknameRegister);
                    println("Password:" + passwordRegister);
                } else {
                    println("User already exists");
                    println("Please login");
                }
            } else {
                passwordRegister = passwordRegister.substring(0, cursorPasswordRegister)
                        + key
                        + passwordRegister.substring(cursorPasswordRegister);
                cursorPasswordRegister++;
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
        if (state == 4) {
            gameOver.fill(255, 0, 0);
            gameOver.text("Leider verkackt", 380, 80);
        }else if (state == 5) {
            gameOver.fill(255, 255, 255);
            gameOver.text("Gewonnen", 380, 80);
        }

        // Highscore
        gameOver.fill(255);
        gameOver.textSize(35);
        gameOver.text("Highscore: " + steuerung.getHighscore(), 380, 170);
        // Score
        gameOver.text("Score: " + steuerung.getScore(), 380, 220);
        // Quit
        gameOver.fill(180, 40, 40);
        gameOver.rect(180, 280, 400, 70, 15);

        gameOver.fill(255);
        gameOver.textSize(30);
        gameOver.text("Ragequit", 380, 315);

        // Neustarten
        gameOver.fill(40, 180, 40);
        gameOver.rect(180, 390, 400, 70, 15);

        gameOver.fill(255);
        gameOver.text("Neustart", 380, 425);

        gameOver.endDraw();

        image(gameOver, 120, 120);
    }


    public static void main(String[] args) {
        PApplet.main("GUI");
    }
}

