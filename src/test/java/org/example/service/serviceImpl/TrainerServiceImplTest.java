package org.example.service.serviceImpl;

import org.example.dao.Dao;
import org.example.matchers.TrainerMatcher;
import org.example.model.Trainer;
import org.example.model.User;
import org.example.service.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TrainerServiceImplTest {
    @InjectMocks
    private TrainerServiceImpl trainerService;
    @Mock
    private Dao<Trainer> trainerDao;
    @Mock
    private Dao<User> userDao;
    @Mock
    private UserUtils userUtils;
    private User user;
    private Trainer trainerTest;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("first", "last");
        user.setId(1);

        trainerTest = new Trainer(1, user);
        trainerTest.setUser(user);
        trainerTest.setId(1);

        when(userDao.get(anyInt())).thenReturn(Optional.ofNullable(user));
        when(trainerDao.get(anyInt())).thenReturn(Optional.ofNullable(trainerTest));

    }

    @Test
    void givenValidRequest_TrainerShouldBeCreated() {
        // arrange
        Trainer testTrainer = createNewTrainer();


        when(userUtils.createUser("John", "Doe")).thenReturn(testTrainer.getUser());
        when(trainerDao.save(argThat(new TrainerMatcher(testTrainer)))).thenReturn(testTrainer);
        // act
        int createdTrainerId = trainerService.createTrainerProfile("John", "Doe", 1);

        // assert
        assertThat(createdTrainerId).isEqualTo(1);
        verify(trainerDao, times(1)).save(argThat(new TrainerMatcher(testTrainer)));
        verify(userUtils, times(1)).createUser("John", "Doe");
    }

    @Test
    void givenValidRequest_TrainerShouldBeUpdated() {
        // arrange
        Trainer testTrainer = createNewTrainer();

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setFirstName("Jean");
        updatedUser.setLastName("Doe");
        updatedUser.setIsActive(false);
        updatedUser.setUsername("Jean.Doe");

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(1);
        updatedTrainer.setUser(updatedUser);
        updatedTrainer.setSpecialization(2);


        when(trainerDao.get(1)).thenReturn(Optional.of(testTrainer));
        when(userUtils.updateUser(1, "Jean", "Doe", false)).thenReturn(updatedUser);
        when(trainerDao.update(eq(1), argThat(new TrainerMatcher(updatedTrainer)))).thenReturn(updatedTrainer);

        // act
        boolean actualResponse = trainerService.updateTrainerProfile(1, "Jean", "Doe", false, 2);

        // assert
        assertThat(actualResponse).isTrue();
        verify(trainerDao, times(1)).get(1);
        verify(userUtils, times(1)).updateUser(1, "Jean", "Doe", false);
        verify(trainerDao, times(1)).update(eq(1), argThat(new TrainerMatcher(testTrainer)));
    }


    @Test
    void givenValidId_TrainerShouldBeReturned() {
        // arrange
        Trainer testTrainer = createNewTrainer();

        when(trainerDao.get(1)).thenReturn(Optional.of(testTrainer));

        // act
        Trainer actualResponse = trainerService.selectTrainerProfile(1);

        // assert
        assertThat(actualResponse).isNotNull();
        verify(trainerDao, times(1)).get(1);
    }


    @Test
    void givenNonExistingTrainerId_ThrowsException() {
        // arrange
        when(trainerDao.get(77)).thenReturn(Optional.empty());
        // assert
        assertThatThrownBy(() -> trainerService.selectTrainerProfile(77)).
                isInstanceOf(IllegalArgumentException.class).
                hasMessage("Provided Trainer Id does not exist");
        verify(trainerDao, times(1)).get(77);
    }

    @Test
    void givenNonExistingTrainerIdUpdate_ThrowsException() {
        // arrange
        when(trainerDao.get(77)).thenReturn(Optional.empty());
        // assert
        assertThatThrownBy(
                () -> trainerService
                        .updateTrainerProfile(77, "New Name",
                                "New LastName", false, 2)).
                isInstanceOf(IllegalArgumentException.class).
                hasMessage("Provided Trainer Id does not exist");
        verify(trainerDao, times(1)).get(77);

    }

    @Test
    void givenInvalidRequestCreate_ThrowsException() {
        assertThatThrownBy(
                () -> trainerService.createTrainerProfile(null, null, 2)).
                isInstanceOf(IllegalArgumentException.class).
                hasMessage("firstName, lastName and specialization arguments cant be null");
    }

    Trainer createNewTrainer() {
        User newUser = new User();

        newUser.setIsActive(true);
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setPassword("password");
        newUser.setUsername("John.Doe");
        newUser.setId(1);

        Trainer newTrainer = new Trainer();
        newTrainer.setSpecialization(1);
        newTrainer.setId(1);
        newTrainer.setUser(newUser);

        return newTrainer;
    }

}