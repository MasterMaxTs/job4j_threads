package concurrent.market;

public class WaitNotifyExample {
    public static void main(String[] args) throws InterruptedException {
        Bakery bakery = new Bakery(5);
        Thread first = new Thread(new Bakery.Producer(bakery), "producer");
        Thread second = new Thread(new Bakery.Consumer(bakery), "consumer");
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println("Bakery closed!");
    }
}
