package cn.itcast.orm.test;

import cn.itcast.orm.core.ORMConfig;
import cn.itcast.orm.core.ORMSession;

import cn.itcast.orm.entity.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MiniORMTest {

    private ORMConfig ormConfig;
    private ORMSession ormSession;

    @Before
    public void init() throws Exception {
        ormConfig = new ORMConfig();
        ormSession = ormConfig.buildORMSession();
    }

    @After
    public void destory() throws Exception{
        ormSession.close();
    }

    /**
     * 测试保存方法
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
        Account account = new Account();
        account.setId(5);
        account.setName("aaa");
        account.setMoney(1111.00d);
        ormSession.save(account);

    }

    /**
     * 测试根据id查询一个Account
     * @throws Exception
     */
    @Test
    public void testfindOne() throws Exception {
        Account account = (Account) ormSession.findOne(Account.class, 1);
        System.out.println(account);
    }

    /**
     * 测试删除方法
     * @throws Exception
     */
    @Test
    public void testDel() throws Exception {
        Account account = new Account();
        account.setId(5);
        ormSession.delete(account);

    }
}
