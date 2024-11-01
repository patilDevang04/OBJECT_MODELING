package com.crio.codingame.commands;

import java.util.List;

import com.crio.codingame.entities.User;
import com.crio.codingame.services.IUserService;

public class CreateUserCommand implements ICommand{

    private final IUserService userService;
    
    public CreateUserCommand(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public void execute(List<String> tokens) {
        try {
            String name=tokens.get(1);
            User user=userService.create(name);
            System.out.println(user);            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
