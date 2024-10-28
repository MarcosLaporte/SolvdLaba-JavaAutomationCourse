package services.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entities.Entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonService<T extends Entity> extends FileDao<T> {
    private static final String baseDir = System.getProperty("user.dir") + "\\files\\json\\";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    public JsonService(Class<T> clazz) {
        super(clazz, baseDir, ".json");
    }

    @Override
    protected List<T> parse(String fileName) throws IOException {
        File file = new File(baseDir + fileName);
        if (!file.exists())
            return new ArrayList<>();

        return MAPPER.readValue(file,
                MAPPER.getTypeFactory().constructCollectionType(List.class, this.clazz)
        );
    }

    @Override
    protected void serializeList(List<T> list) throws IOException {
        File file = new File(baseDir + this.clazz.getSimpleName() + ".json");

        MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(file, list);
    }

}
