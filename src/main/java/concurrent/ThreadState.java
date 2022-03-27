package concurrent;

public class ThreadState {
    public static void main(String[] args) throws InterruptedException {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        getThreadInfo(first);
        getThreadInfo(second);
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                            && second.getState() != Thread.State.TERMINATED) {
            getThreadInfo(first);
            getThreadInfo(second);
        }
        getThreadInfo(first);
        getThreadInfo(second);
        first.join();
        second.join();
        System.out.println("Work completed!");
    }

    private static void getThreadInfo(Thread thread) {
        System.out.println(thread.getName() + " - " + thread.getState());
    }
}
