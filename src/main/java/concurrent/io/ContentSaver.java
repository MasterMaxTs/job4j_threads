package concurrent.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContentSaver {

    private final String path;

    public ContentSaver(String path) {
        this.path = path;
    }

    public synchronized void saveContent(String content)
                                                        throws IOException {
        try (BufferedOutputStream bos =
                     new BufferedOutputStream(
                             new FileOutputStream(path)
                     )) {
            for (int i = 0; i < content.length(); i++) {
                bos.write(content.charAt(i));
            }
        }
    }
}
