package labaFarm.services;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Predicate;

public abstract class InputService {
    private static final Scanner SCANNER = new Scanner(System.in);

    static {
        SCANNER.useLocale(Locale.US);
    }

    public static int readInt(String msg, String errorMsg, int min, int max) {
        System.out.print(msg);
        int inputNumber = min;
        boolean isValid = false;

        while (!isValid) {
            try {
                inputNumber = SCANNER.nextInt();
                Validate.inclusiveBetween(min, max, inputNumber);

                isValid = true;
            } catch (Exception e) {
                System.out.print(errorMsg);
                SCANNER.nextLine();
            }
        }

        SCANNER.nextLine(); //Cleans buffer
        return inputNumber;
    }

    public static float readFloat(String msg, String errorMsg, float min, float max) {
        System.out.print(msg);
        float inputNumber = min;
        boolean isValid = false;

        while (!isValid) {
            try {
                inputNumber = SCANNER.nextFloat();
                Validate.inclusiveBetween(min, max, inputNumber);

                isValid = true;
            } catch (Exception e) {
                System.out.print(errorMsg);
                SCANNER.nextLine();
            }
        }

        SCANNER.nextLine(); //Cleans buffer
        return inputNumber;
    }

    public static char readCharInValues(String msg, String errorMsg, char[] availableValues) {
        System.out.print(msg);
        char inputChar = Character.toUpperCase(SCANNER.next().charAt(0));
        while (!ArrayUtils.contains(availableValues, inputChar)) {
            System.out.print(errorMsg);
            inputChar = Character.toUpperCase(SCANNER.next().charAt(0));
        }

        return inputChar;
    }

    public static String readString(String msg, int minLength, int maxLength) {
        System.out.print(msg);
        String inputStr;

        do {
            try {
                inputStr = SCANNER.nextLine();
                Validate.notBlank(inputStr);
                Validate.inclusiveBetween(minLength, maxLength, inputStr.length(),
                        String.format("String must be between %d and %d characters long.", minLength, maxLength));
            } catch (Exception e) {
                LoggerService.consoleLog(Level.WARN, e.getMessage());
                inputStr = StringUtils.EMPTY;
                System.out.print("Try again: ");
            }
        } while (StringUtils.isEmpty(inputStr));

        return inputStr;
    }

    public static String readStringInValues(String msg, String errorMsg, String[] availableValues) {
        System.out.print(msg);
        String inputStr = SCANNER.next();
        while (!ArrayUtils.contains(availableValues, inputStr)) {
            System.out.print(errorMsg);
            inputStr = SCANNER.next();
        }

        return inputStr;
    }

    public static String readStringFollowsCondition(String msg, String errorMsg, Predicate<String> condition) {
        System.out.print(msg);
        String inputStr;

        do {
            try {
                inputStr = SCANNER.nextLine();
                if (!condition.test(inputStr)) {
                    throw new IllegalArgumentException(errorMsg);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                inputStr = StringUtils.EMPTY;
                System.out.print("Try again: ");
            }
        } while (StringUtils.isEmpty(inputStr));

        return inputStr;
    }

    public static LocalDate readValidDate() {
        int year, month, day;
        boolean isValid = false;
        LocalDate date = null;

        while (!isValid) {
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
                isValid = true;

            } catch (DateTimeException | IllegalArgumentException e) {
                System.out.println("Invalid date. Please try again.");
            }
        }

        return date;
    }
}
