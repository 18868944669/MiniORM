package cn.itcast.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行期间保留注解的信息
@Target(ElementType.FIELD)//作用在属性上
public @interface ORMColumn {

    public String name ()default "";
}
