package com.crio.codingame.commands;

import java.util.List;

import com.crio.codingame.dtos.UserRegistrationDto;
import com.crio.codingame.exceptions.InvalidContestException;
import com.crio.codingame.exceptions.InvalidOperationException;
import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.codingame.services.IUserService;

public class WithdrawContestCommand implements ICommand{

    private final IUserService userService;
    
    public WithdrawContestCommand(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public void execute(List<String> tokens) {
        String contestId=tokens.get(1);
        String userName= tokens.get(2);
        try{
            UserRegistrationDto uRDto=userService.withdrawContest(contestId, userName);
            System.out.println(uRDto);
        }catch(InvalidContestException e){
            System.out.println("Cannot Withdraw Contest. Contest for given id:"+contestId+" not found!");
        }catch(UserNotFoundException e){
            System.out.println("Cannot Withdraw Contest. User for given name:"+ userName+" not found!");
        }catch(InvalidOperationException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
