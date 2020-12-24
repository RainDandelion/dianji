import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class test {
    @Test
    public void test() throws UnsupportedEncodingException {
    byte[] bytes = new byte[]{(byte) 0xf5,0x05,0x02,0,0,2, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x10};
        System.out.println();
        byte b = Byte.parseByte("1");
        System.out.println(b);
    }
}
