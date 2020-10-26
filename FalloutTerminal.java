import java.util.ArrayList;

/**
 * @author iankenny
 */

import java.util.Random;
import java.util.Scanner;

public class FalloutTerminal {

    /**
     * This method creates a single line of the terminal display.
     * @param startAddress The first address of the line.
     * @param wordProbability If less than or equal to 0.6 the line should contain one word.
     * @return The line for display.
     */
    public String buildDisplayLine(int startAddress, float wordProbability)
    {
        StringBuilder line = new StringBuilder(); //build line
        int midAddress = startAddress + 240; //generate the second printable address

        //generate array of garbage characters
        char[] garbageChars = {'!', '(', ')', '{', '}', '<', '>', '[', ']', '/', '|', '$', '@',
                ',', '\'', ';', ':', '?', '*', '^', '=', '.', '-', '+', '&', '_', '%', '#', '\\'};

        line.append("0x").append(Integer.toHexString(startAddress).toUpperCase()).append(" "); //output the first address

        //traverse half the line
        for (int i = 0; i < 30; i++)
        {
            int rnd = new Random().nextInt(garbageChars.length); //get a random index from the garbageChars array
            line.append(garbageChars[rnd]); //add the char to the line
        }

        //add the middle address to the line
        line.append(" 0x").append(Integer.toHexString(midAddress).toUpperCase()).append(" ");

        //traverse the second half of the line
        for (int i = 0; i < 30; i++)
        {
            int rnd = new Random().nextInt(garbageChars.length); //random index
            line.append(garbageChars[rnd]); //add char
        }

        //word appears in the line
        if (wordProbability <= 0.6)
        {
            String word = getRandomWord(); //generate the random word
            addSelectedWord(word); //add the word to the system

            int rndLine1 = 8 + (int)(Math.random() * ((28 - 8) + 1)); //random number generated between line index 8 and 28
            int rndLine2 = 46 + (int)(Math.random() * ((66 - 46) + 1)); //random number generated between line index 46 and 66
            int whereGoes = 1 + (int) (Math.random() * 2); //coin toss

            if (whereGoes == 1) line.replace(rndLine1, rndLine1 + 8, word.toUpperCase()); //word in first half
            else line.replace(rndLine2, rndLine2+8, word.toUpperCase()); //word in second half
        }
        return line.toString();
    }

    /*
     ***************************************************************************************
     */

    private static final int START_ADDRESS = 0x9380;
    private static final int DISPLAY_HEIGHT = 20;
    private static final int NUM_CHARACTERS_PER_ROW = 60;
    private static final float WORD_PROB_1 = 0.6f;
    private static final int WORD_LENGTH = 8;
    private static final int NUM_GUESSES_ALLOWED = 4;
    private static final int BYTE_SIZE = 8;
    private static Random rand;
    private static final String[] words = {
            "flourish",
            "appendix",
            "separate",
            "unlawful",
            "platform",
            "shoulder",
            "marriage",
            "attitude",
            "reliable",
            "contempt",
            "prestige",
            "evaluate",
            "division",
            "birthday",
            "orthodox",
            "appetite",
            "perceive",
            "pleasant",
            "surprise",
            "elephant",
            "incident",
            "medieval",
            "absolute",
            "dominate",
            "designer",
            "misplace",
            "possible",
            "graduate",
            "solution",
            "governor"
    };

    private static String password;

    private static ArrayList<String> selectedWords = new ArrayList<String>();

    private void run() {

        System.out.println(buildDisplay());

        Scanner in = new Scanner(System.in);

        int guessCount = NUM_GUESSES_ALLOWED;

        boolean done = false;

        do {
            System.out.println("> Password required.");
            System.out.println("> Attempts remaining = " + guessCount);
            System.out.println(">");
            String guess = in.nextLine();

            if (guess.equalsIgnoreCase(getPassword())) {
                System.out.println("> Access granted.");
                done = true;
            }

            else {
                --guessCount;
                System.out.println("> Access denied.");
                System.out.println("> Likeness = " + getCorrectCount(guess, password));
            }
        } while (guessCount > 0 && !done);

        if (guessCount == 0) {
            System.out.println("> Initiating lockout");
        }

        in.close();
    }

    private int getCorrectCount(String guess, String password) {

        int count = 0;

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == password.charAt(i)) {
                count++;
            }
        }

        return count;
    }

    private String getRandomWord() { return words[rand.nextInt(words.length)]; }

    private String buildDisplay() {

        String ret = "";

        int address = START_ADDRESS;

        for (int i = 0; i < DISPLAY_HEIGHT; i++) {

            float rf = rand.nextFloat();

            String line = buildDisplayLine(address, rf);

            ret += line + "\n";

            address += BYTE_SIZE * NUM_CHARACTERS_PER_ROW;
        }

        setRandomPassword();

        return ret;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    private void addSelectedWord(String word) {
        selectedWords.add(word);
    }

    public void setRandomPassword() {
        password = selectedWords.get(rand.nextInt(selectedWords.size())).toLowerCase();
    }

    public FalloutTerminal() {
        rand = new Random(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        new FalloutTerminal().run();
    }
}
