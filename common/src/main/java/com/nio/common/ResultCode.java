package com.nio.common;

import lombok.Data;


public enum ResultCode {
    SUCCESS("成功",20000),
    ERROR("失败",20001);

    private String Result;
    private Integer Code;

    ResultCode(String Result, Integer Code) {
        this.Result = Result;
        this.Code = Code;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }
}
