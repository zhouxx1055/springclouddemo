package org.zxx.cloud.exception;

public class JwtException extends Exception {
    int code;
    String exceptionMessage;

    private JwtException(){
    }

    public JwtException(String msg){
        new JwtException(1,msg);
    }
    public JwtException(int code,String msg){
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
