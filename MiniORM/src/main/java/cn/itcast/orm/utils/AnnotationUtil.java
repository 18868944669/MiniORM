package cn.itcast.orm.utils;

import cn.itcast.orm.annotation.ORMColumn;
import cn.itcast.orm.annotation.ORMId;
import cn.itcast.orm.annotation.ORMTable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 使用反射解析实体类中注解的工具类
 */
public class AnnotationUtil {
    /**
     * 获取指定的类名
     *
     * @param clz
     * @return
     */
    public static String getClassName(Class clz) {
        return clz.getName();
    }

    /**
     * 获取ORMTable注解中的表名
     *
     * @param clz
     * @return
     */
    public static String getTableName(Class clz) {
        if (clz.isAnnotationPresent(ORMTable.class)) {
            ORMTable ormTable = (ORMTable) clz.getAnnotation(ORMTable.class);
            return ormTable.name();
        } else {
            System.out.println("缺少ORMTable的注解");
            return null;
        }
    }

    /**
     * 得到主键属性和对应的值
     *
     * @param clz
     * @return
     */
    public static Map<String, String> getIdMapper(Class clz) {
        boolean flag = true;
        Map<String, String> map = new HashMap<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ORMId.class)) {
                flag = false;
                String fileName = field.getName();
                if (field.isAnnotationPresent(ORMColumn.class)) {
                    ORMColumn ormColumn = (ORMColumn) field.getAnnotation(ORMColumn.class);
                    String columName = ormColumn.name();
                    map.put(fileName, columName);
                    break;
                } else {
                    System.out.println("缺少ORMColum的注解");
                }
            }
        }
        if (flag) {
            System.out.println("缺少ORMId的注解");
        }
        return map;
    }

    /**
     * 得到类中所有属性和对应的字段
     *
     * @param clz
     * @return
     */
    public static Map<String, String> getPropMapping(Class clz) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getIdMapper(clz));
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ORMColumn.class)) {
                ORMColumn ormColumn = (ORMColumn) field.getAnnotation(ORMColumn.class);
                String fileName = field.getName();
                String columnName = ormColumn.name();
                map.put(fileName, columnName);
            }
        }
        return map;
    }

    /**
     * 获得某包下面的所有类名
     *
     * @param packagePath
     * @return
     */
    public static Set<String> getClassNameByPackage(String packagePath) {

        Set<String> names = new HashSet<>();
        String packageFile = packagePath.replace(".", "/");
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (classPath == null) {
            classPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        }

        try {
            classPath = java.net.URLDecoder.decode(classPath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File dir = new File(classPath + packageFile);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String name = file.getName();
                if (file.isFile() && name.endsWith(".class")) {
                    name = packagePath + "." + name.substring(0, name.lastIndexOf("."));
                    names.add(name);
                }
            }
        } else {
            System.out.println("包路径不存在");
        }
        return names;
    }


}
