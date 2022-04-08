package concurrent.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Predicate;

public class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContent(Predicate<Character> filter)
                                                            throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new FileReader(file)
        )) {
            String res = "";
            String ls = System.lineSeparator();
            while ((res = br.readLine()) != null) {
                builder.append(res).append(ls);
            }
        }
        return builder.toString();
    }

    public synchronized String getContentWithoutUnicode()
                                                        throws IOException {
        return getContent(d -> d < 0x80);
    }
}
