package com.resumejdbc.result;

public class Result {

	/** 処理結果 */
	private boolean ok;
	
	/** 結果NGの時のメッセージ */
	private String errMsg;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
