package com.troublex3.mastermyhome.app;

import java.util.UUID;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3Entity {

    public UUID getId()  { return this.mId; }
    public void setId(UUID id) { this.mId = id; }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    protected String mName;
    protected ISYNode mNode;
    protected UUID mId;

    public ISYNode getNode() {
        return this.mNode;
    }

    public void setNode(ISYNode node) {
        this.mNode = node;
    }

}
