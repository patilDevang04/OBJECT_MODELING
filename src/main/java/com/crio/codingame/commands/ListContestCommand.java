package com.crio.codingame.commands;

import java.util.List;

import com.crio.codingame.entities.Contest;
import com.crio.codingame.entities.Level;
import com.crio.codingame.services.IContestService;

public class ListContestCommand implements ICommand{

    private final IContestService contestService;
    
    public ListContestCommand(IContestService contestService) {
        this.contestService = contestService;
    }



    @Override
    public void execute(List<String> tokens) {
        try {
            if(tokens.size() == 1){
                List<Contest> listOfContest = contestService.getAllContestLevelWise(null);
                System.out.println(listOfContest);
                return;
            }
            Level contestLevel=Level.valueOf(tokens.get(1));
            List<Contest> listOfContest=contestService.getAllContestLevelWise(contestLevel);
            System.out.println(listOfContest);            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
