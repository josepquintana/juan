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
    private static boolean inc;
    private static boolean v;
    private static boolean stopTimer = false;
    private static long counter;
    private static long combinations;

    public static void start(int number, String pathToFile, String hashType, String configMode, boolean incremental, boolean verbose, int timerInterval) throws IOException, InterruptedException {
        ReadFromFile.open(Paths.get(pathToFile));
        hash = ReadFromFile.read();          // hash to brute force on
        ReadFromFile.close();
        if (hash == null) printInvalidInputAndExit();
        printHashToBruteForceOn(hash, hashType);
        type = hashType; mode = configMode; n = number; inc = incremental; v = verbose;
        setWorkingMode();
        counter = 0; combinations = getHowManyNumbers();
        if (!v) iniTimer(timerInterval);
        try {
            bruteForce();   // brute force all possible combinations
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        stopTimer = true;
        printNoResult();
    }

    private static void bruteForce() throws NoSuchAlgorithmException, InterruptedException {
        char[] chain;
        if (!inc) {
            // if the "-e" flag is activated -> only brute force numbers with 'n' digits
            chain = new char[n];
            if (type.equalsIgnoreCase("md5"))    bruteForceMD5   (n,0,chain);
            if (type.equalsIgnoreCase("sha1"))   bruteForceSHA1  (n,0,chain);
            if (type.equalsIgnoreCase("sha256")) bruteForceSHA256(n,0,chain);
            if (type.equalsIgnoreCase("sha512")) bruteForceSHA512(n,0,chain);
        }
        else {
            // each call to recursive method "bruteForce(int, int, char[])" brute forces
            // all the possible combinations of digits with length 'n'
            for (int i = 1; i <= n; i++) {
                chain = new char[i];
                if (type.equalsIgnoreCase("sha512")) bruteForceSHA512(i, 0, chain);
                if (type.equalsIgnoreCase("sha256")) bruteForceSHA256(i, 0, chain);
                if (type.equalsIgnoreCase("sha1"))   bruteForceSHA1  (i, 0, chain);
                if (type.equalsIgnoreCase("md5"))    bruteForceMD5   (i, 0, chain);
            }
        }
    }

    private static void bruteForceMD5(int limit, int k, char[] chain) throws NoSuchAlgorithmException, InterruptedException {
        if (limit == k) {
            String curr_hashed = Hash.MD5(new String(chain));
            ++counter;
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) { stopTimer = true; printCrackedAndExit(new String(chain)); }
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceMD5(limit, k + 1, chain);
        }
    }

    private static void bruteForceSHA1(int limit, int k, char[] chain) throws NoSuchAlgorithmException, InterruptedException {
        if (limit == k) {
            String curr_hashed = Hash.SHA1(new String(chain));
            ++counter;
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) { stopTimer = true; printCrackedAndExit(new String(chain)); }
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceSHA1(limit, k + 1, chain);
        }
    }

    private static void bruteForceSHA256(int limit, int k, char[] chain) throws NoSuchAlgorithmException, InterruptedException {
        if (limit == k) {
            String curr_hashed = Hash.SHA256(new String(chain));
            ++counter;
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) { stopTimer = true; printCrackedAndExit(new String(chain)); }
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceSHA256(limit, k + 1, chain);
        }
    }

    private static void bruteForceSHA512(int limit, int k, char[] chain) throws NoSuchAlgorithmException, InterruptedException {
        if (limit == k) {
            String curr_hashed = Hash.SHA512(new String(chain));
            ++counter;
            boolean found = false;
            if (compareHash(curr_hashed, hash)) found = true;
            if(v) printVerbose(new String(chain), curr_hashed, hash, found); // bottleneck!
            if (found) { stopTimer = true; printCrackedAndExit(new String(chain)); }
            return;
        }
        for (chain[k] = iniChar; chain[k] <= lastChar; ++chain[k]) {
            if (mode.equalsIgnoreCase("letters") && chain[k] == '[') chain[k] = 'a'; // '[' is the following to 'Z'
            bruteForceSHA512(limit, k + 1, chain);
        }
    }

    private static boolean compareHash(String hash1, String hash2) {
        return (hash1.equals(hash2));
    }

    private static void setWorkingMode() {
        // set the first and last character to iterate into depending on the working mode
        if (mode.equalsIgnoreCase("all"))       { iniChar = '!'; lastChar = '~'; Main.howManyValues = 94; }
        if (mode.equalsIgnoreCase("numbers"))   { iniChar = '0'; lastChar = '9'; Main.howManyValues = 10; }
        if (mode.equalsIgnoreCase("letters"))   { iniChar = 'A'; lastChar = 'z'; Main.howManyValues = 52; }
        if (mode.equalsIgnoreCase("lowercase")) { iniChar = 'a'; lastChar = 'z'; Main.howManyValues = 26; }
        if (mode.equalsIgnoreCase("uppercase")) { iniChar = 'A'; lastChar = 'Z'; Main.howManyValues = 26; }
    }

    private static long getHowManyNumbers() {
        int h = Main.howManyValues;
        long combs = 0;
        if(inc) {
            for (int i = 1; i <= n; i++) {
                combs += (long) Math.pow(h, i);
            }
        }
        else {
            combs = (long) Math.pow(h, n);
        }
        return combs;
    }

    private static void printInvalidInputAndExit() {
        System.out.println("> Error: The input file does not contain a valid hash");
        Main.exit();
    }

    private static void printHashToBruteForceOn(String hash, String hashType) throws InterruptedException {
        System.out.println(" Attempting to unhash the following " + hashType + " hash: ");
        System.out.println(" >> " + hash);
        System.out.println("");
        Thread.sleep(250);
    }

    private static void iniTimer(int timerInterval) {
        Timer printStatusTimer = new Timer();
        printStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double progress = (((double) counter/(double) combinations)*100);
                if (!stopTimer) printProgress(progress);
                else printStatusTimer.cancel();
            }
        }, 5, timerInterval);
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
        progess = Double.parseDouble(String.format("%.3f", progess));
        System.out.print(" Processing... " + progess + "% \t");

        int spaces = String.valueOf(combinations).length() - String.valueOf(counter).length();
        for (int i = 0; i <= spaces; i++) { System.out.print(" "); }
        System.out.print(counter + "/" + combinations + "  \t");


        if      (progess == 0) System.out.print("|......................................................|");
        else if (progess < 10) System.out.print("|#####.................................................|");
        else if (progess < 20) System.out.print("|##########............................................|");
        else if (progess < 30) System.out.print("|###############.......................................|");
        else if (progess < 40) System.out.print("|####################..................................|");
        else if (progess < 50) System.out.print("|#########################.............................|");
        else if (progess < 60) System.out.print("|##############################........................|");
        else if (progess < 70) System.out.print("|###################################...................|");
        else if (progess < 80) System.out.print("|########################################..............|");
        else if (progess < 90) System.out.print("|#############################################.........|");
        else                   System.out.print("|##################################################....|");

        updateExecTime();
        long t = Main.execTime;
        if (t <= 10000) {
            System.out.print(" \t time: " + t + "ms\n");                                  // 1000ms = 10s
        }
        else if(t <= 60000) {
            System.out.print(" \t time: " + t/1000 + "s " + ((t/1000)%60) + "ms\n");      // 60s = 1min
        }
        else if(t <= 3600000) {
            System.out.print(" \t time: " + t/60000 + "min " + ((t/1000)%60) + "s " + (t%1000) + "ms\n");    // 60min = 1h
        }
        else if(t <= 86400000) {
            System.out.print(" \t time: " + t/3600000 + "h " + ((t/60000)%60) + "min " + (t%1000) + "s\n");  // 24h = 1day
        }
        else {
            if(t/86400000 == 1) System.out.print(" \t time: " + t/86400000 + "day ");
            else                System.out.print(" \t time: " + t/86400000 + "days ");
            System.out.print(t/3600000 + "h " + ((t/60000)%60) + "min " + ((t/1000)%60) + "s\n");
        }

    }

    private static void updateExecTime() {
        Main.timeEnd = System.currentTimeMillis();
        Main.execTime = Main.timeEnd - Main.timeIni;
    }

    private static void printNoResult() throws InterruptedException {
        // the program only gets here if no hash coincidence has been found.
        System.out.println("\n Processing... 100.00% \t " + combinations + "/" + combinations + " \t|######################################################|");
        Thread.sleep(250);
        System.out.print(" \n Sorry... the unhashed version of the input could not be found.");
        Thread.sleep(500);

        long t = Main.execTime;
        if (t <= 10000) {
            System.out.print(" You wasted " + t + "ms\n");                                  // 1000ms = 10s
        }
        else if(t <= 60000) {
            System.out.print(" You wasted " + t/1000 + "s " + ((t/1000)%60) + "ms\n");      // 60s = 1min
        }
        else if(t <= 3600000) {
            System.out.print(" You wasted " + t/60000 + "min " + ((t/1000)%60) + "s " + (t%1000) + "ms\n");    // 60min = 1h
        }
        else if(t <= 86400000) {
            System.out.print(" You wasted " + t/3600000 + "h " + ((t/60000)%60) + "min " + (t%1000) + "s\n");  // 24h = 1day
        }
        else {
            if(t/86400000 == 1) System.out.print(" You wasted " + t/86400000 + "day ");
            else                System.out.print(" You wasted " + t/86400000 + "days ");
            System.out.print(t/3600000 + "h " + ((t/60000)%60) + "min " + ((t/1000)%60) + "s\n");
        }
    }

    private static void printCrackedAndExit(String cracked) throws InterruptedException {
        System.out.println("\n Processing... 100.00% \t " + combinations + "/" + combinations + " \t|######################################################|");
        Thread.sleep(250);
        System.out.println("\n The unhashed version of the input is: ");
        System.out.println(" >> " + cracked);
        Main.timeEnd = System.currentTimeMillis();
        Main.execTime = Main.timeEnd - Main.timeIni;

        long t = Main.execTime;
        if (t <= 10000) {
            System.out.print("\n It took me " + t + " milliseconds");  // 1000ms = 10s
        }
        else if(t <= 60000) {
            System.out.print("\n It took me " + t/1000 + " seconds and " + ((t/1000)%60) + " milliseconds"); // 60s = 1min
        }
        else if(t <= 3600000) {
            if(t/60000 == 1) System.out.print("\n It took me " + t/60000 + " minute, "  + ((t/1000)%60) + " seconds and " + (t%1000) + " milliseconds");    // 60min = 1h
            else             System.out.print("\n It took me " + t/60000 + " minutes, " + ((t/1000)%60) + " seconds and " + (t%1000) + " milliseconds");    // 60min = 1h
        }
        else if(t <= 86400000) {
            if(t/3600000 == 1) System.out.print("\n It took me " + t/3600000 + " hour, "  + ((t/60000)%60) + " minutes and " + (t%1000) + " seconds");  // 24h = 1day
            else               System.out.print("\n It took me " + t/3600000 + " hours, " + ((t/60000)%60) + " minutes and " + (t%1000) + " seconds");  // 24h = 1day
        }
        else {
            if(t/86400000 == 1) System.out.print("\n It took me " + t/86400000 + " day, " );
            else                System.out.print("\n It took me " + t/86400000 + " days, ");
            System.out.print(t/3600000 + " hours, " + ((t/60000)%60) + " minutes and " + ((t/1000)%60) + " seconds");
        }

        System.out.print(" to find the solution.\n");
        Main.exit(); // do not try more combinations!
    }

}
