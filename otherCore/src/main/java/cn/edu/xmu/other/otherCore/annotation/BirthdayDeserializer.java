package cn.edu.xmu.other.otherCore.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author XQChen
 * @version 创建时间：2020/12/1 上午8:44
 */
public class BirthdayDeserializer extends JsonDeserializer<LocalDate> {
    private final String pattern;

    private static final Logger logger = LoggerFactory.getLogger(BirthdayDeserializer.class);

    public BirthdayDeserializer() {
        super();
        this.pattern = "yyyy-MM-dd";
    }

    public BirthdayDeserializer(String pattern) {
        super();
        this.pattern = pattern;
    }


    public LocalDate judge(@Birthday String text) {
        if(text.isBlank()) return null;

        logger.debug(text);

        try {
            LocalDate localDate =  LocalDate.parse(text);
            if(localDate.isAfter(ChronoLocalDate.from(LocalDate.now()))) {
                return localDate;
            }
        } catch (DateTimeParseException e) {
            //格式错误
        }
        return null;
    }


    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        @Birthday
        String text = jsonParser.getText().trim();



        return judge(text);
    }
}