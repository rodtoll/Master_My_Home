package com.troublex3.mastermyhome.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3EntityDataStore {

    private T3EntityDataStore(Context context) {
        this.mContext = context;
        this.mGroups = new ArrayList<T3EntityGroup>();
        this.mEntities = new HashMap<UUID, T3Entity>();
    }

    private static T3EntityDataStore sT3EntityDataStore;

    public static T3EntityDataStore get(Context context) {
        if(sT3EntityDataStore == null) {
            sT3EntityDataStore = new T3EntityDataStore(context);
        }
        return sT3EntityDataStore;
    }

    public void buildFromISYController(ISYController controller) {
        for(ISYNode node : controller.getNodeMap().values()) {
            if(node.getNodeType() == ISYNode.ISYNodeType.FOLDER) {
                T3EntityGroup group = new T3EntityGroup(this);
                group.setName(node.getNodeName());
                group.setNode(node);
                this.mGroups.add(group);

                for(ISYNode subNode : node.getChildNodes()) {
                    ISYDevice device = (ISYDevice) subNode;
                    T3Entity newEntity = new T3Entity();
                    newEntity.setId(UUID.randomUUID());
                    newEntity.setName(device.getNodeName());
                    newEntity.setNode(device);
                    this.mEntities.put(newEntity.getId(), newEntity);
                    group.addSubEntity(newEntity);
                }
            }
        }
    }

    public T3EntityGroup newGroup(String groupName) {
        T3EntityGroup newGroup = new T3EntityGroup(this);
        newGroup.setName(groupName);
        newGroup.setId(UUID.randomUUID());
        this.mEntities.put(newGroup.getId(), newGroup);
        this.mGroups.add(newGroup);
        return newGroup;
    }

    public T3Entity newEntityForGroup(T3EntityGroup group) {
        T3Entity newEntity = new T3Entity();
        newEntity.setId(UUID.randomUUID());
        group.addSubEntity(newEntity);
        this.mEntities.put(newEntity.getId(), newEntity);
        return newEntity;
    }

    public void removeGroup(T3EntityGroup group) {
        this.mGroups.remove(group);
    }

    private Context mContext;
    private ArrayList<T3EntityGroup> mGroups;

    private HashMap<UUID, T3Entity> mEntities;

    public ArrayList<T3EntityGroup> getGroups() {
        return this.mGroups;
    }

    public T3Entity getEntity(UUID id) {
        return this.mEntities.get(id);
    }
}
