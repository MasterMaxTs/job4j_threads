package concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {

    private final AtomicReference<Integer> count =
            new AtomicReference<>(0);

    public void increment() {
        Integer i;
        do {
            i = count.get();
        } while (!count.compareAndSet(i, i + 1));
    }

    public int get() {
        return count.get();
    }
}
