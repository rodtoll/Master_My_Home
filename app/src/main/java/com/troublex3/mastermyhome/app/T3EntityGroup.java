package com.troublex3.mastermyhome.app;

import java.util.ArrayList;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3EntityGroup extends T3Entity {

    public T3EntityGroup() {
        this.mSubEntities = new ArrayList<T3Entity>();
    }

    protected ArrayList<T3Entity> mSubEntities;

    public ArrayList<T3Entity> getSubEntities() {
        return this.mSubEntities;
    }

    public void addSubEntity(T3Entity entity) {
        this.mSubEntities.add(entity);
    }
}
