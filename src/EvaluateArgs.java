import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class EvaluateArgs
{

    public static String[] evaluateArgs(String[] args) throws Exception {

        Main.types = new ArrayList<>(); setTypes();
        Main.modes = new ArrayList<>(); setModes();

        String[] flags = new String[8]; // [n, /path/to/file, type_hash, mode, p, i, v, write_to_file]
        flags[0] = "0"; flags[1] = ""; flags[2] = ""; flags[3] = "all"; flags[4] = "2000"; flags[5] = "no"; flags[6] = "no"; flags[7] = "no";

        boolean n = false, f = false, t = false, m = false, p = false, e = false, v = false, w = false;
        for (int i = 0; i < args.length; i++)
        {
            if      (args[i].equals("-h") || args[i].equals("--help")) { printUsageAndExit(); }
            else if (args[i].equals("-s") || args[i].equals("--show_config")) { printConfigAndExit(); }
            else if (args[i].equals("-n") || args[i].equals("--number")) {
                if(n) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isValidNumber(args[i+1])) printInvalidNumberAndExit(args[i], args[i+1]);
                flags[0] = args[i+1]; ++i;
                n = true;
            }
            else if (args[i].equals("-f") || args[i].equals("--file")) {
                if(f) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isValidPath(args[i+1])) {
                    if(!Hash.isValidHash(args[i+1], "ALL")) {
                        printInvalidPathOrHashAndExit(args[i+1]);
                    }
                    else Main.hashInFile = false; // "hash" passed by program argument is valid.
                }
                else Main.hashInFile = true; // "hash" is stored in a file
                flags[1] = args[i+1]; ++i;
                f = true;
            }
            else if (args[i].equals("-t") || args[i].equals("--type_hash")) {
                if(t) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isKnownHash(args[i+1])) printUnknownHashAndExit(args[i], args[i+1]);
                flags[2] = args[i+1]; ++i;
                t = true;
            }
            else if (args[i].equals("-m") || args[i].equals("--mode")) {
                if(m) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isKnownMode(args[i+1])) printUnknownModeAndExit(args[i], args[i+1]);
                flags[3] = args[i+1]; ++i;
                m = true;
            }
            else if (args[i].equals("-p") || args[i].equals("--print_interval")) {
                if(p) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isValidTimer(args[i+1])) printInvalidTimerAndExit(args[i], args[i+1]);
                flags[4] = args[i+1]; ++i;
                p = true;
            }
            else if (args[i].equals("-i") || args[i].equals("--incremental")) {
                if(e) printErrorDuplicateFlagAndExit(args[i]);
                flags[5] = "yes";
                e = true;
            }
            else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                if(v) printErrorDuplicateFlagAndExit(args[i]);
                flags[6] = "yes";
                v = true;
            }
            else if (args[i].equals("-w") || args[i].equals("--write_to_file")) {
                if(w) printErrorDuplicateFlagAndExit(args[i]);
                flags[7] = "yes";
                w = true;
            }
            else {
                System.out.println(" > Error: Unknown specified flag '" + args[i] + "'. Please run \"juan.jar --help\".");
                System.exit(0);
            }
        }

        if(!f) printMissingPathAndExit();
        if(!n) flags[0] = askNumericInput();
        if(!t) flags[2] = askHashType();

        return flags;
    }

    private static String askNumericInput() {
        Scanner s = new Scanner(System.in);
        String num;
        do {
            System.out.print(" Enter the number of digits [e.g. 8]: >> ");
            num = s.next();
        } while(!isValidNumber(num));
        System.out.println("");
        return num;
    }

    private static String askHashType() {
        Scanner s = new Scanner(System.in);
        String hash;
        do {
            System.out.print(" Enter the hash type of your input [e.g. sha1]: >> ");
            hash = s.next();
        } while(!isKnownHash(hash));
        System.out.println("");
        return hash;
    }

    private static void printMissingPathAndExit() {
        System.out.println(" > Error: A file with a hash is required!");
        System.exit(0);
    }

    private static void printInvalidNumberAndExit(String currFlag, String num) {
        System.out.println(" > Error: The succeeding element of '" + currFlag + "' should be an integer.");
        System.out.println(" >> Error: The input is not a valid integer: '" + num + "'.");
        System.exit(0);
    }

    private static void printInvalidTimerAndExit(String currFlag, String num) {
        System.out.println(" > Error: The succeeding element of '" + currFlag + "' should be an integer.");
        System.out.println(" >> Error: The input timer interval: '" + num + "' is invalid or too low. [min. 5 ms]");
        System.exit(0);
    }

    private static void printInvalidPathOrHashAndExit(String path) {
        if (!Paths.get(path).isAbsolute()) path = "./" + path;
        System.out.println(" > Error: '" + path + "' is not valid file path nor a valid hash.");
        System.exit(0);
    }

    private static void printUnknownHashAndExit(String currFlag, String type) {
        System.out.println(" > Error: The succeeding element of '" + currFlag + "' should be a valid hash type.");
        System.out.println(" >> Error: The input is unknown: '" + type + "'.");
        System.exit(0);
    }

    private static void printUnknownModeAndExit(String currFlag, String mode) {
        System.out.println(" > Error: The succeeding element of '" + currFlag + "' should be a valid mode.");
        System.out.println(" >> Error: The input is unknown: '" + mode + "'.");
        System.exit(0);
    }

    private static void printErrorDuplicateFlagAndExit(String currFlag) {
        System.out.println(" > Error: The flag '" + currFlag + "' is duplicated.");
        System.exit(0);
    }

    private static void printUsageAndExit() {
        System.out.println(" Usage: java -jar juan.jar ");
        System.out.println("");
        System.out.println("    -h, --help");
        System.out.println("    -s, --show_config");
        System.out.println("    -n, --number 'NUM'");
        System.out.println("    -t, --type_hash");
        System.out.println("    -m, --mode");
        System.out.println("    -i, --incremental");
        System.out.println("    -p, --print_interval 'NUM'");
        System.out.println("    -v, --verbose");
        System.out.println("    -w, --write_to_file");
        System.out.println("    -f, --file '/path/to/input/file'");
        System.out.println("               ` echo <passwd_plain_text> | tr -d '\\n' | sha256sum | cut -f 1 -d ' ' `   # only bash terminal");
        System.out.println("");
        System.exit(0);
    }

    private static void printConfigAndExit() throws InterruptedException {
        System.out.println(" > Modes: \n");
        System.out.print(" ");
        for (int i = 0; i < Main.modes.size(); i++) {
            System.out.print(Main.modes.get(i) + "    ");
        }
        Thread.sleep(250);
        System.out.println("\n\n > Known hash types: \n");
        System.out.print(" ");
        for (int i = 0; i < Main.types.size(); i++) {
            System.out.print(Main.types.get(i) + "      ");
        }
        System.out.println("\n");
        System.exit(0);
    }

    private static void setTypes() {
        Main.types.add("md5");
        Main.types.add("sha1");
        Main.types.add("sha256");
        Main.types.add("sha512");
    }

    private static void setModes() {
        Main.modes.add("all");
        Main.modes.add("numbers");
        Main.modes.add("letters");
        Main.modes.add("lowercase");
        Main.modes.add("uppercase");
    }

    private static boolean isValidNumber(String s) {
        try {
            int num = Integer.parseInt(s);
            if(num < 0) return false;
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isValidPath(String s) {
        Path path = Paths.get(s).toAbsolutePath().normalize();
        // check that the file exists and is readable
        return (Files.exists(path) && Files.isReadable(path));
    }

    private static boolean isKnownHash(String s) {
        for (int i = 0; i < Main.types.size(); i++) {
            if(Main.types.get(i).equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    private static boolean isKnownMode(String s) {
        for (int i = 0; i < Main.modes.size(); i++) {
            if(Main.modes.get(i).equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    private static boolean isValidTimer(String s) {
        try {
            int t = Integer.parseInt(s);
            if (t < 5) return false;
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
