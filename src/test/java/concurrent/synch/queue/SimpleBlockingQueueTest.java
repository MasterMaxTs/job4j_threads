package concurrent.synch.queue;

import org.junit.Before;
import org.junit.Test;

import  static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {
    private SimpleBlockingQueue<Integer> queue;
    private Thread producer1;
    private Thread producer2;
    private Thread consumer1;
    private Thread consumer2;

    @Before
    public void whenSetUp() {
        queue = new SimpleBlockingQueue<>(2);
        producer1 = new Thread(
                () -> {
                    try {
                        queue.offer(1);
                        queue.offer(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        producer2 = new Thread(
                () -> {
                    try {
                        queue.offer(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        consumer1 = new Thread(
                () -> {
                    try {
                        queue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        consumer2 = new Thread(
                () -> {
                    try {
                        queue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Test
    public void whenOfferAndAllPollThanEmpty() throws InterruptedException {
        producer1.start();
        consumer1.start();
        consumer2.start();
        producer1.join();
        consumer1.join();
        consumer2.join();
        assertThat(queue.size(), is(0));
    }

    @Test
    public void whenTwoProducerAndOneConsumerThanPoll() throws InterruptedException {
        producer1.start();
        Thread.sleep(1000);
        producer2.start();
        consumer1.start();
        producer2.join();
        consumer1.join();
        assertThat(queue.poll(), is(2));
        assertThat(queue.poll(), is(3));
    }

    @Test
    public void whenTwoProducerAndOneProducerWaitThanPoll() throws InterruptedException {
        producer1.start();
        Thread.sleep(1000);
        producer2.start();
        producer2.interrupt();
        assertThat(queue.poll(), is(1));
        assertThat(queue.poll(), is(2));
    }
}