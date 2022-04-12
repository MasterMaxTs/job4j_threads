package concurrent.control;

public class CountBarrier {

    private final Object monitor = this;
    private final int total;
    private static int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(2);
        Thread first = new Thread(
                () -> {
                    countBarrier.count();
                    System.out.println(Thread.currentThread().getName()
                            + " has incremented the counter value! Counter "
                            + "value is " + count);
                },
                "Master1"
        );
        Thread second = new Thread(
                () -> {
                    countBarrier.count();
                    System.out.println(Thread.currentThread().getName()
                            + " has incremented the counter value! Counter "
                            + "value is " + count);
                },
                "Master2"
        );
        Thread third = new Thread(
                () -> {
                    countBarrier.await();
                    System.out.println(Thread.currentThread().getName()
                    + "start! Counter value is " + count);
                },
                "Slave"
        );
        first.start();
        second.start();
        third.start();
    }
}
