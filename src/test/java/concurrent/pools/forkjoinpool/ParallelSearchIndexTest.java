package concurrent.pools.forkjoinpool;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ParallelSearchIndexTest {
    private final Integer[] intArray = initIntegerArray();

    private static Integer[] initIntegerArray() {
        int n = 10_000_000;
        Integer[] array = new Integer[n];
        for (int i = 0; i != n; i++) {
            array[i] = i;
        }
        return array;
    }

    @Test
    public void whenElementExistsThanIndexLargerThanZero() {
        assertThat(ParallelSearchIndex.search(intArray, 999_999), is(999_999));
        assertThat(ParallelSearchIndex.search(intArray, 9_999_999), is(9_999_999));
    }

    @Test
    public void whenElementNotExistsThanIndexLessThanZero() {
        assertThat(ParallelSearchIndex.search(intArray, 10_000_001), is(-1));
        assertThat(ParallelSearchIndex.search(intArray, -10), is(-1));
    }
}