package com.troublex3.mastermyhome.app;

/**
 * Created by rodtoll on 4/7/14.
 */
public class ISYDevice extends ISYNode {

    public enum ISYDeviceType {
        UNKNOWN,
        LIGHT_SWITCH,
        DOOR_SENSOR_SENSE,
        DOOR_SENSOR_LOW_BATTERY,
        MOTION_SENSOR_MOVEMENT,
        MOTION_SENSOR_LIGHT,
        MOTION_SENSOR_LOW_BATTERY,
        FAN_LIGHT,
        FAN_MOTOR,
        INLINE_SWITCH,
        MOTION_LIGHT,
        IO_SENSE,
        IO_RELAY,
        KEYPAD_KEY,
        LAMP_MODULE,
        REMOTE_BUTTON,
        APPLIANCE_MODULE,
        DOOR_LOCK_ON,
        DOOR_LOCK_OFF
    }

    public ISYDeviceType getISYDeviceType() {
        return mDeviceType;
    }

    public void setISYDeviceType(ISYDeviceType deviceType) {
        mDeviceType = deviceType;
    }

    public ISYDeviceType getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(ISYDeviceType deviceType) {
        this.mDeviceType = mDeviceType;
    }

    public String getDeviceTypeInternal() {
        return mDeviceTypeInternal;
    }

    @Override
    public void copyFrom(ISYNode isyNode) {
        if(isyNode.getNodeType() != ISYNodeType.DEVICE) {
            throw new IllegalArgumentException();
        }
        super.copyFrom(isyNode);
        ISYDevice device = (ISYDevice) isyNode;
        setDeviceTypeInternal(device.getDeviceTypeInternal());
        setStatus(device.getStatus());
    }

    public void setDeviceTypeInternal(String deviceTypeInternal) {
        mDeviceTypeInternal = deviceTypeInternal;
        updateDeviceType();
    }

    protected void updateDeviceType() {
        if (this.mDeviceTypeInternal.equals("16.1.0.0")) {
            if(this.getNodeAddress().endsWith("1")) {
                this.mDeviceType = ISYDeviceType.MOTION_SENSOR_MOVEMENT;
            } else if(this.getNodeAddress().endsWith("2")) {
                this.mDeviceType = ISYDeviceType.MOTION_SENSOR_LIGHT;
            } else {
                this.mDeviceType = ISYDeviceType.MOTION_SENSOR_LOW_BATTERY;
            }
        } else if (this.mDeviceTypeInternal.equals("1.25.56.0") ||
                   this.mDeviceTypeInternal.equals("1.32.64.0") ||
                   this.mDeviceTypeInternal.equals("2.28.58.0") ||
                   this.mDeviceTypeInternal.equals("1.32.64.79")) {
            this.mDeviceType = ISYDeviceType.LIGHT_SWITCH;
        } else if (this.mDeviceTypeInternal.equals("1.0.56.0")) {
            this.mDeviceType = ISYDeviceType.LAMP_MODULE;
        } else if(this.mDeviceTypeInternal.equals("16.2.0.0")) {
            if (this.getNodeAddress().endsWith("1")) {
                this.mDeviceType = ISYDeviceType.DOOR_SENSOR_SENSE;
            } else {
                this.mDeviceType = ISYDeviceType.DOOR_SENSOR_LOW_BATTERY;
            }
        } else if(this.mDeviceTypeInternal.equals("1.46.65.0")) {
            if(this.getNodeAddress().endsWith("1")) {
                this.mDeviceType = ISYDeviceType.FAN_LIGHT;
            } else {
                this.mDeviceType = ISYDeviceType.FAN_MOTOR;
            }
        } else if(this.mDeviceTypeInternal.equals("2.20.56.0")) {
            this.mDeviceType = ISYDeviceType.INLINE_SWITCH;
        } else if(this.mDeviceTypeInternal.equals("2.20.56.157")) {
            this.mDeviceType = ISYDeviceType.MOTION_LIGHT;
        } else if(this.mDeviceTypeInternal.equals("7.0.54.0")) {
            if(this.getNodeAddress().endsWith("1")) {
                this.mDeviceType = ISYDeviceType.IO_SENSE;
            } else {
                this.mDeviceType = ISYDeviceType.IO_RELAY;
            }
        } else if(this.mDeviceTypeInternal.equals("1.28.54.0") ||
                  this.mDeviceTypeInternal.equals("1.27.65.0")) {
            this.mDeviceType = ISYDeviceType.KEYPAD_KEY;
        } else if(this.mDeviceTypeInternal.equals("0.18.0.0")) {
            this.mDeviceType = ISYDeviceType.REMOTE_BUTTON;
        } else if(this.mDeviceTypeInternal.equals("2.9.66.0")) {
            this.mDeviceType = ISYDeviceType.APPLIANCE_MODULE;
        } else if(this.mDeviceTypeInternal.equals("15.6.66.0")) {
            if(this.getNodeAddress().endsWith("1")) {
                this.mDeviceType = ISYDeviceType.DOOR_LOCK_ON;
            } else {
                this.mDeviceType = ISYDeviceType.DOOR_LOCK_OFF;
            }
        } else {
            this.mDeviceType = ISYDeviceType.UNKNOWN;
        }
    }

    public String getStatus() {
        return this.mStatus;
    }

    public void setStatus(String status) {
        if(status == null) {
            this.mStatus = "";
        } else {
            this.mStatus = status;
        }
    }

    public Integer getOnLevel() {
        return mOnLevel;
    }

    public void setOnLevel(Integer mOnLevel) {
        this.mOnLevel = mOnLevel;
    }

    protected String mDeviceTypeInternal;
    protected ISYDeviceType mDeviceType;
    protected Integer mOnLevel;
    protected String mStatus;
}
