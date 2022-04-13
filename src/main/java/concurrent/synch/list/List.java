package concurrent.synch.list;

public interface List<T> extends Iterable<T> {

    void add(T model);

    T get(int index);
}
