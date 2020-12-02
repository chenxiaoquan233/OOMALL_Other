package cn.edu.xmu.other.otherCore.annotation;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author XQChen
 * @version 创建时间：2020/12/1 上午8:51
 */
public class BirthdayAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 7368707128625539909L;

    @Override
    public Object findDeserializer(Annotated annotated) {
        Birthday formatter = annotated.getAnnotation(Birthday.class);
        if (formatter != null) {
            return new BirthdayDeserializer(formatter.pattern());
        }
        return super.findDeserializer(annotated);
    }
}
