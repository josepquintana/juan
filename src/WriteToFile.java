import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class WriteToFile
{
    private static File file;
    private static FileWriter fw;

    protected static void open(Path path) throws IOException {
        file = new File(String.valueOf(path));
        file.createNewFile();
        fw = new FileWriter(file, false);
    }

    protected static void write(String word) throws IOException {
        fw.write(word);
        fw.write("\n");
    }

    protected static void close() throws IOException {
        fw.close();
        // close file
    }
}
