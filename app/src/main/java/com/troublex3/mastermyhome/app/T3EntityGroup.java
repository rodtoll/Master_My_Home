package com.troublex3.mastermyhome.app;

import java.util.ArrayList;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3EntityGroup extends T3Entity {

    public T3EntityGroup(T3EntityDataStore owningStore) {
        this.mSubEntities = new ArrayList<T3Entity>();
        this.mDataStore = owningStore;
    }

    protected ArrayList<T3Entity> mSubEntities;
    protected T3EntityDataStore mDataStore;

    public ArrayList<T3Entity> getSubEntities() {
        return this.mSubEntities;
    }

    public T3Entity newSubEntity(String name, ISYNode node) {
        T3Entity newEntity = this.mDataStore.newEntityForGroup(this);
        newEntity.setName(name);
        newEntity.setNode(node);
        return newEntity;
    }

    public void removeSubEntity(T3Entity entity) {
        this.mSubEntities.remove(entity);
    }

    public void addSubEntity(T3Entity entity) {
        this.mSubEntities.add(entity);
    }
}
