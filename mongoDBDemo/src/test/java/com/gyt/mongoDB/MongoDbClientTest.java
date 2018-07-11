/**
 * FileName: MongoDbClientTest
 * Author:   Rock_Guo
 * Date:     2018/7/9 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.gyt.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Rock_Guo
 * @create 2018/7/9
 * @since 1.0.0
 */
public class MongoDbClientTest {

    private Bson fir;

    /**
     *  数据库访问需要用户名密码
     */
    @Test
    public void mongoDbTest(){

        //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
        //ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress serverAddress = new ServerAddress("127.0.0.1",27017);
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress);
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential credential = MongoCredential.createScramSha1Credential("username", "databaseName", "password".toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);

        MongoClient mongoClient = new MongoClient(addrs,credentials);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");

    }


    /**
     * Mongo 数据库无需用户名密码验证
     */
    @Test
    public void mongoDb_withoutPass_test(){
        // 连接到MongoDB服务
        MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
        //连接到MongoDB数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("runoob");
        System.out.println("Connect to database successfully");

        // DB下的所有表名
        //MongoIterable<String> colls = mongoDatabase.listCollectionNames();

        // 先删除集合（重复创建会报错）
        MongoCollection mongoCollection = mongoDatabase.getCollection("gyt");
        mongoCollection.drop();
        // 创建集合 Collection （相当于关系型数据库中的table）
        mongoDatabase.createCollection("gyt");
        mongoCollection = mongoDatabase.getCollection("gyt");
        System.out.println("集合创建成功");
        //创建文档 Document（相当于关系型数据库中table中的row）
        Document document1 = new Document("fid","123456").append("fname","Rock").append("faddress","Beijing");
        Document document2 = new Document("fid","000000").append("fname","Huston").append("faddress","DZ").append("contry","USA");
        List<Document> list = new ArrayList<Document>();
        list.add(document1);
        list.add(document2);
        //插入文档
        mongoCollection.insertMany(list);
        //mongoCollection.insertOne(); // 只插入一条数据

        //检索所有文档
        /**
         * 1. 获取迭代器FindIterable<Document>
         * 2. 获取游标MongoCursor<Document>
         * 3. 通过游标遍历检索出的文档集合
         * */
        FindIterable<Document> findIterable = mongoCollection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        System.out.println("插入文档");
        while(mongoCursor.hasNext()){
            Document doc = mongoCursor.next();
            System.out.println(doc.toJson());
        }

        // 修改文档
        mongoCollection.updateMany(Filters.eq("fid", "123456"), new Document("$set",new Document("fname","Rock123456")));
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        findIterable = mongoCollection.find().projection(fields);
        mongoCursor = findIterable.iterator();
        System.out.println("修改文档");
        while(mongoCursor.hasNext()){
            Document doc = mongoCursor.next();
            System.out.println(doc.toJson());
        }

        // 删除文档
        mongoCollection.deleteOne(Filters.eq("fid","000000")); // 删除第一条
        //mongoCollection.deleteMany(Filters.eq("fid","000000")); //删除全部
        findIterable = mongoCollection.find();
        mongoCursor = findIterable.iterator();
        System.out.println("删除文档");
        while(mongoCursor.hasNext()){
            Document doc = mongoCursor.next();
            System.out.println(doc.toJson());
        }

        mongoClient.close();

    }

    /** 分页查询 */
     public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
         Bson orderBy = new BasicDBObject("_id", 1);
         return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
     }
}
