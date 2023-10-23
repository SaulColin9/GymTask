package org.example.model;

public class TrainingType implements Entity{
    int id;
    String trainingTypeName;

    public TrainingType(){

    }
    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    @Override
    public int getId() {
        return id;
    }

    public TrainingType setId(int id) {
        this.id = id;
        return this;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }


}
