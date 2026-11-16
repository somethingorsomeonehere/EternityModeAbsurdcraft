package org.sporotofpoorety.eternitymode.util;

import java.util.ArrayList;


//Will be very useful going forward to schedule entity actions
public class QueuedActionAtPos 
{
    public final double actionX, actionY, actionZ;
    public final long actionTick;
    public final int actionType;
    public double actionScale;
    public ArrayList<Integer> actionMeta = new ArrayList();

    public QueuedActionAtPos(double actionX, double actionY, double actionZ, long actionTick, int actionType) 
    {
        this.actionX = actionX;
        this.actionY = actionY;
        this.actionZ = actionZ;
        this.actionTick = actionTick;
        this.actionType = actionType;
    }
}
