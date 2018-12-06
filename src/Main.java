import java.io.IOException;

public class Main
{
    private static String[] flags; // [number, /path/to/file, type_hash, mode, print_interval, incremental, verbose
    public static int howManyValues;
    public static long timeIni;
    public static long timeEnd;
    public static long execTime;

    public static void main(String[] args) throws Exception
    {
        printHelloMessage();
        start(args);
    }

    private static void start(String[] args) throws Exception
    {
        timeIni = System.currentTimeMillis();
        flags = EvaluateArgs.evaluateArgs(args);

        int n = Integer.parseInt(flags[0]);
        String pathToFile = flags[1];
        String hashType   = flags[2];
        String mode       = flags[3];
        int printInterval = Integer.parseInt(flags[4]);
        boolean e = false;
        if(flags[5].equals("yes")) e = true;
        boolean v = false;
        if(flags[6].equals("yes")) v = true;

        BruteForce.start(n, pathToFile, hashType, mode, e, v, printInterval);
    }

    private static void printHelloMessage() throws InterruptedException {
        Thread.sleep(250);
        System.out.println("");
        System.out.println(" ############################################################################################################################");
        System.out.println(" ################################################## JUAN EL DESTRIPADOR #####################################################");
        System.out.println(" ############################################################################################################################");
        System.out.println("");
        Thread.sleep(250);
    }

    protected static void exit()
    {
        System.exit(0);
    }
}
