package org.example.service.serviceImpl;

import org.example.configuration.Storage;
import org.example.dao.DaoConnectionImpl;
import org.example.model.Trainer;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TrainerServiceImplTest {


    @Autowired
    private TrainerServiceImpl trainerService;
    @Autowired
    private Storage storage;
    private DaoConnectionImpl<Trainer> daoConnection = new DaoConnectionImpl<>(Trainer.class);
    private DaoConnectionImpl<User> daoConnectionUsers = new DaoConnectionImpl<>(User.class);
    private List<Trainer> trainers = new ArrayList<>();
    private List<User> users = new ArrayList<>();


    @BeforeEach
    public void setUp(){
        User userTest = new User("User Test", "User Test", ".");
        userTest.setId(1);
        User userTest2 = new User("User Test 2", "User Test 2", ".");
        userTest2.setId(2);
        users.add(userTest);
        users.add(userTest2);

        Trainer trainerTest =  new Trainer(1,1, userTest);
        trainerTest = trainerTest.setId(1);
        trainerTest.setUser(userTest);
        Trainer trainerTest2 = new Trainer(2, 2, userTest2);
        trainerTest2 = trainerTest2.setId(2);
        trainerTest2.setUser(userTest2);
        trainers.add(trainerTest);
        trainers.add(trainerTest2);


        daoConnection = mock(DaoConnectionImpl.class);
        when(daoConnection.getEntities(anyString())).thenReturn(trainers);
        when(daoConnection.writeEntities(anyString(), anyList())).thenReturn(trainers);


        daoConnectionUsers = mock(DaoConnectionImpl.class);
        when(daoConnectionUsers.getEntities(anyString())).thenReturn(users);
        when(daoConnectionUsers.writeEntities(anyString(), anyList())).thenReturn(users);

    }

    @Test
    void createTrainerProfile() {

        Trainer trainerTest =  new Trainer(1,1);
        trainerTest = trainerTest.setId(storage.getTrainerDao().getNextId());

        Trainer trainerCreated = trainerService.createTrainerProfile("Test Trainer", "Test Trainer Last", 1);

        assertNotNull(trainerCreated);
        assertEquals(trainerTest.getId(), trainerCreated.getId());
        verify(daoConnection, times(1)).writeEntities(anyString(), anyList());
    }

    @Test
    void updateTrainerProfile() {
        Trainer trainerTest = trainerService.selectTrainerProfile(1);
        trainerTest = trainerTest.setSpecialization(2);
        String trainerTestUserName = trainerTest.getUser().getUsername();

        Trainer updatedTrainer = trainerService.updateTrainerProfile(1, "Updated Trainer Name", "Updated Trainer Last", false, 1);
        assertNotEquals(trainerTestUserName, updatedTrainer.getUser().getUsername());
    }

    @Test
    void selectTrainerProfile() {
        Trainer trainerTest = new Trainer(1,1);
        trainerTest.setId(1);
        Trainer trainserSelected = trainerService.selectTrainerProfile(1);

        assertNotNull(trainserSelected);
        assertEquals(trainerTest.getId(), trainserSelected.getId());
        assertEquals(trainerTest.getUserId(), trainserSelected.getUserId());
    }

    @Test
    void selectAll() {
        List<Trainer> selectedTrainers = trainerService.selectAll();
        assertNotNull(selectedTrainers);
        assertEquals(trainers, selectedTrainers);
    }

    @Test
    void trainersUsernamesDifferent(){
        Trainer trainer1 = trainerService.createTrainerProfile("John", "Smith", 1);
        Trainer trainer2 = trainerService.createTrainerProfile("John", "Smith", 1);

        assertNotEquals(trainer1.getUser().getUsername(), trainer2.getUser().getUsername());

    }
}