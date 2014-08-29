package org.yws.pangu.domain;

import java.io.Serializable;

import org.yws.pangu.enums.EOperateResult;
import org.yws.pangu.enums.ERegisterType;

public class RegisterInfo implements Serializable{
	/**  */
	private static final long serialVersionUID = 6376471374645655384L;
	private String hostAddress;
	private String hostName;
	private long touchTime;
	private ERegisterType registerType;
	private EOperateResult operateResult = EOperateResult.NA;
	
	public RegisterInfo(String hostAddress, String hostName, ERegisterType registerType) {
		super();
		this.hostAddress = hostAddress;
		this.hostName = hostName;
		this.touchTime = System.currentTimeMillis();
		this.registerType = registerType;
	}

	public RegisterInfo(String hostAddress, String hostName, long touchTime,
			ERegisterType registerType) {
		super();
		this.hostAddress = hostAddress;
		this.hostName = hostName;
		this.touchTime = touchTime;
		this.registerType = registerType;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public long getTouchTime() {
		return touchTime;
	}

	public void setTouchTime(long touchTime) {
		this.touchTime = touchTime;
	}

	public ERegisterType getRegisterType() {
		return registerType;
	}

	public void setRegisterType(ERegisterType registerType) {
		this.registerType = registerType;
	}

	public EOperateResult getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(EOperateResult operateResult) {
		this.operateResult = operateResult;
	}

}
