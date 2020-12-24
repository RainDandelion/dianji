package com.nio.controller;

import com.nio.client.NioClient;
import com.nio.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/NioClient")
@Api(description="nio客户端控制器")
public class ClientController {

    @Autowired
    private NioClient client;


    @ApiOperation(value = "客户端测试用")
    @GetMapping("/test/{direction}/{step}")
    public Result send(@ApiParam(name = "direction", value = "方向（00/01）", required = true) @PathVariable("direction")String direction,
                       @ApiParam(name = "step", value = "步伐", required = true)@PathVariable("step") String step){



        String temp =  "f5 05 02 " + "%s" +" 00 " + "%s" +" ff ff ff ff ff ff 12";
        String data = String.format(temp,direction,step);
        String sendInfo = client.SendInfo(data);
        if (sendInfo.isEmpty()){
            return Result.error();
        }
        else {
            return Result.ok();
        }

    }

}
