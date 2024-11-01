package com.crio.codingame.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.crio.codingame.dtos.UserRegistrationDto;
import com.crio.codingame.entities.Contest;
import com.crio.codingame.entities.ContestStatus;
import com.crio.codingame.entities.RegisterationStatus;
import com.crio.codingame.entities.ScoreOrder;
import com.crio.codingame.entities.User;
import com.crio.codingame.exceptions.ContestNotFoundException;
import com.crio.codingame.exceptions.InvalidOperationException;
import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.codingame.repositories.IContestRepository;
import com.crio.codingame.repositories.IUserRepository;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IContestRepository contestRepository;

    public UserService(IUserRepository userRepository, IContestRepository contestRepository) {
        this.userRepository = userRepository;
        this.contestRepository = contestRepository;
    }

    @Override
    public User create(String name) {
     return userRepository.save(new User(null, name, 1500));
    }



    @Override
    public List<User> getAllUserScoreOrderWise(ScoreOrder scoreOrder){
        if(scoreOrder.equals(ScoreOrder.ASC))
            return userRepository.findAll()
            .stream()
            .sorted((u1,u2)->u1.getScore()-u2.getScore())
            .collect(Collectors.toList());
        else if(scoreOrder.equals(ScoreOrder.DESC))
            return userRepository.findAll()
            .stream()
            .sorted((u1,u2)->u2.getScore()-u1.getScore())
            .collect(Collectors.toList());
     return Collections.emptyList();
    }

    @Override
    public UserRegistrationDto attendContest(String contestId, String userName) throws ContestNotFoundException, UserNotFoundException, InvalidOperationException {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new ContestNotFoundException("Cannot Attend Contest. Contest for given id:"+contestId+" not found!"));
        User user = userRepository.findByName(userName).orElseThrow(() -> new UserNotFoundException("Cannot Attend Contest. User for given name:"+ userName+" not found!"));
        if(contest.getContestStatus().equals(ContestStatus.IN_PROGRESS)){
            throw new InvalidOperationException("Cannot Attend Contest. Contest for given id:"+contestId+" is in progress!");
        }
        if(contest.getContestStatus().equals(ContestStatus.ENDED)){
            throw new InvalidOperationException("Cannot Attend Contest. Contest for given id:"+contestId+" is ended!");
        }
        if(user.checkIfContestExists(contest)){
            throw new InvalidOperationException("Cannot Attend Contest. Contest for given id:"+contestId+" is already registered!");
        }
        user.addContest(contest);
        userRepository.save(user);
        return new UserRegistrationDto(contest.getName(), user.getName(),RegisterationStatus.REGISTERED);
    }



    @Override
    public UserRegistrationDto withdrawContest(String contestId, String userName) throws ContestNotFoundException, UserNotFoundException, InvalidOperationException {
        Contest contest=contestRepository.findById(contestId).orElseThrow(()-> new ContestNotFoundException("Cannot Withdraw Contest. Contest for given id:"+contestId+" not found!"));
        User user=userRepository.findByName(userName).orElseThrow(()-> new UserNotFoundException("Cannot Withdraw Contest. User for given name:"+userName+" not found!"));
        if(contest.getCreator()==user){
            throw new InvalidOperationException("Cannot Withdraw Contest. Contest Creator:"+userName+ "not allowed to withdraw contest!");
        }
        if(!user.checkIfContestExists(contest)){
            throw new InvalidOperationException("Cannot Withdraw Contest. User for given contest id:"+contestId+" is not registered!");
        }
        if(contest.getContestStatus().equals(ContestStatus.IN_PROGRESS)){
            throw new InvalidOperationException("Cannot Withdraw Contest. Contest for given id:"+contestId+" is in progress!");
        }
        if(contest.getContestStatus().equals(ContestStatus.ENDED)){
            throw new InvalidOperationException("Cannot Withdraw Contest. Contest for given id:"+contestId+" is ended!");
        }
        user.deleteContest(contest);
        userRepository.save(user);
        UserRegistrationDto udtr = new UserRegistrationDto(contest.getName(),user.getName(),RegisterationStatus.NOT_REGISTERED);
        return udtr;
    }
    
}
