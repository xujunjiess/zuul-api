package com.example.util;

public class Result {
    private String code;
    private String msg;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result ok(){
        Result result=new Result();
        result.setCode("200");
        result.setMsg("操作成功");
        return result;
    }
    public static Result ok(Object data){
        Result result=new Result();
        result.setCode("200");
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }
    public static Result filed(){
        Result result=new Result();
        result.setCode("500");
        result.setMsg("操作失败");
        return result;
    }
}
