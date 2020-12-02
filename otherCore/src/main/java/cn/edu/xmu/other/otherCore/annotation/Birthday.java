package cn.edu.xmu.other.otherCore.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author XQChen
 * @version 创建时间：2020/12/1 上午8:37
 */
@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthdayValidator.class)
public @interface Birthday {
    String pattern() default "yyyy-MM-dd";

    String message() default "生日格式错误";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
