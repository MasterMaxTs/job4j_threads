package concurrent.pools.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearchIndex<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final T element;
    private final int from;
    private final int to;

    public ParallelSearchIndex(T[] array, T element, int from, int to) {
        this.array = array;
        this.element = element;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return linearSearch();
        }
        int mid = (from + to) / 2;
        ParallelSearchIndex<T> left
                = new ParallelSearchIndex<>(array, element, from, mid);
        ParallelSearchIndex<T> right
                = new ParallelSearchIndex<>(array, element, mid + 1, to);
        left.fork();
        right.fork();
        return Math.max(left.join(), right.join());
    }

    private Integer linearSearch() {
        int result = -1;
        for (int i = from; i < to; i++) {
            if (element.equals(array[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static <T> Integer search(T[] array, T value) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(
                new ParallelSearchIndex<>(array, value, 0, array.length)
        );
    }
}
