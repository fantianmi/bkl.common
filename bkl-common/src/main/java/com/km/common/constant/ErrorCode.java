package com.km.common.constant;

public class ErrorCode {
    // 成功响应
    public static final int SUCCESS = 0;
    // 操作失败响应
    public static final int FAILURE = -1;

    // 100 - 1000 用于通用状态码
    // 登录失败
    public static final int LOGIN_FAIL = -100;
    // 服务器繁忙
    public static final int SERVER_ERROR = -101;
    // 请求超时
    public static final int REQUEST_TIMEOUT = -102;
    // 请求错误
    public static final int REQUEST_ERROR = -103;
    // 查询不到相关记录
    public static final int DATA_NOT_FOUND = -104;
    // 没有指定相关的action
    public static final int PARAM_ACTION_EMPTY_ERROR = -105;
    // 日期格式化错误
    public static final int PARAM_DATE_FORMAT_ERROR = -106;
    // 参数错误
    public static final int PARAM_ERROR = -107;
    // 参数空错误
    public static final int PARAM_EMPTY_ERROR = -108;
    // 密码验证错误
    public static final int PASSWORD_ERROR = -109;

}
