package com.troublex3.mastermyhome.app;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3Entity {

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    protected String mName;
    protected ISYNode mNode;

    public ISYNode getNode() {
        return this.mNode;
    }

    public void setNode(ISYNode node) {
        this.mNode = node;
    }

}
