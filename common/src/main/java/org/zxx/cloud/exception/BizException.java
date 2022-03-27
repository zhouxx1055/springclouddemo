package org.zxx.cloud.exception;

public class BizException extends Exception {
    int code;
    String exceptionMessage;

    private BizException(){

    }
    public BizException(String msg){
        new BizException(1,msg);
    }
    public BizException(int code,String msg){
        super(msg);
        this.code=code;
        this.exceptionMessage=msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
