package clientAPI;

public class ResponseInformation {

    private String responseBody;
    private String responseMessage;
    private int responseCode;

    public ResponseInformation(){}
/*
    public ResponseInformation(String responseBody, String responseMessage, int responseCode) {
        this.responseBody = responseBody;
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
    }
*/
    public ResponseInformation(String responseBody, int responseCode) {
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }
    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
