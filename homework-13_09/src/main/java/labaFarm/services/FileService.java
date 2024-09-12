package labaFarm.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class FileService {
    public static void writeFile(String filePath, String content, boolean append) {
        File file = new File(preparePath(filePath));
        try {
            FileUtils.write(file, content, Charset.defaultCharset(), append);
            LoggerService.fileLog(Level.INFO, "Content written to file successfully.");
        } catch (IOException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }
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
        String separator = File.separator;

        if (!path.startsWith(separator))
            path = separator + path;

        path = path.replace("\\", separator).replace("/", separator);

        return System.getProperty("user.dir") + path;
    }

    private static final Pattern VALID_FILE_NAME_PATTERN = Pattern.compile("^[\\w\\-. ]+$");
    public static boolean isValidFileName(String fileName) {
        return !StringUtils.isBlank(fileName) &&
                VALID_FILE_NAME_PATTERN.matcher(fileName).matches() &&
                fileName.trim().length() == fileName.length() &&
                fileName.length() <= 255;
    }
}
