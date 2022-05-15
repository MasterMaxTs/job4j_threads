package concurrent.async;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RowColSum {

    public static class Sums {

        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }
    }

    private static Sums[] sum(int[][] matrix) {
        int rowSum = 0;
        int colSum = 0;
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            sums[i] = new Sums(rowSum, colSum);
            rowSum = 0;
            colSum = 0;
        }
        return sums;
    }

    public static int getRowSum(int numberRow, int[][] matrix) {
        Objects.checkIndex(numberRow - 1, matrix.length);
        Sums[] sums = sum(matrix);
        return sums[numberRow - 1].rowSum;
    }

    public static int getColSum(int numberCol, int[][] matrix) {
        Objects.checkIndex(numberCol - 1, matrix.length);
        Sums[] sums = sum(matrix);
        return sums[numberCol - 1].colSum;
    }

    private static Sums[] asyncSum(int[][] matrix)
                                throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        int k = 0;
        List<CompletableFuture<Integer>> rowSumFutures = new ArrayList<>();
        List<CompletableFuture<Integer>> colSumFutures = new ArrayList<>();
        while (k < n) {
            rowSumFutures.add(getSumRowTask(matrix, k, n - 1));
            colSumFutures.add(getSumColTask(matrix, k, n - 1));
            k++;
        }
        for (int i = 0; i < n; i++) {
            sums[i] = new Sums(
                    rowSumFutures.get(i).get(), colSumFutures.get(i).get()
            );
        }
        return sums;
    }

   private static CompletableFuture<Integer> getSumRowTask(int[][] matrix,
                                                           int startRow,
                                                           int endCol) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int sum = 0;
                    for (int i = 0; i <= endCol; i++) {
                        sum += matrix[startRow][i];
                    }
                    return sum;
                }
        );
    }

    private static CompletableFuture<Integer> getSumColTask(int[][] matrix,
                                                            int startCol,
                                                            int endRow) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int sum = 0;
                    for (int i = 0; i <= endRow; i++) {
                        sum += matrix[i][startCol];
                    }
                    return sum;
                }
        );
    }

    public static int getAsyncRowSum(int numberRow, int[][] matrix)
                                throws ExecutionException, InterruptedException {
        Objects.checkIndex(numberRow - 1, matrix.length);
        Sums[] sums = asyncSum(matrix);
        return sums[numberRow - 1].rowSum;
    }

    public static int getAsyncColSum(int numberCol, int[][] matrix)
                                throws ExecutionException, InterruptedException {
        Objects.checkIndex(numberCol - 1, matrix.length);
        Sums[] sums = asyncSum(matrix);
        return sums[numberCol - 1].colSum;
    }
}
