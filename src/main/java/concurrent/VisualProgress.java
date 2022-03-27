package concurrent;

public class VisualProgress {

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1000);
        progress.interrupt();
    }

    private static class ConsoleProgress implements Runnable {

        boolean isStop = false;

        @Override
        public void run() {
            String[] process = new String[]{"-", "\\", "|", "/"};
            try {
                while (!isStop) {
                    for (int i = 0; i < 4; i++) {
                        System.out.print("\rLoading... " + process[i]);
                    }
                    Thread.sleep(500);
                    isStop = Thread.currentThread().isInterrupted();
                }
            } catch (InterruptedException e) {
                System.out.println("\nThread has been interrupted!");
                e.printStackTrace();
                isStop = true;
            }
        }
    }
}
