package com.example.getInfo;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class GetRequest
{
    public String getObj(String url,String ORIGINAL,String REFERER,String USERAGENT)//url相关参数
    {
        String result = "";
        BufferedReader reader = null;
        try//建立链接
        {
            URL newUrl = new URL(url);
            URLConnection connection = newUrl.openConnection();
            //
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("origin",ORIGINAL);
            connection.setRequestProperty("referer",REFERER);
            connection.setRequestProperty("user-agent",USERAGENT);
            //
            connection.connect();

            Map<String,List<String>> map = connection.getHeaderFields();

            for(String key : map.keySet())//遍历输出所有的响应头字段(向控制台)
            {
                System.out.println(key + "-----" + map.get(key));
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null)//读取并把内容加给result，最终返回result
            {
                result+=line;
            }

        }
        catch (Exception e)//异常处理
        {
            System.out.println("GET异常:" + e);
            e.printStackTrace();
        }
        finally//结束释放处理
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }

        return result;
    }


}
