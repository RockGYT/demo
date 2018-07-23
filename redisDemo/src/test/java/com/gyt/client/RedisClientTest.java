/**
 * FileName: redisClientTest
 * Author:   Rock_Guo
 * Date:     2018/7/4 14:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.gyt.client;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Rock_Guo
 * @create 2018/7/4
 * @since 1.0.0
 */
public class RedisClientTest {

    @Test
    public void redisTest() throws  Exception{

        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("连接成功！");
        jedis.auth("Rock1987guo");
        System.out.println("服务正在运行："+jedis.ping());

        // 切换到指定数据库 （默认的是 0 ）
        jedis.select(0);

        // 所有key
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            jedis.del(key);
        }

        // 检查是否存在key
        System.out.println("是否存在key[address] = " +jedis.exists("address"));
        // 为给定的key设置过期时间（单位：秒）
        jedis.expire("address", 300);
        //为给定的key设置过期时间（参数为unix时间戳 （秒））
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jedis.expireAt("address", sdf.parse("2019-12-31 00:00:00").getTime()/1000);
        // 为给定的key设置过期时间（单位：毫秒）
        jedis.pexpire("address", 300000L);
        //为给定的key设置过期时间（参数为unix时间戳）
        jedis.pexpireAt("address", sdf.parse("2019-12-31 00:00:00").getTime());
        //以毫秒为单位返回 key 的剩余的过期时间。
        jedis.pttl("address");
        // 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
        jedis.ttl("address");
        //移除 key 的过期时间，key 将持久保持。
        jedis.persist("address");

        // 查找所有符合给定模式( pattern)的 key 。
        //jedis.key
        // 将当前数据库的 key 移动到给定的数据库 db 当中
        jedis.move("moveKey", 1);

        // 从当前数据库中随机返回一个 key 。
        jedis.randomKey();
        // 修改 key 的名称（改名成功时提示 OK ，失败时候返回一个错误。
        //当 OLD_KEY_NAME 和 NEW_KEY_NAME 相同，或者 OLD_KEY_NAME 不存在时，返回一个错误。 当 NEW_KEY_NAME 已经存在时， RENAME 命令将覆盖旧值）
        jedis.set("oldKey","oldKey_value");
        jedis.rename("oldKey", "newKey");
        jedis.rename("newKey", "oldKey");
        // 仅当 newkey 不存在时，将 key 改名为 newkey 。（修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0 。）
        jedis.renamenx("oldKey", "newKey");
        // 返回 key 所储存的值的类型。
        jedis.type("address");



        // 设置字符串
        jedis.set("address", "北京市海淀区");
        System.out.println("address = "+jedis.get("address"));
        //返回 key 中字符串值的子字符
        System.out.println(jedis.getrange("address", 0, 5));
        // getSet  将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
        System.out.println("oldValue = "+jedis.getSet("address", "上海市静安区")+"，newValue = "+jedis.get("address"));
        // 同时设置一个或多个 key-value 对
        jedis.mset("key1","value1","key2","value2");
        // 获取所有(一个或多个)给定 key 的值 （返回值 List）
        System.out.println(jedis.mget("key1", "key2"));
        // 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。
        jedis.setex("address", 900, "西直门");
        System.out.println(jedis.get("address"));
        // 返回 key 所储存的字符串值的长度。
        System.out.println(jedis.strlen("address"));
        // 将 key 中储存的数字值增一。
        jedis.set("number","10");
        jedis.incr("number");
        System.out.println(jedis.get("number"));
        // 将 key 所储存的值加上给定的增量值（increment） 。
        jedis.incrBy("number", 5);
        System.out.println(jedis.get("number"));
        // jedis.decr() 将 key 中储存的数字值减一。
        // jedis.decrBy() 将 key 所储存的值减去给定的减量值（decrement）
        jedis.del("number");
        // 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。
        jedis.append("address", " 灵境胡同");
        System.out.println(jedis.get("address"));

        // Redis Hash
        jedis.del("myHash");
        // 设置值
        jedis.hset("myHash", "A1", "a1");
        jedis.hset("myHash", "A2", "a2");
        // 获取值
        System.out.println("myHash.A1 = " + jedis.hget("myHash", "A1"));
        // 删除一个或多个哈希表字段
        jedis.hdel("myHash", "B1", "B2", "B3");
        // 查看哈希表中指定的字段是否存在
        System.out.println("A1是否存在 = " + jedis.hexists("myHash", "A1") + "，B1是否存在 = " + jedis.hexists("myHash", "1"));
        // 获取在哈希表中指定 key 的所有字段和值
        System.out.println("myHash = " + jedis.hgetAll("myHash"));
        // 获取所有哈希表中的字段
        System.out.println("myHash.keys = " + jedis.hkeys("myHash"));
        // 获取哈希表中字段的数量
        System.out.println("myHash.hlen = " + jedis.hlen("myHash"));
        // 同时将多个 field-value (域-值)对设置到哈希表 key 中。
        Map<String,String> hash = new HashMap<String,String>();
        hash.put("A3", "a3");
        hash.put("A4", "a4");
        jedis.hmset("myHash", hash);
        System.out.println("myHash = " + jedis.hgetAll("myHash"));
        jedis.hmget("myHash", "A1", "A3");



        // 设置列表 List
        jedis.del("students");
        jedis.lpush("students", "ZhangSan");
        jedis.lpush("students", "LiSi", "WangWu", "Zhaoliu");
        jedis.rpush("students", "GuiJiaoqi");
        System.out.println("students.length = "+jedis.llen("students"));
        System.out.println("students = " + jedis.lrange("students", 0, jedis.llen("students")));
        /*List<String> list = jedis.lrange("students", 0, jedis.llen("students"));
        for(int i=0; i<list.size(); i++){
            System.out.println("list("+i+") = "+list.get(i));
        }*/



        // Redis 集合 （Set)
        jedis.del("mySet");
        jedis.del("mySet1");
        // 添加值
        jedis.sadd("mySet", "BeiJing", "ShangHai", "Tianjin", "ChongQing");
        // 查询
        System.out.println("mySet.members = " + jedis.smembers("mySet"));
        // 取集合的成员数
        System.out.println("mySet.scard = " + jedis.scard("mySet"));
        // 取给定集合的差集
        jedis.sdiff("mySet1", "mySet2");
        // 返回给定所有集合的交集
        jedis.sinter("mySet1", "mySet2", "mySet3");
        // 返回所有给定集合的并集
        jedis.sunion("mySet1", "mySet2", "mySet3");
        // 判断 member 元素是否是集合 key 的成员
        System.out.println(jedis.sismember("mySet", "ShenZhen"));
        // 将 member 元素从 source 集合移动到 destination 集合
        jedis.smove("mySet", "mySet1", "ChongQing");
        System.out.println("mySet = " + jedis.smembers("mySet") + "，mySet1 = " + jedis.smembers("mySet1"));
        // 返回集合中一个或多个随机数
        System.out.println("mySet.srandmenber = " + jedis.srandmember("mySet"));
        jedis.srandmember("mySet", 2);
        // 移除集合中一个或多个成员
        jedis.srem("mySet", "GuangZhou", "NanJing");


        // Redis 有序集合 （sorted set）
        jedis.del("myZset");
        jedis.del("myZset1");
        // 向有序集合添加一个或多个成员，或者更新已存在成员的分数
        jedis.zadd("myZset", 1D, "A");
        jedis.zadd("myZset", 2D, "K");
        Map<String,Double> map = new HashMap<String, Double>();
        map.put("Q", 3D);
        map.put("J", 4D);
        map.put("10", 5D);
        jedis.zadd("myZset", map);
        Map<String,Double> map1 = new HashMap<String, Double>();
        map1.put("AAA", 10D);
        map1.put("BBB", 10D);
        map1.put("CCC", 10D);
        map1.put("DDD", 10D);
        map1.put("EEE", 10D);
        jedis.zadd("myZset1", map1);
        // 集合成员数
        long count = jedis.zcard("myZset");
        // 查询
        System.out.println("myZset = " + jedis.zrange("myZset", 0, count));
        // 计算在有序集合中指定区间分数的成员数
        System.out.println("myZset 分数在2至4之间的成员数 = " + jedis.zcount("myZset", 2, 4));
        // 在有序集合中计算指定字典区间内成员数量 （分数相同的 ZSET）
        System.out.println("myZset1 指定字典区间内成员数量 = " + jedis.zlexcount("myZset1", "(AAA", "[DDD"));
        // 通过字典区间返回有序集合的成员 （分数相同的 ZSET）
        System.out.println("myZset1.zrangeByLex = " + jedis.zrangeByLex("myZset1", "(AAA", "[DDD"));
        System.out.println("myZset1.zrangeByLex = " + jedis.zrangeByLex("myZset1", "-", "+"));
        // 返回有序集合中指定成员的索引
        System.out.println("myZset.K.zrank = " + jedis.zrank("myZset", "A"));
        // 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序  （排序从 0 开始）
        System.out.println("myZset.J.zrevrank = " + jedis.zrevrank("myZset", "J"));
        // 返回有序集中，成员的分数值
        System.out.println("myZset.K.zscore = " + jedis.zscore("myZset", "K"));


        // Redis 事务
        // 开启事务
        Transaction transaction = jedis.multi();
        //监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断，事务的exec会返回null。jedis.watch(key)都会返回OK
        String watch = jedis.watch("address");
        // 事务操作 （与jedis操作一样，只是对象换成了transaction）
        transaction.append("address", "   18号楼 ");
        // 提交事务
        transaction.exec();
        // 取消 WATCH 命令对所有 key 的监视。
        jedis.unwatch();
        // 取消事务
        //transaction.discard();
        transaction.close();


        // 所有key
        /*keys = jedis.keys("*");
        it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }*/
        System.out.println("address1 = " + jedis.get("address"));
        jedis.connect();
        System.out.println("address2 = " + jedis.get("address"));
        jedis.disconnect();
        System.out.println("address3 = " + jedis.get("address"));
        jedis.close();
        System.out.println("address4 = " + jedis.get("address"));

    }
}
