package cn.edu.xmu.other.otherCore.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author XQChen
 * @version 创建时间：2020/12/2 上午8:44
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateBeforeValidator.class)
public @interface DateBefore {
    String message() default "时间范围错误";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
