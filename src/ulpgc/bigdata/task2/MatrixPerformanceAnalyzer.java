package ulpgc.bigdata.task2;


public class MatrixPerformanceAnalyzer {
    public static void main(String[] args) {
        int initialSize = 256;
        int maxSize = 8192;
        int stepSize = 256;

        for (int size = initialSize; size <= maxSize; size += stepSize) {
            double[][] matrixA = MatrixUtils.generateRandomMatrix(size, size);
            double[][] matrixB = MatrixUtils.generateRandomMatrix(size, size);

            System.out.println("\n----- Matrix Size: " + size + "x" + size + " -----");

            // Measure performance of optimized standard multiplication
            double[][] finalMatrixA = matrixA;
            double[][] finalMatrixB = matrixB;
            measurePerformance(() -> MatrixUtils.standardMultiply(finalMatrixA, finalMatrixB), "Optimized Standard Multiplication");

            // Measuring performance of optimized Strassen multiplication
            double[][] finalMatrixA1 = matrixA;
            double[][] finalMatrixB1 = matrixB;
            measurePerformance(() -> StrassenMatrixMultiplication.multiply(finalMatrixA1, finalMatrixB1), "Optimized Strassen Multiplication");

            // Measuring performance of optimized sparse matrix multiplication
            matrixA = MatrixUtils.generateSparseMatrix(size, size, 0.8);
            matrixB = MatrixUtils.generateSparseMatrix(size, size, 0.8);
            double[][] finalMatrixA2 = matrixA;
            double[][] finalMatrixB2 = matrixB;
            measurePerformance(() -> SparseMatrixMultiplication.multiply(finalMatrixA2, finalMatrixB2), "Optimized Sparse Multiplication");
        }
    }

    public static void measurePerformance(Runnable method, String methodName) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        method.run();

        long endTime = System.nanoTime();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println(methodName + "\n - Tiempo de ejecución: " + (endTime - startTime) / 1e9 + " s\n - Memoria: " + (endMemory - startMemory) / (1024*1024) + " MB");

    }
}