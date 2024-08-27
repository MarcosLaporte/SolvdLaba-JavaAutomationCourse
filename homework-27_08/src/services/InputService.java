package services;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

public abstract class InputService {
    private static final Scanner SCANNER = new Scanner(System.in);
    static {
        SCANNER.useLocale(Locale.US);
    }

    public static int readInt(String msg, String errorMsg, int min, int max) {
        System.out.print(msg);
        int inputNumber = min;
        boolean isValid = false;

        do {
            try {
                inputNumber = SCANNER.nextInt();
                if (inputNumber < min || inputNumber > max)
                    throw new Exception("Value out of bounds.");

                isValid = true;
            } catch (Exception e) {
                System.out.print(errorMsg);
                SCANNER.nextLine();
            }
        } while (!isValid);

        return inputNumber;
    }

    public static float readFloat(String msg, String errorMsg, float min, float max) {
        System.out.print(msg);
        float inputNumber = min;
        boolean isValid = false;

        do {
            try {
                inputNumber = SCANNER.nextFloat();
                if (inputNumber < min || inputNumber > max)
                    throw new Exception("Value out of bounds.");

                isValid = true;
            } catch (Exception e) {
                System.out.print(errorMsg);
                SCANNER.nextLine();
            }
        } while (!isValid);

        return inputNumber;
    }

    public static char readCharInValues(String msg, String errorMsg, char[] availableValues) {
        System.out.print(msg);
        char inputChar = Character.toUpperCase(SCANNER.next().charAt(0));
        while (!contains(availableValues, inputChar)) {
            System.out.print(errorMsg);
            inputChar = Character.toUpperCase(SCANNER.next().charAt(0));
        }

        return inputChar;
    }

    public static String readString(String msg, int minLength, int maxLength) {
        System.out.print(msg);
        String inputStr = SCANNER.nextLine();
        while (inputStr.length() < minLength) {
            System.out.print("Minimum length is " + minLength + " characters.\nTry again: ");
            inputStr = SCANNER.nextLine();
        }
        if (inputStr.length() > maxLength)
            inputStr = inputStr.substring(0, maxLength);

        return inputStr;
    }

    public static String readStringInValues(String msg, String errorMsg, String[] availableValues) {
        System.out.print(msg);
        String inputStr = SCANNER.next();
        while (!contains(availableValues, inputStr)) {
            System.out.print(errorMsg);
            inputStr = SCANNER.next();
        }

        return inputStr;
    }

    public static boolean contains(char[] array, char target) {
        for (char c : array) {
            if (c == target)
                return true;
        }
        return false;
    }

    public static <T> boolean contains(T[] array, T target) {
        for (T element : array) {
            if (element.equals(target))
                return true;
        }
        return false;
    }

    public static LocalDate readValidDate() {
        int year, month, day;
        boolean validDate = false;
        LocalDate date = null;

        while (!validDate) {
            try {
                year = readInt(
                        "Enter year: ",
                        "Invalid year. Try again: ",
                        LocalDate.MIN.getYear(), LocalDate.MAX.getYear()
                );

                month = readInt(
                        "Enter month (1-12): ",
                        "Invalid month. Try again: ",
                        1, 12
                );

                day = readInt(
                        "Enter day: ",
                        "Invalid day. Try again: ",
                        1, 31
                );

                date = LocalDate.of(year, month, day);
                validDate = true;

            } catch (DateTimeException | IllegalArgumentException e) {
                System.out.println("Invalid date. Please try again.");
            }
        }

        return date;
    }
}
