package cn.edu.xmu.other.otherCore.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

/**
 * @author XQChen
 * @version 创建时间：2020/12/2 上午8:45
 */
public class DateBeforeValidator implements ConstraintValidator<DateBefore, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if(localDate == null) return true;
        return !localDate.isAfter(ChronoLocalDate.from(LocalDate.now()));
    }
}
