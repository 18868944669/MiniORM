package cn.itcast.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行期间保留注解的信息
@Target(ElementType.TYPE)//作用在类上
public @interface ORMTable {
    public String name ()default "";
}
