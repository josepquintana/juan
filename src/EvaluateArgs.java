import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class EvaluateArgs
{
    private static ArrayList<String> types;

    public static String[] evaluateArgs(String[] args) throws Exception {

        types = new ArrayList<>();
        knownHashTypes();

        String[] flags = new String[5]; // [n, /path/to/file, type_hash, e, v]
        flags[0] = "0"; flags[1] = ""; flags[2] = ""; flags[3] = "no"; flags[4] = "no";

        if (args.length >= 1 && (args[0].equals("-h") || args[0].equals("--help"))) { printUsageAndExit(); }
        if (args.length >= 1 && (args[0].equals("-s") || args[0].equals("--show_hash_types"))) { printKnownHashTypesAndExit(); }

        boolean n = false, f = false, t = false, e = false, v = false;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-n") || args[i].equals("--number")) {
                if(n) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isInteger(args[i+1])) printInvalidNumberAndExit(args[i], args[i+1]);
                flags[0] = args[i+1]; ++i;
                n = true;
            }
            else if (args[i].equals("-f") || args[i].equals("--file")) {
                if(f) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isValidPath(args[i+1])) printInvalidPathAndExit(args[i+1]);
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
            else if (args[i].equals("-e") || args[i].equals("--exclusive")) {
                if(e) printErrorDuplicateFlagAndExit(args[i]);
                flags[3] = "yes";
                e = true;
            }
            else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                if(v) printErrorDuplicateFlagAndExit(args[i]);
                flags[4] = "yes";
                v = true;
            }
            else {
                System.out.println("> Error: Unknown specified flag '" + args[i] + "'.");
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
            System.out.print("Enter the number of digits [e.g. 8]: >> ");
            num = s.next();
        } while(!isInteger(num));
        System.out.println("");
        return num;
    }

    private static String askHashType() {
        Scanner s = new Scanner(System.in);
        String hash;
        do {
            System.out.print("Enter the hash type of your input [e.g. sha1]: >> ");
            hash = s.next();
        } while(!isKnownHash(hash));
        System.out.println("");
        return hash;
    }

    private static void printMissingPathAndExit() {
        System.out.println("> Error: A file with a hash is required!");
        System.exit(0);
    }

    private static void printInvalidNumberAndExit(String currFlag, String NaN) {
        System.out.println("> Error: The succeeding element of '" + currFlag + "' should be an integer.");
        System.out.println(">> Error: The input is NaN: '" + NaN + "'.");
        System.exit(0);
    }

    private static void printInvalidPathAndExit(String path) {
        if (!Paths.get(path).isAbsolute()) path = "./" + path;
        System.out.println("> Error: The file path '" + path + "' is not valid.");
        System.exit(0);
    }

    private static void printUnknownHashAndExit(String currFlag, String type) {
        System.out.println("> Error: The succeeding element of '" + currFlag + "' should be a valid hash type.");
        System.out.println(">> Error: The input is unknown: '" + type + "'.");
        System.exit(0);
    }

    private static void printErrorDuplicateFlagAndExit(String currFlag) {
        System.out.println("> Error: The flag '" + currFlag + "' is duplicated.");
        System.exit(0);
    }

    private static void printUsageAndExit() throws Exception {
        System.out.println("Usage: java -jar NumberList.jar ");
        System.out.println("");
        System.out.println("    -h, --help");
        System.out.println("    -s, --show_hash_types");
        System.out.println("    -n, --number 'NUM'");
        System.out.println("    -f, --file '/path/to/input/file'");
        System.out.println("    -t, --type_hash");
        System.out.println("    -e, --exclusive");
        System.out.println("    -v, --verbose");
        System.out.println("");
        System.exit(0);
    }

    private static void printKnownHashTypesAndExit() {
        System.out.println("Known hash types: \n");
        for (int i = 0; i < types.size(); i++) {
            System.out.print(types.get(i) + " \t");
        }
        System.out.println("");
        System.exit(0);
    }

    private static void knownHashTypes() {
        types.add("sha1");
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isValidPath(String s) {
        Path path = Paths.get(s).toAbsolutePath().normalize();
        // check that the file exists and is a readable
        return (Files.exists(path) && Files.isReadable(path));
    }

    private static boolean isKnownHash(String s) {
        for (int i = 0; i < types.size(); i++) {
            if(types.get(i).equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}
