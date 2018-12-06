import java.nio.file.Paths;

public class Main
{
    public static int howManyValues = 10;

    public static void main(String[] args) throws Exception
    {
        printHelloMessage();

        String[] flags; // [n, /path/to/file, type_hash, e, v
        flags = EvaluateArgs.evaluateArgs(args);
        int n = Integer.parseInt(flags[0]);
        String pathToFile = flags[1];
        String hashType   = flags[2];
        boolean e = false;
        if(flags[3].equals("yes")) e = true;
        boolean v = false;
        if(flags[4].equals("yes")) v = true;

        BruteForce.start(n, pathToFile, hashType, e, v, 1000);

        System.out.println("Processing... 100.00%\t################################################## !!!\n");
        Thread.sleep(500);
        System.out.println("Sorry... the unhashed version of the input could not be found.");
    }

    private static void printHelloMessage() {
        System.out.println("");
        System.out.println("####################################################################################");
        System.out.println("############################## JUAN EL DESTRIPADOR #################################");
        System.out.println("####################################################################################");
        System.out.println("");
    }

    protected static void exit() {
        System.exit(0);
    }
}
