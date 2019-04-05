import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteToFile
{
    protected static File file;
    private static FileWriter fw;
    //protected static PrintStream psFile;

    protected static void open(String path) throws IOException {
        file = new File(path);
        new File(new File(path).getParent()).mkdirs();
        file.createNewFile();
        fw = new FileWriter(file, false);

        WriteToFile.write("\n");
        WriteToFile.write(" ############################################################################################################################\n");
        WriteToFile.write(" ################################################## JUAN EL DESTRIPADOR #####################################################\n");
        WriteToFile.write(" ############################################################################################################################\n");
        WriteToFile.write("\n");

        //psFile = new PrintStream(file);
    }

    protected static void write(String word) throws IOException {
        fw.write(word);
    }

    protected static void close() throws IOException {
        fw.close();
        // close file
    }

    protected static String generateFilename() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return (System.getProperty("user.dir") + File.separator + "out" + File.separator + "out-juan_" + dateFormat.format(new Date()) + ".txt");
    }
}
