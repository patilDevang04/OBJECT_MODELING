package com.crio.codingame.commands;

import java.util.List;

import com.crio.codingame.entities.Contest;
import com.crio.codingame.entities.Level;
import com.crio.codingame.exceptions.InvalidContestException;
import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.codingame.services.IContestService;

public class CreateContestCommand implements ICommand{

    private final IContestService contestService;

    public CreateContestCommand(IContestService contestService) {
        this.contestService = contestService;
    }


    @Override
    public void execute(List<String> tokens) {
        try{
        String contestName=tokens.get(1);
        Level contestLevel=Level.valueOf(tokens.get(2));
        String contestCreator=tokens.get(3);
        Integer numQuestion=null;
        if(tokens.size()==5)
            numQuestion=Integer.parseInt(tokens.get(4));
        Contest contest=contestService.create(contestName,contestLevel,contestCreator,numQuestion);
        System.out.println(contest);
        }catch(UserNotFoundException e){
            System.out.println("Contest Creator for given name: creator not found!");
        }catch(InvalidContestException e){
            System.out.println("Request cannot be processed as enough number of questions can not found. Please try again later!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    
}
