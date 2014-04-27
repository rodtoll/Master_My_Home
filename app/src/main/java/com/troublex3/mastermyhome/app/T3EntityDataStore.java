package com.troublex3.mastermyhome.app;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3EntityDataStore {

    private T3EntityDataStore(Context context) {
        this.mContext = context;
        this.mGroups = new ArrayList<T3EntityGroup>();
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
                T3EntityGroup group = new T3EntityGroup();
                group.setName(node.getNodeName());
                group.setNode(node);
                this.mGroups.add(group);

                for(ISYNode subNode : node.getChildNodes()) {
                    ISYDevice device = (ISYDevice) subNode;
                    T3Entity newEntity = new T3Entity();
                    newEntity.setName(device.getNodeName());
                    newEntity.setNode(device);
                    group.addSubEntity(newEntity);
                }
            }
        }
    }

    private Context mContext;
    private ArrayList<T3EntityGroup> mGroups;

    public ArrayList<T3EntityGroup> getGroups() {
        return this.mGroups;
    }
}
