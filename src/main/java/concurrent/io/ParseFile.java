package concurrent.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContent(Predicate<Character> filter)
                                                            throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedInputStream bis =
                new BufferedInputStream(
                        new FileInputStream(file)
                )) {
            int data;
            while ((data = bis.read()) != -1) {
                if (filter.test((char) data)) {
                    builder.append(data);
                }
            }
        }
        return builder.toString();
    }

    public synchronized String getContentWithoutUnicode()
                                                        throws IOException {
        return getContent(d -> d < 0x80);
    }
}
