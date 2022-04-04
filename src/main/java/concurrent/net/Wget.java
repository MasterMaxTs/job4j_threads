package concurrent.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;
    private final int speed;
    private static String path;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
        path = getPathFromUrl(url);
    }

    @Override
    public void run() {
        long timeBegin = System.currentTimeMillis();
        try (BufferedInputStream bis =
                     new BufferedInputStream(
                             new URL(url).openStream());
             FileOutputStream fos = new FileOutputStream(path)) {
            byte[] dataBuffer = new byte[1024];
            long timeStart = timeBegin;
            long timeFinish;
            long delta;
            int bytesRead;
            int downloadData = 0;
            while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
                downloadData += bytesRead;
                if (downloadData >= speed) {
                    timeFinish = System.currentTimeMillis();
                    delta = timeFinish - timeStart;
                    if (delta < 1000) {
                        Thread.sleep(1000 - delta);
                    }
                    timeStart = System.currentTimeMillis();
                    downloadData = 0;
                }
                fos.write(dataBuffer, 0, bytesRead);
            }
            getResultInfo(timeBegin);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getPathFromUrl(String url) {
        String[] arr = url.split("/");
        return arr[arr.length - 1];
    }

    private void getResultInfo(long timeBegin) {
        String ls = System.lineSeparator();
        System.out.printf("file uploaded successfully within %d millis.%s",
                System.currentTimeMillis() - timeBegin, ls);
    }

    public static void getFileInfo(String arg0, int arg1) {
        String ls = System.lineSeparator();
        File file = new File(path);
        System.out.printf("Input parameters > URL : %s ;"
                            + " SPEED : %d b/sec ; FILE : '%s' %d bytes%s",
                                arg0, arg1, file.getName(), file.length(), ls);
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
        getFileInfo(url, speed);
    }
}
