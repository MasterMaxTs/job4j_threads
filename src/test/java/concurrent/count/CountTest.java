package concurrent.count;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CountTest {

    /**
     * Класс описывает нить со счетчиком.
     */
    private static class ThreadCount extends Thread {

        private final Count count;

        public ThreadCount(Count count) {
            this.count = count;
        }


        @Override
        public void run() {
            this.count.increment();
        }
    }

    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        Count count = new Count();
        Thread first = new ThreadCount(count);
        Thread second = new ThreadCount(count);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(count.get(), is(2));
    }
}