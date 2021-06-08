package com.example.tasks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.COVIDinfo;
import com.example.getInfo.GetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
@EnableScheduling
public class GetInfoTask
{
    static String ORIGINAL = "";
    static String REFERER = "";
    static String USERAGENT  = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0";
    static String URL = "https://giea.api.storeapi.net/api/94/219?format=json&appid=6611&sign=7283e964d6e917792a662b01e7ce5190";//后面的参数是与网站用户验证相关的

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*public void jsonLeaf(JsonNode node)//遍历解析JSON各个节点-测试用
    {
        if (node.isValueNode())
        {

            System.out.print(node.toString()+" ");
        }
        if (node.isObject())
        {
            Iterator<Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext())
            {
                Entry<String, JsonNode> entry = it.next();
                jsonLeaf(entry.getValue());
                System.out.println();
            }
        }
        if (node.isArray())
        {
            Iterator<JsonNode> it = node.iterator();
            while(it.hasNext())
            {
                jsonLeaf(it.next());
            }
        }
    }*/

    /*public void paresJson(String data)//把String型数据解析为JSON-测试用
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(data);
            //
            jsonLeaf(rootNode);
            //
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }*/

    @Scheduled(fixedRate = 600000)//每10分钟定时更新
    public void downloadInfo() throws Exception
    {
        // 获取当前时间
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
        String timeNow=simpleDateFormat.format(date1).substring(0,10);
        //System.out.println("test1-----"+timeNow);

        //根据当前时间建表
        jdbcTemplate.execute("DROP TABLE IF EXISTS table_in_"+timeNow);
        jdbcTemplate.execute("CREATE TABLE table_in_"+timeNow+"(time VARCHAR(512))");
        jdbcTemplate.update("INSERT INTO table_in_"+timeNow+"(time) VALUES(?)", simpleDateFormat.format(date1));

        //获取String类型的返回,并转化为JSON类型，向控制台输出测试
        GetRequest getRequest = new GetRequest();
        String data = getRequest.getObj(URL,ORIGINAL,REFERER,USERAGENT);
        //paresJson(data);//测试用

        //System.out.println(data);//测试用
        JSONObject jsonObject = JSONObject.parseObject(data);//把String类型的data转化为JSONObject
        JSONArray jsonArray = jsonObject.getJSONArray("retdata");//把JSON对象里的retdata“数组”搞出来，也就是获取“省级”信息

        List<COVIDinfo> list = new ArrayList<>();//list用于记录for循环里的数据并且最后给数据库

        for(int i=0;i<jsonArray.size();i++)//遍历数组，获取“省级”信息
        {
            COVIDinfo covid = new COVIDinfo();
            JSONObject idInfo = jsonArray.getJSONObject(i);
            try
            {
                covid.setId(String.valueOf(i));
                covid.setConfirm(idInfo.get("curConfirm").toString());
                covid.setDied(idInfo.get("died").toString());
                covid.setHeal(idInfo.get("heal").toString());
                covid.setLastUpdateTime(timeNow);//时间直接用“今天”的
                //covid.setAsymptomatic(idInfo.get("asymptomatic").toString());//只有这里有时会抛出NullPointerException异常，因为原API提供的港澳台数据里就没有这一项，所以用以下处理
                try
                {
                    covid.setAsymptomatic(idInfo.get("asymptomatic").toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    covid.setAsymptomatic("0");
                }
                covid.setName(idInfo.get("xArea").toString());
                covid.setStoreConfirm(idInfo.get("confirm").toString());

                try//测试用
                {
                    System.out.println("编号："+covid.getId());//测试用
                    System.out.println("省份："+covid.getName());//测试用
                    System.out.println("确诊："+covid.getConfirm());//测试用
                    System.out.println("累计确诊："+covid.getStoreConfirm());//测试用
                    System.out.println("治愈："+covid.getHeal());//测试用
                    System.out.println("死亡："+covid.getDied());//测试用
                    System.out.println("无症状："+covid.getAsymptomatic());//测试用
                    System.out.println("获取时间："+covid.getLastUpdateTime());//测试用
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                list.add(covid);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                covid=null;
                idInfo=null;
            }

        }

        //以下为插入数据库的代码
        jdbcTemplate.execute("DROP TABLE IF EXISTS Covid_"+timeNow);//如果已经有了就删除
        jdbcTemplate.execute("CREATE TABLE Covid_"+timeNow+"("
                +"id VARCHAR(128), name VARCHAR(128), confirm VARCHAR(128), "
                +"storeConfirm VARCHAR(128), heal VARCHAR(128), died VARCHAR(128), "
                +"asymptomatic VARCHAR(128), lastUpdateTime VARCHAR(128)"
                +")");//新建表
        jdbcTemplate.execute("ALTER TABLE Covid_"+timeNow+" CONVERT TO CHARACTER SET utf8mb4");

        jdbcTemplate.batchUpdate("INSERT INTO Covid_"+timeNow+"("
                +"id, name, confirm, storeConfirm, heal, died, asymptomatic, lastUpdateTime)"
                +"VALUES(?,?,?,?,?,?,?,?)",
                list.stream().map(name ->name.toString().split("\t")).collect(Collectors.toList()));

    }

    /*@Scheduled(fixedRate = 600000)//-测试用，无任何用处
    public List<COVIDinfo> getAllInfo()
    {

        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
        String timeNow=simpleDateFormat.format(date1).substring(0,10);

        final String sql="SELECT * FROM Covid_"+timeNow;
        System.out.println(sql);
        //List<COVIDinfo> list = jdbcTemplate1.query(sql,new BeanPropertyRowMapper<>(COVIDinfo.class));//由于前面的是周期性执行，这里的jdbcTemplate又不能用了，只好写一个新的
        BeanPropertyRowMapper<COVIDinfo> mapper = new BeanPropertyRowMapper<>(COVIDinfo.class);
        List<COVIDinfo> query = jdbcTemplate.query(sql,mapper);
        for(COVIDinfo covid:query)
        {
            System.out.println(covid);
        }
        return query;
    }*/

    /*@Scheduled(fixedRate = 10000)//测试用
    public void test()
    {
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeNow=simpleDateFormat.toString().substring(0,10);
        System.out.println("test1-----"+timeNow);
    }*/


}
