package org.example.service;

import org.example.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training createTrainingProfile(Training training);
    Optional<Training> selectTrainingProfile(int id);
    List<Training> selectAll();
}
