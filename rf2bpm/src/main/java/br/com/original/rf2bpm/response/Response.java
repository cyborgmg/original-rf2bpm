package br.com.original.rf2bpm.response;

public class Response {

	private int statusCode;
	private String response;

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Response(int statusCode, String response) {
		super();
		this.statusCode = statusCode;
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
