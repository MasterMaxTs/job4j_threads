package concurrent.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {

    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = (List<T>) copy(list);
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

    private Object copy(Object list) {
        Object rsl = null;
        try {
            Constructor constructor = list.getClass().getConstructor();
            Object clone = constructor.newInstance();
            Field[] fields = list.getClass().getDeclaredFields();
            for (Field field
                    : fields) {
                field.setAccessible(true);
                field.set(clone, field.get(list));
            }
            rsl = clone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
