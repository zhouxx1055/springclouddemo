package org.zxx.cloud.commenum;

public enum BaseResultError {


    //前端调用错误码（用户操作错误码）
    SUCCESS(0, "成功!"),
    API_OK(0, "成功!"),
    API_INVALID_PARAMETER(1000, "输入有误，请重新输入！"),
    API_NO_PARAMETER(1000, "输入有误，请重新输入！"),
    API_FUNCTION_NO_ACCESS(1001, "无访问权限，请联系管理人员！"),
    API_UNKNOWN_ERROR(1002, "网络超时，请稍后操作！"),
    API_NETWORK_ERROR(1003, "网络异常，请稍后操作！"),


    /**  100-199  鉴权错误码 */
    API_LOGIN_OK(0, "登录成功!"),
    API_LOGIN_Fail(100, "登录失败，账号或密码错误!"),
    API_LOGIN_Fail_USER_NOT_EXISTED(101, "登录失败，该账户未注册，请重新注册!"),
    API_LOGIN_INVALID_TOKEN(102, "无效token，请重新登录!")
    ;

    private String msg;
    private int code;

    private BaseResultError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }
}
