package Runner;

import Configuration.Configuration;
import clientAPI.FactoryRequest;
import clientAPI.RequestInformation;
import clientAPI.ResponseInformation;
import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.net.httpserver.BasicAuthenticator;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.deps.com.thoughtworks.xstream.core.util.Base64Encoder;
import gherkin.deps.net.iharder.Base64;
import helpers.JsonHelper;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.*;


public class StepDef2 {
    //private static final String TOKEN_AUTHENTICATION_HEADER = ;
    ResponseInformation response = new ResponseInformation();

    Map<String,String> variables = new HashMap<>();

    String email;
    String userDate;
    String codeBasicAuth="";

    @Given("^I have authentication to todo\\.ly$")
    public void iHaveAuthenticationToTodoLy() {

    }

    @When("^I send (POST|PUT|DELETE|GET) request '(.*)' with json an (TOKEN|BASIC) authentication$")
    public void iSendPOSTRequestApiUserJsonWithJsonAnBASICAuthentication
            (String method, String url, String authentication, String jsonBody)
            {
        RequestInformation request = new RequestInformation();

        request.setUrl(Configuration.HOST + this.replaceVariables(url));
        request.setBody(jsonBody);

        if(authentication.equals("TOKEN")){
            request.addHeaders(Configuration.TOKEN_AUTHENTICATION_HEADER,this.replaceVariables(authentication));
        }else{
            request.addHeaders(Configuration.BASIC_AUTHENTICATION_HEADER, Configuration.BASIC_AUTHENTICATION);
        }

        response = FactoryRequest.make(method.toLowerCase()).send(request);
    }

    @Then("^I expected the response code (\\d+)$")
    public void iExpectedTheResponseCode(int expectResponseCode) {
        System.out.println("Response Code " + response.getResponseCode());

        System.out.println("Body : " + response.getResponseBody());

        //String user = "alvaro21@email.com:Alvaro123";
        //String code = "Basic "+new String(new Base64().encodeBytes(user.getBytes()));

        Assert.assertEquals("ERROR!! the response code is incorrect",
                expectResponseCode, response.getResponseCode());

    }



    @And("^I expected the response body is equal$")
    public void iExpectedTheResponseBodyIsEqual(String expectResponseBody) throws JSONException {

        System.out.println("Response Body "+this.replaceVariables(response.getResponseBody()));

        Assert.assertTrue("ERROR", JsonHelper.areEqualJSON(this.replaceVariables(expectResponseBody),
                response.getResponseBody()));
    }

    private String replaceVariables (String value){

        for(String key : this.variables.keySet()){
            value = value.replace(key, this.variables.get(key));
        }
        return value;
    }

    @And("^I get the property value '(.*)' and save on (.*)$")
    public void iGetThePropertyValueValueAndSaveOnValue(String property,
                                                        String nameVariable) throws JSONException{

        String value = JsonHelper.getValueFromJson(response.getResponseBody(), property);
        variables.put(nameVariable, value);
        System.out.println("variable : "+nameVariable+" value: "+variables.get(nameVariable));
    }

    @When("^I create user with Email:'(.*)'###'(@.*\\.\\w{3})', Password:'(.*)' and FullName:'(Alvaro Quena)'$")
    public void iCreateUserWithEmailMiCorreoNumRandomEmailComPasswordAlvaroAndFullNameAlvaroQuena(
            String nameEmail, String domain, String password, String fullName) {
        int NRandom = (int)(Math.floor(Math.random()*1000));

        email = nameEmail+NRandom+domain;
        userDate="{" +
                "\"FullName\": \""+fullName+"\","+
                "\"Email\": \""+email+"\","+
                "\"Password\": \""+password+"\""+
                "}";
    }

    //Genera Codigo Basic para el usurio
    private String getBasicCode(String jsonBody) throws JSONException {
        String dates;
        String code;

        dates = JsonHelper.convertJSON(jsonBody).get("Email")+":"+
                JsonHelper.convertJSON(jsonBody).get("Password");

        code = "Basic "+Base64.encodeBytes(dates.getBytes());

        return code;
    }

    @And("^I send (POST|GET|PUT|DELETE) request '(.*)' with json and (BASIC|TOKEN) authentication to create user$")
    public void iSendPOSTRequestApiUserJsonWithJsonAndBASICAuthenticationToCreateUser(
            String method, String url, String authentication)
            throws JSONException{
        RequestInformation request = new RequestInformation();

        request.setUrl(Configuration.HOST + this.replaceVariables(url));
        request.setBody(userDate);

        if(authentication.equals("TOKEN")){
            request.addHeaders(Configuration.TOKEN_AUTHENTICATION_HEADER,this.replaceVariables(authentication));
        }else{
            request.addHeaders(Configuration.BASIC_AUTHENTICATION_HEADER, getBasicCode(userDate));
        }

        response = FactoryRequest.make(method.toLowerCase()).send(request);
        switch(method){
            case "POST":
                variables.put("BasicAuth", getBasicCode(userDate));
                break;
            case "DELETE":
                variables.remove("BasicAuth");
                break;
        }
        System.out.println(("Basic in POST::"+variables.get("BasicAuth")));
    }


    @When("^I send (POST|GET|DELETE|PUT) request to '(.*)' with the name (.*) with (BASIC|TOKEN) authentication$")
    public void iSendPOSTRequestToApiProjectsJsonWithTheNameNameProjectWithBASICAuthentication(
            String method, String url, String nameProject, String authentication
    ){
        RequestInformation request = new RequestInformation();
        String jsonProy;
        request.setUrl(Configuration.HOST+url);

        jsonProy = "{"+
                "\"Content\": \""+nameProject+(int)Math.floor(Math.random()*100)+"\""+
                "}";
        request.setBody(jsonProy);
        if(authentication.equals("TOKEN")){
            request.addHeaders(Configuration.TOKEN_AUTHENTICATION_HEADER,this.replaceVariables(authentication));
        }else{
            request.addHeaders(Configuration.BASIC_AUTHENTICATION_HEADER, variables.get("BasicAuth"));
        }

        response = FactoryRequest.make(method.toLowerCase()).send(request);

    }
/*NEUVO*/
    @And("^I send (POST|GET|PUT|DELETE) request '(.*)' with json and (BASIC|TOKEN) authentication$")
    public void iSendMethodUserRequestApiUserJsonWithJsonAndBASICAuthentication(
            String method, String url, String authentication, String jsonBody)
            throws JSONException {
        RequestInformation request = new RequestInformation();
        request.setUrl(Configuration.HOST + this.replaceVariables(url));
        request.setBody(jsonBody);
        userDate = jsonBody;
        if(authentication.equals("TOKEN")){
            request.addHeaders(Configuration.TOKEN_AUTHENTICATION_HEADER,this.replaceVariables(authentication));
        }else{
            request.addHeaders(Configuration.BASIC_AUTHENTICATION_HEADER, getBasicCode(jsonBody));
        }
        response = FactoryRequest.make(method.toLowerCase()).send(request);
    }

    @When("^I send (POST|GET|PUT|DELETE) request to '(.*)' with the content (.*) and (BASIC|TOKEN) authentication$")
    public void iSendMethodProjectRequestToApiProjectsJsonWithTheContentContentProjectAndBASICAuthentication(
            String method, String url, String contentProy, String authentication) throws JSONException{
        RequestInformation request = new RequestInformation();
        String jsonProy;
        request.setUrl(Configuration.HOST + this.replaceVariables(url));

        jsonProy = "{"+
                "\"Content\": \""+contentProy+"\""+
                "}";
        request.setBody(jsonProy);
        if(authentication.equals("TOKEN")){
            request.addHeaders(Configuration.TOKEN_AUTHENTICATION_HEADER,this.replaceVariables(authentication));
        }else{
            request.addHeaders(Configuration.BASIC_AUTHENTICATION_HEADER, getBasicCode(userDate));
        }
        response = FactoryRequest.make(method.toLowerCase()).send(request);
    }


//    @When("^I send  request to '' with the name (.*)$")

}
