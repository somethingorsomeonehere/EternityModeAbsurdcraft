package org.sporotofpoorety.eternitymode.util;


import java.util.ArrayList;

import net.minecraft.util.math.Vec3d;

import org.sporotofpoorety.eternitymode.util.OrthonormalBasis;




public final class DirectionalSpreadUtil 
{

//Make cone of vectors,
//constructed using trigonometry
    public static ArrayList<Vec3d> shootCone(Vec3d adjEnd, double coneAngle, int pointCount)
    {
        ArrayList<Vec3d> conePoints = new ArrayList<>(pointCount);


//Get forward dir and circle step rads
        Vec3d[] forwardBasis = OrthonormalBasis.makeOrthonormalBasis(adjEnd);
        Vec3d orthoRight = forwardBasis[0];
        Vec3d orthoUp = forwardBasis[1];
        Vec3d orthoForward = forwardBasis[2];

        double coneStep = (2.0D * Math.PI) / pointCount;


//Each point in circle as a triangle
        for(int triangleAt = 0; triangleAt < pointCount; triangleAt++)
        {
//Get oppDir from angle
            double oppEndAngle = triangleAt * coneStep;
            Vec3d oppEndDir = orthoRight.scale(Math.cos(oppEndAngle)).add(orthoUp.scale(Math.sin(oppEndAngle)));
//oppEnd = oppDir scaled by tan and adjLen
            Vec3d oppEnd = oppEndDir.scale(Math.tan(coneAngle / 2.0D) * adjEnd.length()); 


//Get hypothenuse end
            Vec3d hypothEnd = adjEnd.add(oppEnd);
      
//Add hypothenuse to cone points
            conePoints.add(hypothEnd);
        }


//Return total cone points
        return conePoints;
    }




//Generate angle spread
    public static ArrayList<Vec3d> fibonacciDirectionalSpread(Vec3d forwardOriginal, int pointCount, double coneEdgeRadians) 
    {
//Array of radial vectors
        ArrayList<Vec3d> radialPopulation = new ArrayList<>(pointCount);

//Normalize original forward direction just in case
        Vec3d forwardNormalized = forwardOriginal.normalize();
//Add original direction
        radialPopulation.add(forwardNormalized);


//Orthonormal basis of the original forward direction
        Vec3d[] forwardBasis = OrthonormalBasis.makeOrthonormalBasis(forwardNormalized);
//Get its axes
        Vec3d orthoRight = forwardBasis[0];
        Vec3d orthoUp = forwardBasis[1];
        Vec3d orthoForward = forwardBasis[2];


//Golden angle approx 137.5 degrees
        final double goldenRadians = (3.0 - Math.sqrt(5.0)) * Math.PI;


//Cosine bounds for cone
        double edgeCosine = Math.cos(coneEdgeRadians);
        double middleCosine = 1.0;


//Now get to placing the points
        for (int projectileAt = 0; projectileAt < pointCount; projectileAt++) 
        {

//Evenly distribute samples in the middle of each stop
            double middleFactor = (projectileAt + 0.5) / pointCount;


//Iterate over cosine space
            double directionCosine =
//From the middle cosine
                middleCosine
//Iterate to edge cosine over middle-points 
                    + ((edgeCosine - middleCosine) * middleFactor);

//For sine you scale down logarithmically as cosine increases
            double directionSine =
                    Math.sqrt(1.0 - directionCosine * directionCosine);


// Rotate around the cone using the golden angle
            double coneAngleAt = projectileAt * goldenRadians;

            double localX = Math.cos(coneAngleAt) * directionSine;
            double localY = Math.sin(coneAngleAt) * directionSine;
            double localZ = directionCosine;


// Transform from local cone space to world space
            Vec3d worldDirection =
                orthoRight.scale(localX)
                .add(orthoUp.scale(localY))
                .add(orthoForward.scale(localZ))
                .normalize();


//Add direction to radial population
            radialPopulation.add(worldDirection);
        }


        return radialPopulation;
    }

}
