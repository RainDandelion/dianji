import com.nio.common.Result;
import com.nio.common.ResultCode;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;

public class test {

    @Test
    public void test(){
        System.out.println(ResultCode.SUCCESS.getCode());
        Result.ok();
    }

    @Test
    public void test2(){
        String temp =  "f5 05 02 " + "%s" +" 00 " + "%s" +" ff ff ff ff ff ff 12";
        String string = String.format(temp,"01","02");

        System.out.println(string);
    }

}
