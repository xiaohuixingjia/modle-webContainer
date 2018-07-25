package com.umpay.proxyservice.exception;

/**
 * 基础异常
 * 
 * @author xuxiaojia 2017年6月6日
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = -4097833482955009541L;

	private Retcode retcode;

	private String errMsg;

	private Exception e;

	public Retcode getRetcode() {
		return retcode;
	}

	public void setRetcode(Retcode retcode) {
		this.retcode = retcode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public BaseException(Retcode retcode) {
		super(retcode.getCode()+retcode.getName());
		this.retcode = retcode;
		this.errMsg=retcode.getName();
	}
	
	public BaseException(Retcode retcode, Exception e) {
		super(retcode.getCode()+retcode.getName(),e);
		this.retcode = retcode;
		this.errMsg=retcode.getName();
		this.e = e;
	}

	public BaseException(Retcode retcode, String errMsg) {
		super(retcode.getCode()+retcode.getName()+errMsg);
		this.retcode = retcode;
		this.errMsg=retcode.getName()+errMsg;
	}
	public BaseException(Retcode retcode, String errMsg,Exception e) {
		super(retcode.getCode()+retcode.getName()+errMsg,e);
		this.retcode = retcode;
		this.errMsg=retcode.getName()+errMsg;
		this.e = e;
	}

	@Override
	public String toString() {
		return "BaseException [retcode=" + retcode + ", errMsg=" + errMsg + ", e=" + e + "]";
	}
	

}
