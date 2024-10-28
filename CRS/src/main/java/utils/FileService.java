package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FileService {
    public static void writeFile(String filePath, String content, boolean append) {
        File file = new File(preparePath(filePath));
        try {
            FileUtils.write(file, content, Charset.defaultCharset(), append);
            LoggerService.log(Level.INFO, "Written to '" + filePath + "' successfully.");
        } catch (IOException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }
    }

    public static void writeFile(String filePath, String content) {
        writeFile(filePath, content, true);
    }

    public static String readFile(String filePath) {
        File file = new File(preparePath(filePath));
        try {
            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return null;
    }

    private static String preparePath(String path) {
        path = path.trim();

        if (!path.startsWith(File.separator))
            path = File.separator + path;

        path = path.replace("\\", File.separator).replace("/", File.separator);

        return System.getProperty("user.dir") + path;
    }

    public static String[] getFileNames(String path, Set<String> extensions) {
        File folder = new File(path);

        if (!folder.exists() || !folder.isDirectory()) {
            return new String[0];
        }

        File[] fileList = folder.listFiles();
        if (fileList == null || fileList.length == 0) {
            return new String[0];
        }

        Predicate<File> hasValidExtension = file -> extensions.isEmpty() ||
                extensions.stream().anyMatch(ext -> file.getName().toLowerCase().endsWith(ext.toLowerCase()));

        return Arrays.stream(fileList)
                .filter(hasValidExtension)
                .map(File::getName)
                .toArray(String[]::new);
    }

    public static String[] getFileNames(String path, String... extensions) {
        return getFileNames(path, new HashSet<>(Arrays.asList(extensions)));
    }

    public static final Function<String, String> stripExtension = fileName -> fileName.split("\\.(?=[^\\.]+$)")[0];
    private static final Pattern VALID_FILE_NAME_PATTERN = Pattern.compile("^[\\w\\-. ]+$");

    public static boolean isValidFileName(String fileName) {
        return !StringUtils.isBlank(fileName) &&
                VALID_FILE_NAME_PATTERN.matcher(fileName).matches() &&
                fileName.trim().length() == fileName.length() &&
                fileName.length() <= 255;
    }
}
