package concurrent.market;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Bakery implements Market {

    @GuardedBy("this")
    private int breadCount;
    private final int total;

    public Bakery(int total) {
        this.total = total;
    }

    @Override
    public synchronized void buy() {
        while (breadCount == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        breadCount--;
        this.notify();
        System.out.println("Потребитель купил один хлеб");
        System.out.println("Хлеба на ветрине осталось: " + breadCount);
    }

    @Override
    public synchronized void produce() {
        while (breadCount == total) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        breadCount++;
        this.notify();
        System.out.println("Производитель добавил на витрину магазина один "
                + "хлеб");
        System.out.println("Хлеба на ветрине осталось: " + breadCount);

    }

     static class Producer implements Runnable {

        private final Bakery bakery;

        public Producer(Bakery bakery) {
            this.bakery = bakery;
        }

        @Override
        public void run() {
            for (int i = 0; i < Bakery.PRODUCE_SALE_COUNT_PER_DAY; i++) {
                bakery.produce();
            }
        }
    }

    static class Consumer implements Runnable {

        private final Bakery bakery;

        public Consumer(Bakery bakery) {
            this.bakery = bakery;
        }

        @Override
        public void run() {
            for (int i = 0; i < Bakery.PRODUCE_SALE_COUNT_PER_DAY; i++) {
                bakery.buy();
            }
        }
    }
}
