package concurrent.synch;

public interface List<T> extends Iterable<T> {

    void add(T model);

    T get(int index);
}
