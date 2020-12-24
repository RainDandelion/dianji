package com.njrz.comtroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DefaultController {
    @RequestMapping("/test")
    public String index(){
        return "index";
    }

    @GetMapping("/test2")
    @ResponseBody
    public String index2(){
        return "{\n" +
                "\n" +
                "        \"key\" : 520,\n" +
                "\n" +
                "        \"key1\" : 1314\n" +
                "\n" +
                "    }";
    }
}
