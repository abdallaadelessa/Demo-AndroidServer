package com.example.aessa.androidserver.Server;

import com.example.aessa.androidserver.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aessa on 6/28/2015.
 */
public class ServerMock
{
    private static List<User> users;

    public static List<User> getUsers()
    {
        if(users != null)
        {
            return users;
        }

        users = new ArrayList<>();
        users.add(new User(1, "User1", 10));
        users.add(new User(2, "User1", 10));
        users.add(new User(3, "User1", 10));
        users.add(new User(4, "User1", 10));
        users.add(new User(5, "User1", 10));
        users.add(new User(6, "User1", 10));
        return users;
    }

    public static User getUser(int id)
    {
        User user = null;
        List<User> users = getUsers();
        for(User u : users)
        {
            if(u.getId() == id)
            {
                user = u;
                break;
            }
        }
        return user;
    }
}
