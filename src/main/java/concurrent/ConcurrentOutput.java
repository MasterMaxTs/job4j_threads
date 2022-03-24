package concurrent;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                    for (int i = 0; i < 5 ; i++) {
                        System.out.println(Thread.currentThread().getName());
                    }
                });
        Thread second = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println(Thread.currentThread().getName());
                    }
                });
        first.start();
        second.start();
        System.out.println(Thread.currentThread().getName());
    }
}
