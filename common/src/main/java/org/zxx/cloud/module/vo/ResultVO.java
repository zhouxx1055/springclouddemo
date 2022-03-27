package org.zxx.cloud.module.vo;

import lombok.Data;
import org.zxx.cloud.commenum.BaseResultError;

@Data
public class ResultVO<T> {

    /**
     * 状态码，比如1000代表响应成功
     */
    private int code;
    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;
    /**
     * 响应的具体数据
     */
    private T data;

    private ResultVO(){

    }
    public ResultVO(T data) {
        this(BaseResultError.SUCCESS, data);
    }
    public ResultVO(BaseResultError resultError) {
        this(resultError,null);
    }
    public ResultVO(BaseResultError resultError,T data) {
        this(resultError.getCode(),resultError.getMsg(),data);
    }
    public ResultVO(int code,String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVO fail(BaseResultError resultError){

        return new ResultVO(resultError);
    }
    public static ResultVO fail(BaseResultError resultError,Object data){

        return new ResultVO(resultError,data);
    }
}
