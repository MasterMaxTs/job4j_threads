package concurrent.synch.list;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {

    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = copy(list);
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return list.iterator();
    }

    private List<T> copy(List<T> list) {
        DynamicList<T> clone = new DynamicList<>();
        for (T element : list) {
            clone.add(element);
        }
        return clone;
    }
}
