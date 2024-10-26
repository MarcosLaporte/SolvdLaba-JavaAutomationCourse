package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entities.lists.*;
import org.apache.logging.log4j.Level;
import services.database.IDao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonService<T> implements IDao<T> {
    private static final String baseDir = System.getProperty("user.dir") + "\\files\\json\\";

    public final Class<T> clazz;

    public JsonService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static void parse() {
        String[] jsonFiles = FileService.getFileNames(baseDir, "json");

        if (jsonFiles.length == 0) {
            LoggerService.println("No files to read.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        for (String json : jsonFiles) {
            try {
                LoggerService.println(json);
                String fileNoExt = FileService.stripExtension.apply(json);
                Class<?> clazz = switch (fileNoExt) {
                    case "CustomerList" -> CustomerList.class;
                    case "FeedbackList" -> FeedbackList.class;
                    case "InvoiceList" -> InvoiceList.class;
                    case "JobList" -> JobList.class;
                    case "JobTechnicianList" -> JobTechnicianList.class;
                    case "PartList" -> PartList.class;
                    case "PaymentList" -> PaymentList.class;
                    case "RepairTicketList" -> RepairTicketList.class;
                    case "RepairTicketPartList" -> RepairTicketPartList.class;
                    case "SupplierList" -> SupplierList.class;
                    case "TechnicianList" -> TechnicianList.class;
//                    case "TicketStatus" -> {}
                    default -> throw new IllegalStateException("Unexpected value: " + fileNoExt);
                };

                Object readValue = objectMapper.readValue(new File(baseDir + json), clazz);
                LoggerService.println(ReflectionService.toString(readValue));
                LoggerService.println("----------------------------------");
                LoggerService.println("----------------------------------");
            } catch (IOException | IllegalStateException e) {
                LoggerService.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public static <T> void serializeList(T list) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        File newFile = new File(baseDir + list.getClass().getSimpleName() + ".json");

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(newFile, list);
    }

    @Override
    public List<T> get(Map<String, Object> columnCondition) {
        return List.of();
    }

    @Override
    public int create(T t) {
        return 0;
    }

    @Override
    public int update(Map<String, Object> newValues, Map<String, Object> columnCondition) {
        return 0;
    }

    @Override
    public int delete(Map<String, Object> columnCondition) {
        return 0;
    }
}
