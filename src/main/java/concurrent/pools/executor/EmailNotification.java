package concurrent.pools.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool;
    private final static String FILE = "./src/main/java/concurrent/pools/"
            + "executor/sample.txt";

    public EmailNotification(ExecutorService pool) {
        this.pool = pool;
    }

    private void emailTo(User user, String email) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                String sample = getSample(user);
                String[] values = sample.split("=");
                send(values[0], values[2], email);
            }
        });
    }

    public void send(String subject, String body, String email) {

    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSample(User user) {
        String sample = "";
        try {
            String read = Files.readString(Path.of(FILE));
            sample = read
                    .replaceAll("\\{username}", user.getUsername())
                    .replaceAll("\\{email}", user.getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sample;
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        EmailNotification emailNotification = new EmailNotification(service);
        emailNotification.emailTo(
                new User("Test", "test@test"),
                "It's test email to user");
        emailNotification.close();
    }
}
