package concurrent.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmailNotification {

    private final ExecutorService pool;
    private final static String PATH = "./src/main/java/concurrent/executor"
            + "/sample.txt";

    public EmailNotification(ExecutorService pool) {
        this.pool = pool;
    }

    public void emailTo(User user, String email) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                String[] parameters = new String[2];
                String sample = getSample(user);
                String[] arr = sample.split("\\.");
                parameters[0] = arr[0].substring(8);
                parameters[1] = arr[1].substring(7);
                send(parameters[0], parameters[1], email);
            }
        });
    }

    public void send(String subject, String body, String email) {

    }

    public void close() {
        pool.shutdown();
        try {
            pool.awaitTermination(10000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getSample(User user) {
        String sample = "";
        try {
            String read = Files.readString(Path.of(PATH));
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
