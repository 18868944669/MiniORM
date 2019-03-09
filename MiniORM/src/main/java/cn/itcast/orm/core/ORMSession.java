package cn.itcast.orm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ORMSession {
    private Connection connection;

    public ORMSession(Connection connection) {
        this.connection = connection;
    }

    //保存数据
    public void save(Object entity) throws Exception {
        //从ORMConfig中获取保存有映射信息的集合
        List<Mapper> mapperList = ORMConfig.mapperList;
        String insertSQL = "";
        //遍历集合,从集合中找到和entity参数相对应的mapper对象
        for (Mapper mapper : mapperList) {
            if (mapper.getClassName().equals(entity.getClass().getName())) {
                String tableName = mapper.getTableName();
                String insertSQL1 = "insert into " + tableName + "( ";
                String insertSQL2 = " )values( ";
                //得到当前对象所属类中的所有属性
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    // 遍历过程中获取属性对象的字段名
                    String columnName = mapper.getPropMapper().get(field.getName());
                    //根据属性获取所有对应的值
                    String columnValue = field.get(entity).toString();
                    //拼接sql语句
                    insertSQL1 += columnName + ",";
                    insertSQL2 += "'" + columnValue + "',";
                }
                insertSQL = insertSQL1.substring(0, insertSQL1.length() - 1) + insertSQL2.substring(0, insertSQL2.length() - 1) + ")";
                break;
            }
        }
        System.out.println("MiniORM-save:" + insertSQL);
        //通过jdbc发送并执行sql
        PreparedStatement pstmt = connection.prepareStatement(insertSQL);
        pstmt.executeUpdate();
        pstmt.close();
    }

    //根据主键进行数据删除
    public void delete(Object entity) throws Exception {
        String delSQL = "delete from ";
        //从ORMConfig中获取保存有映射信息的集合
        List<Mapper> mapperList = ORMConfig.mapperList;
        for (Mapper mapper : mapperList) {
            if (mapper.getClassName().equals(entity.getClass().getName())) {
                //获取对应的表名
                String tableName = mapper.getTableName();
                delSQL += tableName + " where ";
                //得到主键的字段名和属性名
                Object[] idProp = mapper.getIdMapper().keySet().toArray();
                Object[] idColumn = mapper.getIdMapper().values().toArray();

                //得到主键的值
                Field field = entity.getClass().getDeclaredField(idProp[0].toString());
                field.setAccessible(true);
                String idValue = field.get(entity).toString();
                delSQL += idColumn[0].toString() + " = " + idValue;
                break;
            }

        }
        System.out.println("MiniORM-del:" + delSQL);
        //通过jdbc发送并执行sql
        PreparedStatement pstmt = connection.prepareStatement(delSQL);
        pstmt.executeUpdate();
        pstmt.close();
    }

    //根据主键进行查询
    public Object findOne(Class clz, Object id) throws Exception {

        String findOneSQL = "select * from ";
        //从ORMConfig中获取保存有映射信息的集合
        List<Mapper> mapperList = ORMConfig.mapperList;
        for (Mapper mapper : mapperList) {
            if (mapper.getClassName().equals(clz.getName())) {
                //获取对应的表名
                String tableName = mapper.getTableName();
                //得到主键的字段名
                Object[] idColumn = mapper.getIdMapper().values().toArray();

                findOneSQL += tableName + " where " + idColumn[0].toString() + " = " + id;
                break;
            }

        }
        System.out.println("MiniORM-findOne:" + findOneSQL);
        //通过jdbc发送并执行sql,获取结果集
        PreparedStatement pstmt = connection.prepareStatement(findOneSQL);
        ResultSet resultSet = pstmt.executeQuery();
        //封装结果集
        if (resultSet.next()) {
            //查询到一行数据中
            //创建一个对象,用于封装数据
            Object obj = clz.newInstance();
            //遍历mapper集合,找到想要的mapper对象
            for (Mapper mapper : mapperList) {
                if (mapper.getClassName().equals(clz.getName())) {
                    //得到存有属性-字段的映射信息
                    Map<String, String> propMap = mapper.getPropMapper();
                    Set<String> keySet = propMap.keySet();
                    // 遍历集合获取属性名和对应的字段名
                    for (String prop : keySet) { //prop就是属性名
                        String column = propMap.get(prop); //column就是和属性对应的字段名
                        Field field = clz.getDeclaredField(prop);
                        field.setAccessible(true);
                        Object object = resultSet.getObject(column);
                        field.set(obj, object);
                    }
                    break;
                }
            }
            resultSet.close();
            pstmt.close();
            return obj;
        } else {
            return null;
        }

    }

    //关闭连接,释放资源
    public  void close() throws Exception {
        if (connection!=null){
            connection.close();
            connection=null;
        }
    }
}
