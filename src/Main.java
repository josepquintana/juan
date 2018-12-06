
public class Main
{
    public static int howManyValues = 10;
    public static long timeIni;
    public static long timeEnd;
    public static long execTime;

    public static void main(String[] args) throws Exception
    {
        printHelloMessage();
        timeIni = System.currentTimeMillis();

        String[] flags; // [n, /path/to/file, type_hash, e, v
        flags = EvaluateArgs.evaluateArgs(args);
        int n = Integer.parseInt(flags[0]);
        String pathToFile = flags[1];
        String hashType   = flags[2];
        String mode       = flags[3];
        boolean e = false;
        if(flags[4].equals("yes")) e = true;
        boolean v = false;
        if(flags[5].equals("yes")) v = true;

        BruteForce.start(n, pathToFile, hashType, mode, e, v, 1000);

        // the program only gets here if no hash coincidence has been found.
        System.out.println("Processing... 100.00%\t################################################## !!!\n");
        Thread.sleep(500);
        System.out.println("Sorry... the unhashed version of the input could not be found.");
        Thread.sleep(500);
        timeEnd = System.currentTimeMillis();
        execTime = timeEnd-timeIni;
        System.out.println("You wasted " + execTime/1000 + "s ...");
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
