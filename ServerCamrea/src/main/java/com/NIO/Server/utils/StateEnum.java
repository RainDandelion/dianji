package com.NIO.Server.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public enum StateEnum {

    NIOD("ID错误",-1),
    OFFLINE("设备离线",-2),
    ERRORCMD("方向有误",-3),
    BORDERLINE("已到边界",0),
    OK("成功",1);

    private String message;
    private Integer Code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    StateEnum(String message, Integer code) {
        this.message = message;
        this.Code = code;
    }
}
