package concurrent;

public class Wget {

    public static void main(String[] args) {
        printLoading();
    }

    private static void printLoading() {
        new Thread(
                () -> {
                    for (int i = 0; i <= 100; i++) {
                        System.out.print("\rLoading " + i + "%");
                        if (i == 100) {
                            System.out.print(" - Finished!");
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }
}
