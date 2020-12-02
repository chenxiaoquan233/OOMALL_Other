package cn.edu.xmu.other.otherCore.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author XQChen
 * @version 创建时间：2020/12/1 上午11:11
 */
public class BirthdayValidator implements ConstraintValidator<Birthday, String> {

    private static final Logger logger = LoggerFactory.getLogger(BirthdayValidator.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        logger.debug(s);

        try{
            LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
