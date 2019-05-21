package skcc.nexcore.client.application.web;

public class SpringFactoryContext implements java.io.Serializable {

	private static final long serialVersionUID = 7499137206501461728L;

	public String bean;
	public String method;
	public HttpParameterData requestData;
	public HttpParameterData responseData;

	public boolean fail;
	public String message;

}
