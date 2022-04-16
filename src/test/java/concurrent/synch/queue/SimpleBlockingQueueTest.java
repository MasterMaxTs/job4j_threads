package concurrent.synch.queue;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer =
                                             new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue =
                                            new SimpleBlockingQueue<>(5);

        Thread producer = new Thread(
                () ->
                    IntStream.range(0, 5).forEach(
                            i -> {
                                try {
                                    queue.offer(i);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                    }
                    ));
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(List.of(0, 1, 2, 3, 4)));
    }
}