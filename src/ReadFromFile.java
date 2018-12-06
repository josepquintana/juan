import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ReadFromFile
{
    private static File file;
    private static BufferedReader br;

    protected static void open(Path path) throws IOException {
        file = new File(String.valueOf(path));
        br = new BufferedReader(new FileReader(file));
    }

    protected static String read() throws IOException {
        String ln;
        while ((ln = br.readLine()) != null) {
            if (ln.charAt(0) == '#' || ((ln.charAt(0) == '/') && (ln.charAt(1) == '/'))) continue; // this input line is a comment
            ln = getHashFromInputLine(ln);
            return ln; // only reads the first valid line
        }
        return null; // never reaches this point if valid input file
    }

    protected static void close() throws IOException {
        br.close();
        // close file
    }

    private static String getHashFromInputLine(String ln) {
        String[] full_ln = ln.split("\\s+");
        return full_ln[0];
    }
}
