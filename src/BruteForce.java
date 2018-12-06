import java.io.*;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class BruteForce
{
    private static String hash;
    private static String type;
    private static int n;
    private static boolean e;
    private static boolean v;
    private static Timer printStatusTimer;
    private static boolean stopTimer = false;
    private static long counter;
    private static long n_numbers;

    public static void start(int number, String pathToFile, String hashType, boolean exclusive, boolean verbose, int timerInterval) throws IOException {
        ReadFromFile.open(Paths.get(pathToFile));
        hash = ReadFromFile.read();          // hash to brute force on
        ReadFromFile.close();
        if (hash == null) printInvalidInputAndExit();
        printHashToBruteForceOn(hash, hashType);
        type = hashType;
        n = number; e = exclusive; v = verbose;
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
        char[] number;
        if (e) {
            // if the "-e" flag is activated -> only brute force numbers with 'n' digits
            number = new char[n];
            if (type.equals("sha1")) bruteForceSHA1(n,0,number);
//            if (type.equals("sha256")) bruteForceSHA256(n,0,number);
//            if (type.equals("sha512")) bruteForceSHA512(n,0,number);
        }
        else {
            // each call to recursive method "bruteForce(int, int, char[])" brute forces
            // all the possible combinations of digits with length 'n'
            for (int i = 1; i <= n; i++) {
                number = new char[i];
                if (type.equals("sha1")) bruteForceSHA1(i, 0, number);
//                if (type.equals("sha256")) bruteForceSHA256(i, 0, number);
//                if (type.equals("sha512")) bruteForceSHA512(i, 0, number);
            }
        }
    }

    private static void bruteForceSHA1(int limit, int k, char[] number) throws NoSuchAlgorithmException {
        if (limit == k) {
            String curr_hashed = Hash.SHA1(new String(number));
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(number), curr_hashed, hash, found); // bottleneck!
            if (found) printCrackedAndExit(new String(number));
            ++counter;
            return;
        }
        for (number[k] = '0'; number[k] <= '9'; ++number[k]) {
            bruteForceSHA1(limit, k + 1, number);
        }
    }

    private static boolean compareHash(String hash1, String hash2) {
        return (hash1.equals(hash2));
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

    private static void printHashToBruteForceOn(String hash, String hashType) {
        System.out.println(" Attempting to unhash the following " + hashType + " hash: ");
        System.out.println(" >> " + hash);
        System.out.println("");
    }

    private static void printCrackedAndExit(String cracked) {
        System.out.println("\n The unhashed version of the input is: ");
        System.out.println(" >> " + cracked);
        Main.exit(); // do not try more combinations!
    }

    private static void printInvalidInputAndExit() {
        System.out.println("> Error: The input file does not contain a valid hash");
        Main.exit();
    }

    private static int getHowManyNumbers() {
        int h = Main.howManyValues;
        double d = Math.pow(h, n);
        return (int) d;
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

}
