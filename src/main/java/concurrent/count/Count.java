package concurrent.count;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Count {

    @GuardedBy("this")
    private int value;

    public synchronized void increment() {
        value++;
    }

    public synchronized int get() {
        return value;
    }
}
