package clientAPI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RequestPUT extends RequestClient{
    @Override
    public ResponseInformation send(RequestInformation request) {
        System.out.println("PUT " + request.getUrl());
        System.out.println("body " + request.getBody());

        Response response = this.client.target(request.getUrl())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .headers(request.getHeaders())
                .post(Entity.json(request.getBody()));

        ResponseInformation responseInformation = new ResponseInformation(

        );
        response.close();
        return responseInformation;
    }
}
