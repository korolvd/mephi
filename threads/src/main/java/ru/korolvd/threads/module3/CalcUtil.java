package ru.korolvd.threads.module3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Асинхронное вычисление формулы с помощью CompletableFuture
 * и его функционала по комбинированию и асинхронному выполнению задач
 */
public class CalcUtil {

    /**
     * Вычисление формулы (a^2 + b^2) * log(c) / sqrt(d),
     * где с > 0, d > 0
     */
    public static double asyncCalc(double a, double b, double c, double d) {
        if (c <= 0 || d <= 0) {
            throw new IllegalArgumentException("c (%f) or d (%f) must be > 0".formatted(c, d));
        }

        CompletableFuture<Double> sumSqrt = calcBy("sum of squares", (l, r) -> Math.pow(l, 2) + Math.pow(r, 2), 5, a, b);
        CompletableFuture<Double> log = calcBy("log(c)", (l, r) -> Math.log(l), 15, c);
        CompletableFuture<Double> sqrt = calcBy("sqrt(d)", (l, r) -> Math.sqrt(l), 10, d);

        try {
            return CompletableFuture.allOf(sumSqrt, log, sqrt)
                    .thenApplyAsync(v -> {
                        try {
                            return (sumSqrt.get() * log.get()) / sqrt.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Асинхронное вычисление одной функции
     * @param logName - название функции для логирования
     * @param func - функция
     * @param timout - время задержки вычисления
     * @param d - одно или два значения для вычисления
     */
    private static CompletableFuture<Double> calcBy(String logName, BiFunction<Double, Double, Double> func, long timout, double... d) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(timout);
                double result = func.apply(d[0], d.length > 1 ? d[1] : 0);
                System.out.println("Calculating " + logName + ": " + result);
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("Final result of the formula: " + asyncCalc(3, 4, 10, 16));
    }
}
