package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

public class MainMenu {
    private enum Menu {
        EXIT, COUNT_UNIQUE_WORDS, COUNT_LETTERS, FIND_WORDS, WRITE_FILE;

        @Override
        public String toString() {
            return this.name().replace('_', ' ');
        }

        public static String MenuToString() {
            StringBuilder sb = new StringBuilder();
            for (Menu option : Menu.values()) {
                sb.append(String.format("%d. %s\n", option.ordinal(), option));
            }

            return sb.toString();
        }
    }

    public static void mainMenu() {
        int mainMenuOption;
        do {
            mainMenuOption = InputService.readInt(
                    "\n" + Menu.MenuToString() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            if (Menu.values()[mainMenuOption] == Menu.EXIT) {
                System.out.println("Exiting...");
                break;
            }

            switch (Menu.values()[mainMenuOption]) {
                case COUNT_UNIQUE_WORDS -> manageOption1();
                case COUNT_LETTERS -> manageOption2();
                case FIND_WORDS -> manageOption3();
                case WRITE_FILE -> manageOption4();
                default -> System.out.println("This option does not exist.");
            }

        } while (mainMenuOption != 0);
    }

    private static String getText() {
        boolean readFromFile = InputService.readInt(
                "Read values from file (1) or enter manually (2)? ",
                "This option does not exist. Try again: ",
                1, 2
        ) == 1;

        if (readFromFile) {
            String fileName = InputService.readString("Enter file name: ", 1, 255);
            return FileService.readFile(fileName);
        }

        return InputService.readString("Enter the text to process: ", 1, 255);
    }

    private static void manageOption1() {
        String text = getText();
        if (text == null) {
            LoggerService.log(Level.ERROR, "File doesn't exist");
            return;
        }

        String msg = "Amount of unique words in \"" +
                StringUtils.abbreviate(text, 64) +
                "\": " +
                StringService.countUniqueWordsInText(text) +
                "\n";

        LoggerService.consoleLog(Level.INFO, msg);
        FileService.writeFile("output.txt", msg + "\n");
    }

    private static void manageOption2() {
        String text = getText();
        if (text == null) {
            LoggerService.log(Level.ERROR, "File doesn't exist");
            return;
        }

        String msg = "Amount of letters in \"" +
                StringUtils.abbreviate(text, 64) +
                "\": " +
                StringService.countLettersInText(text) +
                "\n";

        LoggerService.consoleLog(Level.INFO, msg);
        FileService.writeFile("output.txt", msg + "\n");
    }

    private static void manageOption3() {
        String text = getText();
        if (text == null) {
            LoggerService.log(Level.ERROR, "File doesn't exist");
            return;
        }

        String word = InputService.readString("Enter the word to look in the text: ", 2, 255);

        String msg = String.format("Amount of \"%s\" found in \"%s\": %d\n",
                word,
                StringUtils.abbreviate(text, 64),
                StringService.countGivenWordInText(text, word)
        );

        LoggerService.consoleLog(Level.INFO, msg);
        FileService.writeFile("output.txt", msg + "\n");
    }

    private static void manageOption4() {
        String fileContents = InputService.readString("Enter what you want to save to a file: ", 1, 255);
        String filePath = InputService.readStringFollowsCondition("Enter file path: ", "File path invalid.", FileService::isValidFileName);

        LoggerService.consoleLog(Level.INFO, "Content written to file successfully.");
        FileService.writeFile(filePath, fileContents);
    }

}
