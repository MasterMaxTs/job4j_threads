package concurrent;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class CASCountTest {

    @Test
    public void when3incrementThan3Get() throws InterruptedException {
        CASCount casCount = new CASCount();
        Thread first = new Thread(
                () -> IntStream.range(0, 1000).forEach(i -> casCount.increment())
        );
        Thread second = new Thread(
                () -> IntStream.range(0, 1000).forEach(i -> casCount.increment())
        );
        Thread third = new Thread(
                () -> IntStream.range(0, 1000).forEach(i -> casCount.increment())
        );
        first.start();
        second.start();
        third.start();
        first.join();
        second.join();
        third.join();
        assertThat(casCount.get(), is(3000));
    }
}