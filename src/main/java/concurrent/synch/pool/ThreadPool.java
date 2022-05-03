package concurrent.synch.pool;

import concurrent.synch.queue.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;
    private static boolean isWork = true;

    public ThreadPool(int countThreads, int queueSize) {
        tasks = new SimpleBlockingQueue<>(queueSize);
        initThreads(countThreads);
    }

    private void initThreads(int countThreads) {
        for (int i = 0; i < countThreads; i++) {
            threads.add(new Thread(
                    new TaskWorker())
            );
        }
    }

    public void startThreads() {
        for (Thread t
                : threads) {
            t.start();
        }
    }

    public void shutdown() {
        for (Thread t
                : threads) {
            t.interrupt();
        }
        isWork = false;
    }

    public void work(Runnable job) throws InterruptedException {
        if (isWork) {
            tasks.offer(job);
        }
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable nextTask = tasks.poll();
                    if (nextTask != null) {
                        nextTask.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
