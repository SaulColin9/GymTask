package org.example.dao;

import org.example.configuration.Storage;
import org.example.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DaoImpl<T extends Entity>  implements Dao<T> {
    final Class<T> tClass;
    protected List<T> entities = new ArrayList<>();
    protected Map<Integer, T> storageEntities;


    public void setStorage(Storage storage){
        if(tClass.equals(new User().getClass())){
            storageEntities = (Map<Integer, T>) storage.getUsers();
            return;
        }
        if(tClass.equals(new Trainee().getClass())){
            storageEntities = (Map<Integer, T>) storage.getTrainees();
            return;
        }
        if(tClass.equals(new Trainer().getClass())){
            storageEntities = (Map<Integer, T>) storage.getTrainers();
            return;
        }
        if(tClass.equals(new Training().getClass())){
            storageEntities = (Map<Integer, T>) storage.getTrainings();
            return;
        }
        if(tClass.equals(new TrainingType().getClass())){
            storageEntities = (Map<Integer, T>) storage.getTrainingTypes();
        }
    }

    public DaoImpl(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public int getNextId(){
        if(storageEntities.values().isEmpty()){
            return 1;
        }
        return storageEntities.get(storageEntities.values().size() - 1).getId() + 1;
    }
    @Override
    public  Optional<T> get(int id){
        T foundT = null;
        for(T t: storageEntities.values()){
            if(t.getId() == id){
                foundT = t;
            }
        }
        return Optional.ofNullable(foundT);
    }
    @Override
    public List<T> getAll() {
        return new ArrayList<>(storageEntities.values());
    }
    @Override
    public T save(T t){
        T newT = setId(t, getNextId());
        storageEntities.put(newT.getId(),newT);
        return newT;

    }
    @Override
    public T update(int id, T t) {
        Optional<T> foundEntity = get(id);
        if(foundEntity.isPresent()){
            t.setId(id);
            storageEntities.put(id, t);
            return t;
        }
        return null;
    }

    @Override
    public Optional<T> delete(int id) {
        Optional<T> foundEntity = get(id);
        if(foundEntity.isPresent()){
            storageEntities.remove(id);
        }
        return foundEntity;
    }
    public T setId(T t, int id){
        t.setId(id);
        return t;
    }



}
