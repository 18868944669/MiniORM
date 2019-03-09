package cn.itcast.orm.entity;


import cn.itcast.orm.annotation.ORMColumn;
import cn.itcast.orm.annotation.ORMId;
import cn.itcast.orm.annotation.ORMTable;

import java.io.Serializable;


@ORMTable(name = "account")
public class Account implements Serializable {
    @ORMId
    @ORMColumn(name = "id")
    private Integer id;
    @ORMColumn(name = "name")
    private String name;
    @ORMColumn(name = "money")
    private Double money;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
