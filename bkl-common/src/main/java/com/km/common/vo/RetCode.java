package com.km.common.vo;


public enum RetCode {
	OK(0,"success"),
	SESSION_TIMEOUT(-1,"未登陆或登陆超时"),
	ERROR(500, "操作失败"),
	NOPERM(401,"没有权限"),	
	FORBID(403,"forbid"),
	
	USERNAME_OR_PASSWORD_ERROR(601,"用户名或密码错误"),
	USER_SECRET_ERROR(602,"交易密码错误"),
	USER_STATUS_FREEZE(603, "帐户已经冻结"),
	USER_TRADE_PASSWORD_NOT_SET(604, "未设置交易密码"),
	TRADE_PASSWORD_ERROR_REPEATEDLY(605, "密码连续出错"),
	USER_EXIST(606, "用户已存在"),
	EMAIL_ERROR(607, "错误的邮箱"),
	USER_NOT_ACTIVE(608, "帐户未激活"),
	USER_NOT_EXIST(609, "用户不存在"),
	EMAIL_SEND_ERROR(610, "邮件发送失败"),
	
	WALLET_DISABLED(640, "钱包已禁用"),
	WALLET_NOT_START(641, "钱包服务异常"),
	BTC_SENDCOINDS_TO_ADDR_ERROR(642,"转帐地址不合法"),
	BTC_SENDCOINDS_AMOUNT_LT_ZERO(643,"钱包转帐金额小于0"),
	
	
	ORDER_NOTALLOW_ID_NULL(701,"订单id为空或不存在"),
	ORDER_CONFIRM_YET(702,"订单已确认"),
	ORDER_CANNOT_FOUND_USER(703,"找不到订单用户"),
	ORDER_USER_AMOUNT_NOT_ENOUGH(704,"用户余额不足"),
	ORDER_TYPE_UNKNOW(705,"订单类型未知"),
	ORDER_CONFIRM_CANCEL(706,"订单已取消"),
	ORDER_DISABLE(707,"订单功能已禁用"),
	ORDER_AMOUNT_OR_PRICE_NULL(708,"订单的金额或数量为空"),
	ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED(708,"订单的金额或数量超过精度限制"),
	ORDER_AMOUNT_LESS_OR_EQUAL_0(709,"订单的金额数量小于或等于0"),
	ORDER_PRICE_LESS_OR_EQUAL_0(710,"订单的价格小于或等于0"),
	ORDER_AMOUNT_LESS_THAN_LIMIT(711, "订单的数量小于最低限制"),
	ORDER_PRICE_LESS_THAN_LIMIT(712, "订单的价格小于最低限制"),
	ORDER_PRICE_DECIMAL_PRECISION_EXCEED(713,"订单的价格超过精度限制"),
	

	
	
	RENGOU_DISABLED(730,"认购功能没有开启"),
	RENGOU_OUT_OF_LIMIT(731,"认购超过次数限制"),
	RENGOU_OVERFLOW(732,"认购量超过认购总量"),
	
	DB_SAVE_FAILED(801,"保存失败"),
	
	ORGINAL_PASSWORD_EMPTY(901, "未输入旧密码"),
	ORGINAL_PASSWORD_ERROR(902, "旧密码错误"),
	PASSWORD_ILLEGAL(903, "非法密码"),
	TWO_PASSWORD_NOT_EQUAL(904, "两次密码不一致"),
	LOGIN_PASSWORD_EQUAL_TRADE_PASSWORD(905, "登陆密码与交易密码一致"),
	END(Integer.MAX_VALUE,"占位符"),
	
	SHOPORDER_SID_NOT_SET(741,"无对应商品信息"),
	SHOPORDER_OUT_OF_LIMIT(742,"超过购买限制"),
	SHOPORDER_OVERFLOW(743,"购买超过可买总量"),
	SHOPORDER_RNAME_NOT_SET(744,"收货人没有设置"),
	SHOPORDER_RTEL_NOT_SET(745,"联系方式没有设置"),
	SHOPORDER_RADR_NOT_SET(746,"收获地址没有设置");
	
	
	private int code;
	private String msg;
	private RetCode(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int code() {
		return code;
	}
	
	public String msg() {
		return msg;
	}
}
