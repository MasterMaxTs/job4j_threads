package concurrent.synch.pool;

import concurrent.synch.queue.SimpleBlockingQueue;

public class ThreadPool {

    private volatile boolean isRunning = true;
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(4);
    private static final int SIZE = Runtime.getRuntime().availableProcessors();

    public ThreadPool() {
        for (int i = 0; i < SIZE; i++) {
            new Thread(
                    new TaskWorker()
            ).start();
        }
    }

    public void work(Runnable job) {
        try {
            if (isRunning) {
                tasks.offer(job);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Runnable nextTask = tasks.poll();
                    if (nextTask != null) {
                        nextTask.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
