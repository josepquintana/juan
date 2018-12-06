import java.io.*;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class BruteForce
{
    private static String hash;
    private static char iniChar;
    private static char lastChar;
    private static String type;
    private static String mode;
    private static int n;
    private static boolean e;
    private static boolean v;
    private static Timer printStatusTimer;
    private static boolean stopTimer = false;
    private static long counter;
    private static long n_numbers;

    public static void start(int chain, String pathToFile, String hashType, String configMode, boolean exclusive, boolean verbose, int timerInterval) throws IOException {
        ReadFromFile.open(Paths.get(pathToFile));
        hash = ReadFromFile.read();          // hash to brute force on
        ReadFromFile.close();
        if (hash == null) printInvalidInputAndExit();
        printHashToBruteForceOn(hash, hashType);
        type = hashType; mode = configMode; n = chain; e = exclusive; v = verbose;
        setWorkingMode();
        counter = 0; n_numbers = getHowManyNumbers();
        if (!v) iniTimer(timerInterval);
        try {
            bruteForce();   // brute force all possible combinations
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        stopTimer = true;
    }

    private static void bruteForce() throws NoSuchAlgorithmException {
        char[] chain;
        if (e) {
            // if the "-e" flag is activated -> only brute force numbers with 'n' digits
            chain = new char[n];
            if (type.equals("sha1"))   bruteForceSHA1  (n,0,chain);
            if (type.equals("sha256")) bruteForceSHA256(n,0,chain);
//            if (type.equals("sha512")) bruteForceSHA512(n,0,chain);
        }
        else {
            // each call to recursive method "bruteForce(int, int, char[])" brute forces
            // all the possible combinations of digits with length 'n'
            for (int i = 1; i <= n; i++) {
                chain = new char[i];
                if (type.equals("sha1"))   bruteForceSHA1  (i, 0, chain);
                if (type.equals("sha256")) bruteForceSHA256(i, 0, chain);
//                if (type.equals("sha512")) bruteForceSHA512(i, 0, chain);
            }
        }
    }

    private static void bruteForceSHA1(int limit, int k, char[] chain) throws NoSuchAlgorithmException {
        if (limit == k) {
            String curr_hashed = Hash.SHA1(new String(chain));
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) printCrackedAndExit(new String(chain));
            ++counter;
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceSHA1(limit, k + 1, chain);
        }
    }

    private static void bruteForceSHA256(int limit, int k, char[] chain) throws NoSuchAlgorithmException {
        if (limit == k) {
            String curr_hashed = Hash.SHA256(new String(chain));
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) printCrackedAndExit(new String(chain));
            ++counter;
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceSHA256(limit, k + 1, chain);
        }
    }

    private static boolean compareHash(String hash1, String hash2) {
        return (hash1.equals(hash2));
    }

    private static void setWorkingMode() {
        // set the first and last character to iterate into depending on the working mode
        if (mode.equalsIgnoreCase("all"))       { iniChar = '!'; lastChar = '~'; }
        if (mode.equalsIgnoreCase("numbers"))   { iniChar = '0'; lastChar = '9'; }
        if (mode.equalsIgnoreCase("letters"))   { iniChar = 'A'; lastChar = 'z'; }
        if (mode.equalsIgnoreCase("lowercase")) { iniChar = 'a'; lastChar = 'z'; }
        if (mode.equalsIgnoreCase("uppercase")) { iniChar = 'A'; lastChar = 'Z'; }
    }

    private static int getHowManyNumbers() {
        int h = Main.howManyValues;
        double d = Math.pow(h, n);
        return (int) d;
    }

    private static void printInvalidInputAndExit() {
        System.out.println("> Error: The input file does not contain a valid hash");
        Main.exit();
    }

    private static void printHashToBruteForceOn(String hash, String hashType) {
        System.out.println(" Attempting to unhash the following " + hashType + " hash: ");
        System.out.println(" >> " + hash);
        System.out.println("");
    }

    private static void iniTimer(int timerInterval) {
        printStatusTimer = new Timer();
        printStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double progress = ((float) counter/(float) n_numbers)*100;
                if (!stopTimer) printProgress(progress);
                else printStatusTimer.cancel();
            }
        }, timerInterval/5, timerInterval);
    }

    private static void printVerbose(String curr, String curr_hashed, String hash, boolean found) {
        if (!found) {
            System.out.print(" Testing... >> \t" + new String(curr) + " -> " + curr_hashed);
            System.out.print(" is not " + hash + "\n");
        }
        else {
            System.out.print(" \n!!FOUND... >> \t" + new String(curr) + " -> " + curr_hashed);
            System.out.print("   is   " + hash + "\n");
        }
    }

    private static void printProgress(double progess) {
        if (progess >= 100) progess = 99.99;
        String p = String.format("%.2f", progess);
        System.out.print("Processing... " + p + "% \t");
        if      (progess < 10) System.out.println("##### ...");
        else if (progess < 20) System.out.println("########## ...");
        else if (progess < 30) System.out.println("############### ...");
        else if (progess < 40) System.out.println("#################### ...");
        else if (progess < 50) System.out.println("######################### ...");
        else if (progess < 60) System.out.println("############################## ...");
        else if (progess < 70) System.out.println("################################### ...");
        else if (progess < 80) System.out.println("######################################## ...");
        else if (progess < 90) System.out.println("############################################# ...");
        else                   System.out.println("################################################## ...");
    }

    private static void printCrackedAndExit(String cracked) {
        System.out.println("\n The unhashed version of the input is: ");
        System.out.println(" >> " + cracked);
        Main.timeEnd = System.currentTimeMillis();
        Main.execTime = Main.timeEnd - Main.timeIni;
        System.out.println("\n It took me " + Main.execTime/1000 + "s to find the solution.");
        Main.exit(); // do not try more combinations!
    }

}
