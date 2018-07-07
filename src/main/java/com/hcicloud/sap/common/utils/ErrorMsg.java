package com.hcicloud.sap.common.utils;

public class ErrorMsg {
	public static String getMsg(int err) {
		String errMessage = "";
		switch (err) {
		case 1:
			errMessage = "参数不合法";
			break;
		case 2:
			errMessage = "参数不支持";
			break;
		case 3:
			errMessage = "引擎初始化失败";
			break;
		case 4:
			errMessage = "引擎处理失败";
			break;
		case 5:
			errMessage = "服务处理失败，内部小错误";
			break;
		case 6:
			errMessage = "请求token为空";
			break;
		case 7:
			errMessage = "请求配置串错误";
			break;
		case 8:
			errMessage = "资源ID已经存在";
			break;
		case 9:
			errMessage = "资源ID不存在";
			break;
		case 10:
			errMessage = "所有的会话都在使用中";
			break;
		case 11:
			errMessage = "该资源正在使用中";
			break;
		case 12:
			errMessage = "引擎正在训练";
			break;
		case 50:
			errMessage = "引擎处理成功";
			break;
		case 51:
			errMessage = "引擎参数不合法";
			break;
		case 52:
			errMessage = "引擎参数不支持";
			break;
		case 53:
			errMessage = "引擎文件失败";
			break;
		case 54:
			errMessage = "引擎件失败";
			break;
		case 55:
			errMessage = "资源版本错误";
			break;
		case 56:
			errMessage = "资源检查不合法";
			break;
		case 57:
			errMessage = "会话句柄和资源句柄绑定失败";
			break;
		case 58:
			errMessage = "资源句柄没有绑定会话句柄";
			break;
		case 59:
			errMessage = "没有足够的内存空间";
			break;
		case 60:
			errMessage = "授权错误";
			break;
		case 61:
			errMessage = "结果为空";
			break;
		case 62:
			errMessage = "引擎内部错误";
			break;
		default:
			break;
		}
		return errMessage;
	}
}
