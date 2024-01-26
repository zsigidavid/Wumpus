import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playerName = getPlayerName(scanner);
        WumpusGame wumpusGame = null;

        while (true) {
            System.out.println("\n----- Wumpus Játék -----");
            System.out.println("1. Új játék");
            System.out.println("2. Fájlból betöltés");
            System.out.println("3. Mentés");
            System.out.println("4. Ranglista");
            System.out.println("5. Kilépés");
            System.out.print("Válassz egy menüpontot: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consuming the newline character

            switch (choice) {
                case 1:
                    wumpusGame = startNewGame(scanner, playerName);
                    playGame(wumpusGame, scanner, playerName);
                    break;
                case 2:
                    wumpusGame = loadFromFile();
                    if (wumpusGame != null) {
                        playGame(wumpusGame, scanner, playerName);
                    } else {
                        System.out.println("Hiba a fájl betöltésekor.");
                    }
                    break;
                case 3:
                    if (wumpusGame != null) {
                        saveToFile(wumpusGame);
                        System.out.println("Játékállás mentve.");
                    } else {
                        System.out.println("Nincs aktív játék.");
                    }
                    break;
                case 4:
                    displayLeaderboard();
                    break;
                case 5:
                    System.out.println("Kilépés.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Érvénytelen választás.");
            }
        }
    }

    private static String getPlayerName(Scanner scanner) {
        System.out.print("Adja meg a játékos nevét: ");
        return scanner.nextLine();
    }



    private static WumpusGame startNewGame(Scanner scanner, String playerName) {
        int size;
        do {
            System.out.print("Adja meg a pálya méretét (NxN, 6-20): ");
            size = scanner.nextInt();
            if (size < 6 || size > 20) {
                System.out.println("A pálya mérete nem felel meg a kritériumnak (6-20)!");
            }
        } while (size < 6 || size > 20);
        System.out.print("Adja meg a hős kezdő oszlopát: ");
        int playerCol = scanner.nextInt();
        System.out.print("Adja meg a hős kezdő sorát: ");
        int playerRow = scanner.nextInt();
        System.out.print("Adja meg a hős kezdő irányát (N/W/S/E): ");
        char playerDirection = scanner.next().charAt(0);

        WumpusGame wumpusGame = new WumpusGame(size, playerRow, playerCol, playerDirection, playerName);
        wumpusGame.initializeRandomElements(playerRow, playerCol);

        System.out.println("Incializált pálya:");
        wumpusGame.printBoard();

        System.out.println("A játék elkezdődött!");
        System.out.println("A játékmenet során a lehetséges akciók: lép, fordulj jobbra, fordulj balra, lő, aranyat felszed, felad, halasztás");
        return wumpusGame;
    }

    private static void playGame(WumpusGame wumpusGame, Scanner scanner, String playerName) {
        int steps = 0;
        while (true) {
            System.out.println("\n----- Játék -----");
            System.out.println("1. Lép");
            System.out.println("2. Fordulj jobbra");
            System.out.println("3. Fordulj balra");
            System.out.println("4. Lő");
            System.out.println("5. Aranyat felszed");
            System.out.println("6. Felad");
            System.out.println("7. Halasztás");
            System.out.print("Válassz egy akciót: ");

            int action = scanner.nextInt();
            scanner.nextLine();  // Consuming the newline character

            steps++;

            switch (action) {
                case 1:
                    wumpusGame.move();
                    break;
                case 2:
                    wumpusGame.turnRight();
                    break;
                case 3:
                    wumpusGame.turnLeft();
                    break;
                case 4:
                    wumpusGame.shoot();
                    break;
                case 5:
                    wumpusGame.pickupGold();
                    break;
                case 6:
                    System.out.println("Játék feladva. Visszatérés az alapmenühöz.");
                    return;
                case 7:
                    saveGame(wumpusGame, scanner, playerName);
                    break;
                default:
                    System.out.println("Érvénytelen akció.");
            }

            if (wumpusGame.isGameCompleted()) {
                System.out.println("Gratulálok, sikerült felszedned az összes aranyat"  + steps +  "lépésből. Győztél!");
                updateLeaderboard(playerName, steps);
                return; // Visszalépés a főmenübe
            } else if (WumpusGame.isBackToStartingPosition() && !wumpusGame.hasPickedUpAllGold()) {
                System.out.println("Visszaértél a kezdőpozícióba, de még van felszedhető arany a pályán.");
            } else if (wumpusGame.isGameOver()) {
                System.out.println("Játék vége.");
                return;
            }
        }
    }

    private static void saveGame(WumpusGame wumpusGame, Scanner scanner, String playerName) {
        System.out.print("Adja meg a mentés nevét: ");
        String saveName = scanner.nextLine();
        // Itt implementáld a játékállás mentését a saveName fájlba,
        // társítva a felhasználónevet a mentéshez, valamint a játékban lévő állapottal.
        System.out.println("Játék mentve: " + saveName);
    }


    private static void saveToFile(WumpusGame wumpusGame) {
        // Implementáld a játékállás mentését a fájlba.
    }
    private static void updateLeaderboard(String playerName, int steps) {
        try {
            FileWriter writer = new FileWriter("ranglista.txt", true);
            writer.write(playerName + ": " + steps + " lépés\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Hiba történt a ranglista frissítése közben.");
            e.printStackTrace();
        }
    }
    private static WumpusGame loadFromFile() {
        try {
            File file = new File("wumpluszinput.txt");
            Scanner fileScanner = new Scanner(file);

            // Olvassuk be a pálya méretét és a játékos kezdő pozícióját
            int size = fileScanner.nextInt();
            int playerRow = fileScanner.nextInt();
            int playerCol = fileScanner.nextInt();
            char playerDirection = fileScanner.next().charAt(0);

            // Készítsünk új játékot a beolvasott adatokkal
            WumpusGame wumpusGame = new WumpusGame(size, playerRow, playerCol, playerDirection, "Player");
            wumpusGame.initializeRandomElements(playerRow, playerCol);

            return wumpusGame;
        } catch (FileNotFoundException e) {
            System.out.println("A fájl nem található.");
            return null;
        }

    }
    private static void displayLeaderboard() {
        try {
            File file = new File("ranglista.txt");
            Scanner scanner = new Scanner(file);

            List<String> leaderboard = new ArrayList<>();
            while (scanner.hasNextLine()) {
                leaderboard.add(scanner.nextLine());
            }

            // Rendezés lépésszám alapján növekvő sorrendben
            Collections.sort(leaderboard, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int steps1 = Integer.parseInt(o1.split(": ")[1].split(" lépés")[0]);
                    int steps2 = Integer.parseInt(o2.split(": ")[1].split(" lépés")[0]);
                    return steps1 - steps2;
                }
            });

            System.out.println("Ranglista:");
            for (String entry : leaderboard) {
                System.out.println(entry);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Hiba: ranglista.txt fájl nem található.");
        }


    }

}

