package cn.itcast.orm.core;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类用来封装和存储映射信息
 */
@Data
@ToString
public class Mapper {
    //类名
    private String className;
    //表名
    private String tableName;
    //主键信息
    private Map<String, String> idMapper = new HashMap<>();
    //普通的属性和字段信息
    private Map<String, String> propMapper = new HashMap<>();


}
