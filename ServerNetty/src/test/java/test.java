import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {
        // 创建一个默认可以关闭的HTTP客户端，用于访问HTTPServer端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建一个get请求用于访问当前的百度地址
        HttpGet httpGet = new HttpGet("http://localhost:8080/test");
        //这里可以使用HttpPost httpPost=new HttpPost("http://www.baidu.com");
        //创建post请求
        CloseableHttpResponse response = null;
        try {
            // 通过当前的客户端执行当前的get请求并获得响应结果
            response = httpclient.execute(httpGet);
            // 输出响应状态码
            System.out.println("输出当前的响应状态行：" + response.getStatusLine());
            // 获取响应的实体
            HttpEntity entity = response.getEntity();
            if(entity==null) {
                System.out.println("没有响应数据");
                return;
            }
            // do something useful with the response body
            // and ensure it is fully consumed
            // EntityUtils.consume(entity);，这里不应该在这里关闭如果关闭后将不能使用这个流了！
            byte[] byteArray = EntityUtils.toByteArray(entity);// 使用工具来拷贝流的数据为二进制数组
            EntityUtils.consume(entity);// 最后在这里关闭
            String content = new String(byteArray, "UTF-8");// 使用String将当前的二维数组变为字符
            System.out.println(content);
        } finally {
            response.close();
        }
        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }
}
