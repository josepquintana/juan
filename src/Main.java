import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    private static String[] flags; // [number, /path/to/file, type_hash, mode, print_interval, incremental, verbose
    public static int howManyValues;
    public static long timeIni;
    public static long timeEnd;
    public static long execTime;
    protected static boolean hashInFile;
    protected static ArrayList<String> types;
    protected static ArrayList<String> modes;

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
        boolean w = false;
        if(flags[7].equals("yes")) w = true;

        BruteForce.start(n, pathToFile, hashType, mode, printInterval, e, v, w, hashInFile);
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
        try {
            WriteToFile.close();
            System.out.print("\n > Output generated in the file: ");
            System.out.println(WriteToFile.file.getAbsolutePath() + "\n");
        } catch (IOException e) {
            System.out.println("\n\n >> !!Error while writing to output file.\n");
            e.printStackTrace();
        }
        System.exit(0);
    }
}
