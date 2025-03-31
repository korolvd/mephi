package ru.korolvd.threads.module3;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

/**
 * Сервис переводов между балансами, реализованный с помощью Executor Framework
 */
public class BalanceService implements AutoCloseable {

    private final int MAX_TIMEOUT = 10;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final AtomicIntegerArray balanceStore;

    public BalanceService(int... sums) {
        for (int sum : sums) {
            if (sum < 0) throw new IllegalArgumentException("Balance can't be negative");
        }
        this.balanceStore = new AtomicIntegerArray(sums);
    }

    public List<Integer> getAll() {
        return IntStream.range(0, balanceStore.length())
                .mapToObj(balanceStore::get)
                .toList();
    }

    /**
     * Выполнение транзакций
     */
    public void executeTransactions(List<Transaction> transactions) {
        for (Transaction trans : transactions) {
            executor.execute(() -> {
                while (true) {
                    int currentFrom = balanceStore.get(trans.fromId);
                    if (currentFrom < trans.amount) {
                        break;
                    }

                    int newFrom = currentFrom - trans.amount;
                    int currentTo = balanceStore.get(trans.toId);
                    int newTo = currentTo + trans.amount;

                    if (balanceStore.compareAndSet(trans.fromId, currentFrom, newFrom)) {
                        balanceStore.compareAndSet(trans.toId, currentTo, newTo);
                        break;
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(MAX_TIMEOUT, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    record Transaction(int fromId, int toId, int amount) {
        public Transaction {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (fromId == toId) {
                throw new IllegalArgumentException("Can't transfer to same account");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] balances = new int[n];
        for (int i = 0; i < n; i++) {
            balances[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        scanner.nextLine();
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            try {
                String[] s = scanner.nextLine().split(" - ");
                int fromId = Integer.parseInt(s[0]);
                int amount = Integer.parseInt(s[1]);
                int toId = Integer.parseInt(s[2]);
                transactions.add(new Transaction(fromId, toId, amount));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid input format.");
            }
        }

        try (BalanceService service = new BalanceService(balances)) {
            service.executeTransactions(transactions);
            List<Integer> rsl = service.getAll();
            for (int i = 0; i < n; i++) {
                System.out.printf("User %d final balance: %d%n", i, rsl.get(i));
            }
        }
    }
}
