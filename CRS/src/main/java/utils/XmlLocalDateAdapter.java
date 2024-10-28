package utils;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class XmlLocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String dateString) {
        if (dateString == null || dateString.isEmpty())
            return null;

        return stringToLocalDate(dateString);
    }

    @Override
    public String marshal(LocalDate date) {
        if (date == null)
            return null;

        return date.toString();
    }

    public static LocalDate stringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.getDefault());
        return LocalDate.parse(dateStr, formatter);
    }
}