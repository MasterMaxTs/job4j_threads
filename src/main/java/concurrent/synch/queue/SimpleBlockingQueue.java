package concurrent.synch.queue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("monitor")
    private final Queue<T> queue;
    private final Object monitor = this;
    private final int capacity;

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    public void offer(T value) {
        synchronized (monitor) {
            while (queue.size() == capacity) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            monitor.notify();
        }
    }

    public T poll() {
        synchronized (monitor) {
            while (queue.peek() == null) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            monitor.notify();
            return queue.poll();
        }
    }

    public int size() {
        synchronized (monitor) {
            return queue.size();
        }
    }
}
