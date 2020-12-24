

import com.netty.proto.SubscribeReqProto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class test {
    @Test
    public void  test() throws Exception{

        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setProductName("netty book");
        List<String> address = new ArrayList<>();
        address.add("南京");
        address.add("乌龟");
        builder.addAllAddress(address);
        SubscribeReqProto.SubscribeReq req = builder.build();
        System.out.println("Before encode:"+req.toString());
        String address1 = req.getAddress(0);
        System.out.println(address1);


        SubscribeReqProto.SubscribeReq result=decode(encode(req));
        System.out.println("decode cotent is:"+result.toString());

        System.out.println(req.equals(result));

    }
    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }
    private static SubscribeReqProto.SubscribeReq decode (byte[] bytes) throws Exception{
        return SubscribeReqProto.SubscribeReq.parseFrom(bytes);
    }
}
