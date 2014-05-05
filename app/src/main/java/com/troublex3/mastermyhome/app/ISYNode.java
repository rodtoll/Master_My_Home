package com.troublex3.mastermyhome.app;

import java.util.ArrayList;

/**
 * Created by rodtoll on 4/12/14.
 * Represents a single ISYNode in the hierarchy
 */
public class ISYNode {

    public enum ISYNodeType {
        FOLDER,
        SCENE,
        DEVICE,
        PROGRAM
    }

    public ISYNode() {
        mChildNodes = new ArrayList<ISYNode>();
    }

    public void copyFrom(ISYNode isyNode) {
        setNodeName(isyNode.getNodeName());
        setNodeAddress(isyNode.getNodeAddress());
    }

    public String getNodeName() {
        return mNodeName;
    }

    public void setNodeName(String mNodeName) {
        this.mNodeName = mNodeName;
    }

    public String getNodeAddress() {
        return mNodeAddress;
    }

    public void setNodeAddress(String mNodeAddress) {
        this.mNodeAddress = mNodeAddress;
    }

    public ArrayList<ISYNode> getChildNodes() {
        return mChildNodes;
    }

    public void addChildNode(ISYNode isyNode) {
        this.mChildNodes.add(isyNode);
    }

    public ISYNodeType getNodeType() {
        return mNodeType;
    }

    public void setNodeType(ISYNodeType nodeType) {
        mNodeType = nodeType;
    }

    protected String mNodeName;
    protected String mNodeAddress;
    protected ArrayList<ISYNode> mChildNodes;
    protected ISYNodeType mNodeType;

    @Override
    public String toString() {
        return mNodeName + " [" + mNodeAddress + "]";
    }
}
