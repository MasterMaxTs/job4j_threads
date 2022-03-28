package concurrent.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;
    private final int speed;
    private static String path = "";

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream bis =
                     new BufferedInputStream(
                             new URL(url).openStream());
             FileOutputStream fos = new FileOutputStream(path)) {
            byte[] dataBuffer = new byte[1024];
            long timeStart0 = System.currentTimeMillis();
            long timeStart = timeStart0;
            long timeFinish;
            int bytesRead;
            long delay;
            while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
                timeFinish = System.currentTimeMillis();
                fos.write(dataBuffer, 0, bytesRead);
                delay = getDelay(timeStart, timeFinish);
                Thread.sleep(delay);
                timeStart = timeFinish;
            }
            getResultInfo(timeStart0);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long getDelay(long timeStart, long timeFinish) {
        long koe =  1000L * 1024 / speed;
        return timeFinish - timeStart < koe ? koe : 0;
    }

    private void getResultInfo(long timeStart) {
        String ls = System.lineSeparator();
        System.out.printf("file uploaded successfully within %d millis.%s",
                System.currentTimeMillis() - timeStart, ls);
    }

    public static void getInputInfo(String arg0, int arg1, String path) {
        String ls = System.lineSeparator();
        File file = new File(path);
        System.out.printf("Input parameters > URL : %s ;"
                            + " SPEED : %d b/sec ; FILE : '%s' %d bytes%s",
                                arg0, arg1, file.getName(), file.length(), ls);
    }

    public static void main(String[] args) throws InterruptedException {
        path = "pom_tmp.xml";
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
        getInputInfo(url, speed, path);
    }
}
