package concurrent;

public class VisualProgress {

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }

    private static class ConsoleProgress implements Runnable {

        @Override
        public void run() {
            String[] process = new String[]{"-", "\\", "|", "/"};
            try {
                int index = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.print("\rLoading... " + process[index++]);
                    Thread.sleep(500);
                    if (index == process.length) {
                        index = 0;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
