package concurrent.async;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RowColSumTest {
    private final int[][] matrix = new int[][] {
                                         {1, 2, 3},
                                         {4, 5, 6},
                                         {7, 8, 9},
    };

    @Test
    public void whenGetRowSum() {
        assertThat(RowColSum.getRowSum(1, matrix), is(6));
        assertThat(RowColSum.getRowSum(2, matrix), is(15));
        assertThat(RowColSum.getRowSum(3, matrix), is(24));
    }

    @Test
    public void whenGetColSum() {
        assertThat(RowColSum.getColSum(1, matrix), is(12));
        assertThat(RowColSum.getColSum(2, matrix), is(15));
        assertThat(RowColSum.getColSum(3, matrix), is(18));
    }

    @Test
    public void getGetAsyncRowSum()
                                throws ExecutionException, InterruptedException {
        assertThat(RowColSum.getAsyncRowSum(1, matrix), is(6));
        assertThat(RowColSum.getAsyncRowSum(2, matrix), is(15));
        assertThat(RowColSum.getAsyncRowSum(3, matrix), is(24));
    }

    @Test
    public void getGetAsyncColSum()
                                throws ExecutionException, InterruptedException {
        assertThat(RowColSum.getAsyncColSum(1, matrix), is(12));
        assertThat(RowColSum.getAsyncColSum(2, matrix), is(15));
        assertThat(RowColSum.getAsyncColSum(3, matrix), is(18));
    }
}