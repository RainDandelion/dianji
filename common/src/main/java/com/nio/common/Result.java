package com.nio.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;  //对象类型
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();   //数据采用键值对形式存储
    //构造器私有
    private Result(){

    }
    //使用静态方法响应成功对象
    public static Result ok(){
        Result r=new Result();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMessage("成功");
        r.setSuccess(true);
        return r;
    }
    //使用静态方法响应失败对象
    public static Result error(){
        Result r=new Result();
        r.setCode(ResultCode.ERROR.getCode());
        r.setMessage("失败");
        r.setSuccess(false);
        return r;
    }

    public Result success(Boolean success){
        this.setSuccess(success);
        //this代表当前对象，即R对象，返回this可以实现链式调用
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
