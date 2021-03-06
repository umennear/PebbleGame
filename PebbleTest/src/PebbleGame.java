import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * The type Pebble game.
 */
public class  PebbleGame {

    static volatile Bags blackBagX;
    static volatile Bags blackBagY;
    static volatile Bags blackBagZ;
    static volatile Bags whiteBagA;
    static volatile Bags whiteBagB;
    static volatile Bags whiteBagC;
    public static volatile boolean winner = false;

    /**
     *Empty constructor
     */
    public PebbleGame() {

    }

    /**
     * Getter for Flag to stop threads
     * @return boolean winner
     */
    public static boolean getIfWinner(){
        return winner;
    }

    /**
     * Setter for Flag to stop threads
     */
    public static void setWinnerTrue(){
        winner = true;
    }

    /**
     * Method that creates the random numbers for thr black bag
     * @param numberOfPlayers integer number of players
     * @param bag1 bag1 object
     * @param bag2 bag2 object
     * @param bag3 bag 3 object
     * @throws IOException throws an IOException when writing to file
     */
    public static void createBlackBags(int numberOfPlayers, Bags bag1, Bags bag2, Bags bag3) throws IOException { // method to give the black bags values at beginning of the game
        int numberOfPebbles = numberOfPlayers * 11; // as in spec
        for (int i = 0; i < numberOfPebbles; i++) { // gives each bag a pebble for numberOfPebble times with a random int value
            bag1.addPebble(randomNumGenerator(1, 25));
            bag1.updateFile(bag1.getBagPebbles());
            bag2.addPebble(randomNumGenerator(1, 25));
            bag2.updateFile(bag2.getBagPebbles());
            bag3.addPebble(randomNumGenerator(1, 25));
            bag3.updateFile(bag3.getBagPebbles());

        }
    }

    /**
     * Random number generator which generates integer numbers in a given range
     *
     * @param min lower bound
     * @param max upper bound
     * @return random number
     */
    public static int randomNumGenerator(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    

    /**
     * Check file input file.
     *
     * @param scan the scan
     * @return the file
     */
    public static File checkFileInput(Scanner scan, int counter) {
        boolean fileVarificationSuccessful = false;
        File blackBagFile = null;
        do {
            System.out.println("Please enter locations of bag number " + counter + " to load:");
            String blackBagName = scan.nextLine();
            if (blackBagName.equals("E")) {
                System.exit(0);
            }
            blackBagFile = new File(blackBagName);
            if (blackBagFile.exists() && !blackBagFile.isDirectory()) {
                fileVarificationSuccessful = true;
            } else {
                System.out.println(blackBagName + " does not exists. Please re-enter the location of the file.");
            }
        } while (fileVarificationSuccessful == false);
        return blackBagFile;
    }

    /**
     * Check int input int.
     *
     * @param scan the scan
     * @return the int
     */
    public static int checkIntInput(Scanner scan) {

        boolean validationSuccessful = false;
        int noOfPlayersInput = 0;

        System.out.print("Please enter the number of players: ");
        do {
            String numberOfPlayersString = scan.nextLine();
            // validate that the input is an integer
            if (numberOfPlayersString.equals("E")) {
                System.exit(0);
                continue;
            } else {
                try {
                    noOfPlayersInput = Integer.parseInt(numberOfPlayersString); //Converts String to Int
                    // validate that the input is positive
                    if (noOfPlayersInput < 0) {
                        System.out.print("Please enter a positive integer: ");
                        continue;
                    } else {
                        validationSuccessful = true;
                    }
                } catch (NumberFormatException e) { //If String is unable to be converted to an Int
                    System.out.println("Please enter an integer number of players:");
                }
            }

        } while (validationSuccessful == false);

        return noOfPlayersInput;
    }

    /**
     * Game main.
     */
    public static Player[] gameMain() throws IOException {
        // this goes through the actions of the game
        // starts by setting up the game
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the pebble game!! \nYou will be asked to enter the number of players.\nand then for the location of three files in turn containing comma seperated integer values the pebble weights.\nThe integer values must strictly positive. \nThe game will then be simulated, and output written to files in this directory.\n"); // opening remarks
        int noOfPlayers = checkIntInput(scan);
        File blackBagXFile = checkFileInput(scan, 0);
        File blackBagYFile = checkFileInput(scan, 1);
        File blackBagZFile = checkFileInput(scan, 2);
        //creates the bags files after they have been checked
        // add in try catch for createFile();
        File whiteBagAFile = new File("WhiteBagA.csv");
        File whiteBagBFile = new File("WhiteBagB.csv");
        File whiteBagCFile = new File("WhiteBagC.csv");
        //creates the bags themselves as objects with basic attributes
         blackBagX = new Bags("blackBagX", blackBagXFile); // initialising the bags to create the base objects
         blackBagY = new Bags("blackBagY", blackBagYFile);
         blackBagZ = new Bags("blackBagZ", blackBagZFile);
         whiteBagA = new Bags("whiteBagA", whiteBagAFile);
         whiteBagB = new Bags("whiteBagB", whiteBagBFile);
         whiteBagC = new Bags("whiteBagC", whiteBagCFile);
        // for the black bags, the bags are given the pebbles with the weights
        createBlackBags(noOfPlayers, blackBagX, blackBagY, blackBagZ);
        Player[] players = new Player[noOfPlayers];
        // Instantiates the players and stores in an array called players.
        for(int i=0 ; i< noOfPlayers ;i++){
            players[i] = new Player("player" + (i+1));
        }
        for(int i=0 ; i<noOfPlayers ;i++){
            players[i].start();
        }
        return players;
        //end of set up

    }

    /**
     * Player won.
     *
     * @param PlayerName the player name
     */
    public static void playerWon(String PlayerName) {
        System.out.println("Congratulations to " + PlayerName + ", you have won the game.\nThe game is now over, Goodbye!");
        System.exit(0);

    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {

        boolean equalsE =false;
        do{

            try {
                gameMain();
            } catch (IOException e) {

            }
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            if(input.equals("E")){
                equalsE = true;
            }

        }while (!equalsE);

    }

    /**
     * The type Player.
     */
    public static class Player extends Thread {


        public volatile String name;
        public volatile ArrayList<Integer> currentHand = new ArrayList<Integer>();
        public volatile String lastPickUp;
        public volatile File fileName;
        public volatile boolean discard;
        


        /**
         * Instantiates a new Player.
         *
         * @param playerName the player name
         */
        public Player(String playerName) {

            this.name = playerName;
            try {
                fileName = new File(playerName + "_output.txt");
                if (fileName.createNewFile()) {
                    return;
                } else {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName, false));
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Unable to remove previous contents of file.");
                    }
                }
            } catch (Throwable e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        /**
         * Method that sets up and then runs the threads until the Flag is true and stops them
         */
        public synchronized void run() {
            boolean setUp = false;
            do {

                try {
                    if(!setUp){
                        Bags bag = new Bags();
                        int newBag = randomNumGenerator(1, 3);
                        if (newBag == 1) {
                            bag = blackBagX;
                        } else if (newBag == 2) {
                            bag = blackBagY;
                        } else if (newBag == 3) {
                            bag = blackBagZ;
                        }
                        CopyOnWriteArrayList<Integer> pebbles = bag.getBagPebbles();
                        int pebblesize = pebbles.size();
                        if (pebblesize > 10) {
                            for (int i = 0; i < 10; i++) {
                                int pick = pebbles.get(1);
                                bag.removePebble(1);
                                currentHand.add(pick);
                            }
                        }

                        setUp = true;
                    }
                    turn();
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.out.println("Could not execute players turns.");
                }

                if (getHandSum() == 100 && getCurrentHand().size() == 10) {
                    playerWon(this.getPlayersName());
                    PebbleGame.setWinnerTrue();

                }
                Thread.yield();


            } while (!PebbleGame.getIfWinner());{

            }

            System.out.println("exited");

        }

        /**
         * Method for what each string has to do for their turn
         */
        public synchronized void turn() {
            try {
                if (currentHand.size() == 0){
                    pickUp();
                }else {
                    discard();
                }
                checkBags(blackBagX, blackBagY, blackBagZ);
                pickUp();
                checkBags(blackBagX, blackBagY, blackBagZ);

            } catch (IOException e) {
                System.out.println("Cannot write to file.");
            }
        }

        /**
         * Atomic method discard which picks a random pebble from current hand and discards it to the relevant white bag
         */
        synchronized public void discard() throws IOException {
            discard = true;
            int bound = currentHand.size();
            int pebbleNumber = ThreadLocalRandom.current().nextInt(0, bound-1);
            int pebbleWeight = currentHand.get(pebbleNumber);
            currentHand.remove(pebbleNumber);
            String whiteBagLetter = "";
            int bag;
            Bags whiteBag = new Bags();
            if (lastPickUp == null) {
                bag = randomNumGenerator(1, 3);
                if (bag == 1) {
                    whiteBag = whiteBagA;
                    whiteBagLetter = "A";
                } else if (bag == 2) {
                    whiteBag = whiteBagB;
                    whiteBagLetter = "B";
                } else if (bag == 3) {
                    whiteBag = whiteBagC;
                    whiteBagLetter = "C";
                }
            } else if (lastPickUp.equals("X")) {
                whiteBag = whiteBagA;
                whiteBagLetter = "A";
            } else if (lastPickUp.equals("Y")) {
                whiteBag = whiteBagB;
                whiteBagLetter = "B";
            } else if (lastPickUp.equals("Z")) {
                whiteBag = whiteBagC;
                whiteBagLetter = "C";
            }
            whiteBag.addPebble(pebbleWeight);
            whiteBag.updateFile(whiteBag.getBagPebbles());
            updateFile(discard, currentHand, pebbleWeight, whiteBagLetter);

        }

        /**
         * Checks if the bags are empty and uses the corresponding white bag to fill them
         * @param bag1 bag1 object
         * @param bag2 bag2 object
         * @param bag3 bag3 object
         * @throws IOException
         */
        synchronized public void checkBags(Bags bag1, Bags bag2, Bags bag3) throws IOException {
            //arguments must be in alphabetical order to be paired correctly
            if (bag1.isEmpty()) {
                //gets the arraylist of the corresponding white bag
                CopyOnWriteArrayList<Integer> pebblesA = whiteBagA.getBagPebbles();
                //adds it to the black bag
                for(int i = 0; i < pebblesA.size() ; i++){
                    blackBagX.addPebble(pebblesA.get(i));
                }

                blackBagX.updateFile(pebblesA);
                //removes those pebbles from the white bag
                whiteBagA.updateFileRemove();

            }
            if (bag2.isEmpty()) {
                //gets the arraylist of the corresponding white bag
                CopyOnWriteArrayList<Integer> pebblesB = whiteBagB.getBagPebbles();
                //adds it to the black bag
                for(int i = 0; i < pebblesB.size() ; i++){
                    blackBagY.addPebble(pebblesB.get(i));
                }
                blackBagY.updateFile(pebblesB);
                //removes those pebbles from the white bag
                whiteBagB.updateFileRemove();
            }
            if (bag3.isEmpty()) {
                //gets the arraylist of the corresponding white bag
                CopyOnWriteArrayList<Integer> pebblesC = whiteBagC.getBagPebbles();
                //adds it to the black bag
                for(int i = 0; i < pebblesC.size() ; i++){
                    blackBagZ.addPebble(pebblesC.get(i));
                }
                blackBagZ.updateFile(pebblesC);
                //removes those pebbles from the white bag
                whiteBagC.updateFileRemove();
            }

        }

        /**
         * Atomic method picks up pebble from random black bag and adds it to current hand then removes it from the relevant black bag
         * @throws IOException
         */
        synchronized public void pickUp() throws IOException {
            discard = false;
            Bags bag = new Bags();
            Random rand = new Random();
            int newBag = rand.nextInt(2);
            if (newBag == 0) {
                bag = blackBagX;
                this.lastPickUp = "X";

            } else if (newBag == 1) {
                bag = blackBagY;
                this.lastPickUp = "Y";
            } else if (newBag == 2) {
                bag = blackBagZ;
                this.lastPickUp = "Z";
            }
            CopyOnWriteArrayList<Integer> pebbles = bag.getBagPebbles();
            int pebblesize = pebbles.size();
            do {
                checkBags(blackBagX, blackBagY, blackBagZ);
                 newBag = rand.nextInt(2);
                if (newBag == 0) {
                    bag = blackBagX;
                    this.lastPickUp = "X";

                } else if (newBag == 1) {
                    bag = blackBagY;
                    this.lastPickUp = "Y";
                } else if (newBag == 2) {
                    bag = blackBagZ;
                    this.lastPickUp = "Z";
                }
            } while (pebblesize <= 0);
                int pick = bag.removeRandomPebble();
                currentHand.add(pick);

                //writes to players log
                updateFile(discard, currentHand, pick, lastPickUp);
                //Delete contents of bag file and replaces it with the new contents
                try{
                    bag.updateFile(bag.getBagPebbles());
                }catch(Throwable e){
                    System.out.println("Problem updating black bag file");
                }

        }

        /**
         * Getter for the players name
         * @return the players name
         */
        public String getPlayersName() {
            return this.name;
        }

        /**
         * Getter for the current hand
         * @return an arraylist of the players current hand
         */
        public ArrayList<Integer> getCurrentHand() {
            return this.currentHand;
        }

        /**
         * Get hand sum int.
         *
         * @return the int
         */
        public int getHandSum() {
            int HandSum = 0;
            for (int i = 0; i < this.currentHand.size(); i++) {
                HandSum = HandSum + currentHand.get(i);
            }
            return HandSum;
        }

        /**
         * Updates player log file
         * @param discard boolean whether a action is discard or pick up
         * @param list the current hand
         * @param data the pebble being acted upon
         * @param bag the Bag it was drawn from or add to
         */
        public void updateFile(boolean discard, ArrayList<Integer> list, int data, String bag) {
            try {
                String log = "";
                if (discard) {
                    log += this.getPlayersName() + " has discarded " + data + " pebble to bag " + bag;
                } else {
                    log += this.getPlayersName() + " has drawn a " + data + " pebble from bag " + bag ;
                }
                log += "\n" + this.getPlayersName() + " hand is " + list.toString().replaceAll("[\\[\\]]", "") ;
                BufferedWriter buffer = new BufferedWriter(new FileWriter(this.fileName, true));
                buffer.append(log);
                buffer.newLine();
                buffer.close();
            } catch (IOException e) {
                System.out.println("Cannot add to player output file");
            }
        }

    }


}


