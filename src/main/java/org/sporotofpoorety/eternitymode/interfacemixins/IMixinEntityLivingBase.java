package org.sporotofpoorety.eternitymode.interfacemixins;


import java.util.ArrayList;

import org.sporotofpoorety.eternitymode.util.LaserData;


public interface IMixinEntityLivingBase
{

    public void syncLaserStateToClient();

    public boolean getHasAfterimages();
    public float getAfterimageScale();
    public ArrayList<LaserData> getLaserBeams();


    public void setHasAfterimages(boolean hasAfterimages);
    public void setAfterimageScale(float scale);
    public void setLaserBeams(ArrayList<LaserData> laserBeams);

}
