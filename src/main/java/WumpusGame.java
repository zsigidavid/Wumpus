import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

public class WumpusGame {
    private int steps;
    private static int startingPlayerRow;
    private static int startingPlayerCol;
    private char[][] board;
    private Random random = new Random();
    private int size;
    private static int playerRow;
    private static int playerCol;
    private char playerDirection;
    private int arrows;
    private boolean hasGold;
    private int gold;

    public WumpusGame(int size, int playerRow, int playerCol, char playerDirection, String playerName) {
        this.size = size;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.playerDirection = playerDirection;
        this.board = new char[size][size];
        this.hasGold = false;
        this.steps = 0;
        this.gold = 0;
        if (size <= 8) {
            arrows = 1;
        } else if (size >= 9 && size <= 14) {
            arrows = 3;
        } else if (size >= 15 && size <= 20) {
            arrows = 5;
        }
    }

    public char getPlayerDirection() {
        return playerDirection;
    }

    public void initializeRandomElements(int playerRow, int playerCol) {
        // Pálya méretének ellenőrzése és vermek számának meghatározása
        int pitCount;
        if (size <= 8) {
            pitCount = 1;
        } else if (size >= 9 && size <= 14) {
            pitCount = 3;
        } else {
            pitCount = 5;
        }

        int goldCount;
        if (size <= 8) {
            goldCount = 1;
        } else if (size >= 9 && size <= 14) {
            goldCount = 2;
        } else {
            goldCount = 3;
        }

        int wumpusCount;
        if (size <= 8) {
            wumpusCount = 1;
        } else if (size >= 9 && size <= 14) {
            wumpusCount = 3;
        } else {
            wumpusCount = 5;
        }

        // Pálya inicializálása üres mezőkkel
        initializeBoard();

        // Wumpusok generálása
        for (int i = 0; i < wumpusCount; i++) {
            int randomRow, randomCol;
            do {
                randomRow = random.nextInt(size);
                randomCol = random.nextInt(size);
            } while (board[randomRow][randomCol] != '_'); // Addig választ véletlen mezőt, amíg üres mezőt talál

            // Wumpus elhelyezése az adott mezőn
            board[randomRow][randomCol] = 'U';
        }

        // Aranyak generálása
        for (int i = 0; i < goldCount; i++) {
            int randomRow, randomCol;
            do {
                randomRow = random.nextInt(size);
                randomCol = random.nextInt(size);
            } while (board[randomRow][randomCol] != '_'); // Addig választ véletlen mezőt, amíg üres mezőt talál

            // Arany elhelyezése az adott mezőn
            board[randomRow][randomCol] = 'G';
        }

        // Veremek generálása
        for (int i = 0; i < pitCount; i++) {
            int randomRow, randomCol;
            do {
                randomRow = random.nextInt(size);
                randomCol = random.nextInt(size);
            } while (board[randomRow][randomCol] != '_'); // Addig választ véletlen mezőt, amíg üres mezőt talál

            // Verem elhelyezése az adott mezőn
            board[randomRow][randomCol] = 'P';
        }
        // Falak elhelyezése a pálya szélén
        for (int i = 0; i < size; i++) {
            initializeBoardElement(i, 0, '@'); // Pálya bal széle
            initializeBoardElement(i, size - 1, '@'); // Pálya jobb széle
            initializeBoardElement(0, i, '@'); // Pálya felső széle
            initializeBoardElement(size - 1, i, '@'); // Pálya alsó széle
        }

        // Hős elhelyezése az adott kezdő pozícióra
        this.playerRow = playerRow;
        this.playerCol = playerCol;
    }

    private void initializeBoard() {
        board = new char[size][size];

        // Pálya feltöltése üres mezőkkel
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '_';
            }
        }

        // Pálya széleinek körbefalazása
        for (int i = 0; i < size; i++) {
            board[0][i] = '@';          // Felső sor
            board[size - 1][i] = '@';   // Alsó sor
            board[i][0] = '@';          // Bal oldal
            board[i][size - 1] = '@';   // Jobb oldal
        }
        board[playerRow][playerCol] = 'H';
        printBoardState();
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print((board[i][j] == 0 ? '_' : board[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private void initializeBoardElement(int row, int col, char symbol) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            board[row][col] = symbol;
        }
    }

    public void printBoardState() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == playerRow && j == playerCol) {
                    // Játékos pozíciója
                    System.out.print("H ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void printPlayerPosition() {
        System.out.println("A játékos új pozíciója: " + playerRow + ", " + playerCol);
    }

    public void move() {
        int nextRow = playerRow;
        int nextCol = playerCol;

        // Elmentjük a jelenlegi mező pozícióját
        int prevRow = playerRow;
        int prevCol = playerCol;

        // Meghatározzuk a következő mezőt a játékos mozgása után
        switch (playerDirection) {
            case 'N':
                nextRow--;
                break;
            case 'S':
                nextRow++;
                break;
            case 'W':
                nextCol--;
                break;
            case 'E':
                nextCol++;
                break;
        }

        // Ellenőrizzük, hogy a következő mező a pályán belül van-e
        if (isValidPosition(nextRow, nextCol)) {
            // Ellenőrizzük, hogy a célmező üres-e (nincs fal, Wumpus, Pit vagy arany)
            if (board[nextRow][nextCol] == '_') {
                // Mozgás végrehajtása
                playerRow = nextRow;
                playerCol = nextCol;
                board[prevRow][prevCol] = '_'; // A kezdő pozíció üres lesz

                System.out.println("A hős sikeresen lépett.");

                // Pálya és pozíciók megjelenítése
                printPlayerInfo();
                printBoardState();
                printPlayerPosition();
            } else if (board[nextRow][nextCol] == 'G') {
                // A célmező aranyat tartalmaz
                playerRow = nextRow;
                playerCol = nextCol;

                System.out.println("A hős arany mezőre lépett. Vedd fel az aranyat.");

                // Pálya és pozíciók megjelenítése
                printPlayerInfo();
                printBoardState();
                printPlayerPosition();
            } else if (board[nextRow][nextCol] == 'U') {
                // A célmező wumpus (U karakter)
                System.out.println("Wumpusra léptél, sajnos vesztettél!");
                exit(0);
            } else if (board[nextRow][nextCol] == 'P') {
                // A célmező verem (P karakter)
                playerRow = nextRow;
                playerCol = nextCol;
                {
                    if (arrows > 0)
                    arrows--;
                }

                System.out.println("A hős veremre lépett, vesztett egy nyílat.");

                // Pálya és pozíciók megjelenítése
                printPlayerInfo();
                printBoardState();
                printPlayerPosition();
            } else {
                // A célmező nem üres (fal)
                System.out.println("A célmező nem üres, a hős nem tud oda lépni.");
            }
        } else {
            // A következő mező a pályán kívül van
            System.out.println("A hős nem tud a pályán kívülre lépni.");
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public void turnRight() {
        switch (playerDirection) {
            case 'N':
                playerDirection = 'E';
                break;
            case 'E':
                playerDirection = 'S';
                break;
            case 'S':
                playerDirection = 'W';
                break;
            case 'W':
                playerDirection = 'N';
                break;
        }
        printPlayerInfo();
        printBoardState();
        printPlayerPosition();
        System.out.println("A hős jobbra fordult.");
    }

    public void turnLeft() {
        switch (playerDirection) {
            case 'N':
                playerDirection = 'W';
                break;
            case 'W':
                playerDirection = 'S';
                break;
            case 'S':
                playerDirection = 'E';
                break;
            case 'E':
                playerDirection = 'N';
                break;
        }
        printPlayerInfo();
        printBoardState();
        printPlayerPosition();
        System.out.println("A hős balra fordult.");
    }

    public boolean shoot() {
        System.out.println("A játékos lövésre készül...");

        // Nyíl kezdő pozíciója
        int arrowRow = playerRow;
        int arrowCol = playerCol;

        boolean hitWumpus = false; // Eldöntjük, hogy a nyíl talált-e Wumpust

        while (true) {
            // Nyíl mozgatása az adott irányba
            switch (playerDirection) {
                case 'N':
                    arrowRow--;
                    break;
                case 'S':
                    arrowRow++;
                    break;
                case 'W':
                    arrowCol--;
                    break;
                case 'E':
                    arrowCol++;
                    break;
            }

            // Ellenőrzés, hogy a nyíl talált-e valamire (Wumpus vagy fal)
            if (board[arrowRow][arrowCol] == 'U') { // Wumpus találat
                System.out.println("A nyíl eltalálta a Wumpust! A Wumpus meghalt.");
                board[arrowRow][arrowCol] = '_'; // Wumpus helyére üres mező kerül
                hitWumpus = true; // A nyíl eltalált egy Wumpust
                break;
            } else if (board[arrowRow][arrowCol] == '@') { // Fal találat
                System.out.println("A nyíl falba ütközött.");
                break;
            }
        }

        // Csökkentjük a nyílak számát, ha még van nyíl
        if (arrows > 0) {
            arrows--; // Csökkentjük a nyílak számát
        }

        // Ha a nyílak száma 0, akkor kiírjuk az üzenetet
        if (arrows <= 0) {
            System.out.println("Sajnos elfogytak a nyílak, így nem tudsz lőni.");
        } else {
            System.out.println("A nyílak száma: " + arrows);
        }

        printPlayerInfo();
        printBoardState();
        return hitWumpus;
    }

    public void pickupGold() {
        if (board[playerRow][playerCol] == 'G') {
            // Ha az aktuális mezőn arany található
            gold++;
            board[playerRow][playerCol] = '_'; // Az arany felvétele után az adott mező üres lesz
            System.out.println("Arany felvéve.");
        } else {
            System.out.println("Az aktuális mezőn nincs arany.");
        }
        printPlayerInfo();
        printBoardState();
    }
    public boolean isGameOver() {
        // Implementáld a játék vége ellenőrzését
        return false;
    }

    private void printPlayerInfo() {
        System.out.println("Hős aranya: " + gold);
        System.out.println("Hős nyílai: " + arrows);
    }

    public boolean isGameCompleted() {
        if (hasPickedUpAllGold()) {
            return true;
        }
        return false;
    }

    public boolean hasPickedUpAllGold() {
        // Ellenőrizzük, hogy minden aranyat felvettünk-e
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == 'G') {
                    return false; // Még van felvehető arany a pályán
                }
            }
        }
        return true; // Minden aranyat felvettünk
    }

    public static boolean isBackToStartingPosition() {
        return playerRow == startingPlayerRow && playerCol == startingPlayerCol;
    }

    public int getGoldCount() {
        return gold;
    }

}
