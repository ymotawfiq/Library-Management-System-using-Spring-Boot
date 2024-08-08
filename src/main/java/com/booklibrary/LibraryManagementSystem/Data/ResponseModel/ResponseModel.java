package com.booklibrary.LibraryManagementSystem.Data.ResponseModel;


public class ResponseModel <T> {

	private int statusCode;
	private boolean isSuccess;
	private String Message;
	private T Data;
	
	public ResponseModel(int statusCode, boolean isSuccess,
			String Message, T Data) {
		this.Data = Data;
		this.isSuccess = isSuccess;
		this.Message =  Message;
		this.statusCode = statusCode;
	}
	
	public ResponseModel(int statusCode, boolean isSuccess,
			String Message) {
		this.Data = null;
		this.isSuccess = isSuccess;
		this.Message =  Message;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public T getData() {
		return Data;
	}

	public void setData(T data) {
		Data = data;
	}
	
	
	
	
	
}
