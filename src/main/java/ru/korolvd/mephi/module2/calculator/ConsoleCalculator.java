package ru.korolvd.mephi.module2.calculator;

import java.util.List;
import java.util.Scanner;

/**
 * 2.6 Итоговый проект «Калькулятор»
 * Доступные операции: сложение, вычитание, умножение и деление
 * Дополнительные команды: C - сбросить результат и начать вычисления сначала, s - завершение работы
 */
public class ConsoleCalculator {
    /**
     * Коды чисел 0-9 и дробного разделителя, точки (.)
     **/
    private final static List<Integer> DOUBLE_SYMBOL_CODES = List.of(46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57);

    /**
     * Доступные операции вычислений
     **/
    private final static List<String> OPERATION_SYMBOLS = List.of("+", "-", "/", "*");

    /**
     * Команда сброса вычислений
     **/
    private final static String COMMAND_CLEAR = "C";

    /**
     * Команда завершения работы
     **/
    private final static String COMMAND_STOP = "s";

    private final Scanner scanner = new Scanner(System.in);

    private boolean isNumberInput = true;

    private String operation = "+";

    private double result = 0;

    public static void main(String[] args) {
        new ConsoleCalculator().run();
    }

    public void run() {
        boolean isRun = true;
        while (isRun) {
            System.out.print(isNumberInput ? "Input number: " : "Input operation: ");
            String input = scanner.nextLine();
            switch (input) {
                case COMMAND_STOP -> {
                    isRun = false;
                    continue;
                }
                case COMMAND_CLEAR -> {
                    result = 0;
                    isNumberInput = true;
                    continue;
                }
                default -> {
                    if (!validateInput(input)) {
                        System.out.println("Error! Incorrect input");
                        continue;
                    }
                    if (isNumberInput) {
                        result = calculate(result, operation, Double.parseDouble(input));
                        System.out.println("Result: " + result);
                    } else {
                        operation = input;
                    }
                }
            }
            isNumberInput = !isNumberInput;
        }
    }

    private double calculate(double first, String operation, double second) {
        return switch (operation) {
            case "+" -> first + second;
            case "-" -> first - second;
            case "*" -> first * second;
            case "/" -> first / second;
            default -> 0;
        };
    }

    private boolean validateInput(String input) {
        if (!isNumberInput) {
            return OPERATION_SYMBOLS.contains(input);
        }
        for (int i = 0; i < input.length(); i++) {
            if (!DOUBLE_SYMBOL_CODES.contains((int) input.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
