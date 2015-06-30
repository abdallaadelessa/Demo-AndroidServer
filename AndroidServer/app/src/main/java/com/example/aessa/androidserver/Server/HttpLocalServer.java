package com.example.aessa.androidserver.Server;

import com.example.aessa.androidserver.Utils;
import com.example.aessa.androidserver.model.User;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by aessa on 6/28/2015.
 */
public class HttpLocalServer extends NanoHTTPD
{
    public static final String KEY_ID = "id";

    public HttpLocalServer(int port)
    {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session)
    {
        Method method = session.getMethod();
        String uri = session.getUri();
        Map<String, String> parms = session.getParms();
        Utils.log(method + " '" + uri + "' " + " Params:" + parms);
        if(uri.equals("/user"))
        {
            String text = "";
            if(parms.size() > 0 && parms.containsKey(KEY_ID))
            {
                // Get User By ID
                int id = Integer.parseInt(parms.get(KEY_ID));
                User user = ServerMock.getUser(id);
                text = new Gson().toJson(user);

            }
            else
            {
                // Get All Users
                List<User> users = ServerMock.getUsers();
                text = new Gson().toJson(users);
            }
            return new NanoHTTPD.Response(Response.Status.OK, MIME_PLAINTEXT, text);
        }
        else
        {
            final String html = "<html><head><head><body><h1>Hello World From My Local Server</h1></body></html>";
            return new NanoHTTPD.Response(Response.Status.OK, MIME_HTML, html);
        }
    }
}
