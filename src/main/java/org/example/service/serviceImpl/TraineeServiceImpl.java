package org.example.service.serviceImpl;

import org.example.dao.Dao;
import org.example.model.Trainee;
import org.example.model.User;
import org.example.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

public class TraineeServiceImpl implements TraineeService {
    private Dao<User> userDao;
    private final UserUtils createUtils = new UserUtilsImpl();
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private static final String NO_ID_MSG = "Provided Trainee Id does not exist";
    private Dao<Trainee> traineeDao;


    @Override
    public int createTraineeProfile(String firstName, String lastName, Date dateOfBirth, String address) {
        if (firstName == null || lastName == null || dateOfBirth == null || address == null) {
            logger.error("The next fields were not provided: " +
                    (firstName == null ? "firstName, " : "") +
                    (lastName == null ? "lastName, " : "") +
                    (dateOfBirth == null ? "dateOfBirth, " : "") +
                    (address == null ? "address " : "")
            );
            return -1;
        }
        User newUser = userDao
                .save(createUtils.createUser(firstName, lastName, userDao));

        logger.info("Creating Trainee Profile with id " + newUser.getId());
        Trainee newTrainee = traineeDao.save(new Trainee(dateOfBirth, address, newUser));
        return newTrainee.getId();
    }

    @Override
    public boolean updateTraineeProfile(int id, String firstName, String lastName, boolean isActive, Date dateOfBirth, String address) {
        Optional<Trainee> traineeToUpdate = traineeDao.get(id);
        if (traineeToUpdate.isEmpty()) {
            logger.error(NO_ID_MSG);
            return false;
        }

        int userId = traineeToUpdate.get().getUser().getId();
        User updatedUser = createUtils.updateUser(userId, firstName, lastName, userDao);

        traineeToUpdate.get().setDateOfBirth(dateOfBirth);
        traineeToUpdate.get().setAddress(address);
        traineeToUpdate.get().setUser(updatedUser);

        logger.info("Updating Trainee Profile with id " + id);
        return traineeDao.update(id, traineeToUpdate.get()) != null;
    }

    @Override
    public boolean deleteTraineeProfile(int id) {
        Optional<Trainee> traineeToDelete = traineeDao.get(id);
        if (traineeToDelete.isEmpty()) {
            logger.error(NO_ID_MSG);
            return false;
        }
        logger.info("Deleting Trainee Profile with id " + id);
        Optional<Trainee> deletedTrainee = traineeDao.delete(id);
        return deletedTrainee.isPresent();
    }

    @Override
    public Trainee selectTraineeProfile(int id) {
        Optional<Trainee> trainee = traineeDao.get(id);
        if (trainee.isEmpty()) {
            logger.error(NO_ID_MSG);
            return null;
        }
        logger.info("Selecting Trainee Profile with id " + id);
        return trainee.get();
    }

    public void setUserDao(Dao<User> userDao) {
        this.userDao = userDao;
    }

    public void setTraineeDao(Dao<Trainee> traineeDao) {
        this.traineeDao = traineeDao;
    }

}
