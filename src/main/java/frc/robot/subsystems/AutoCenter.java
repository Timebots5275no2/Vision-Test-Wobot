// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.lang.constant.DirectMethodHandleDesc;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.Vector;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AutoCenter extends SubsystemBase {
  /** Creates a new AutoCenter. */

  private Drivetrain centerDrivetrain;
  private double[] values;
  private int id;
  private double tx;
  private int count;
  private int co;


  private double d;
  private double t;
  private double index5;
  private double Vr;
  private boolean finished;
  private boolean initialized;
  private double xSpeed;
  private double ySpeed;
  private double rotSpeed;
  private double workingAveR;
  private ArrayList<Double> aveNumsR = new ArrayList<Double>();
  private double workingAveX;
  private ArrayList<Double> aveNumsX = new ArrayList<Double>();
  private double workingAveZ;
  private ArrayList<Double> aveNumsZ = new ArrayList<Double>();


  public AutoCenter(Drivetrain drivetrain) {
    centerDrivetrain = drivetrain;
    values = new double[6]; // Array meaning pos x, y, z, then rot x, y, z
    id = -1;
    tx=0;
    count = 0;
    d = 0;
    t = 0;
    index5 = 0;
    Vr = 0;
    finished = false;
    initialized = false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    values = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_robotspace").getDoubleArray(new double[6]);
    id = (int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0.0);
    tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    if(id!=-1){
      if(co>=3){
        //System.out.println(co);
        decAvs();
        aveNumsX.clear();;
        aveNumsZ.clear();
        aveNumsR.clear();
        //System.out.println(workingAveX);
        //System.out.println(workingAveZ);
        //System.out.println(workingAveR);
        co=0;
      } else if(co==1){
        workingAveX=values[0];
        workingAveZ=values[2];
        workingAveR=values[4];
      }
      co++;
      
      addAveVal(values);
    }
    
     if(count >= 50) {
       for(int i = 0; i < 6; i++) {
         if (i == 0) {printValue(i, "xPos");} else if(i == 1) {printValue(i, "yPos");} else if (i == 2){printValue(i, "zPos");}
         count = 0;
       }
       }
       else {
         count++;
       }
    }

     public void printValue(int i, String poop) {
         //System.out.println(poop + " " + values[i]);
     }

public void decAvs(){
  workingAveX = updateList(aveNumsX);
  workingAveZ = updateList(aveNumsZ);
  workingAveR = updateList(aveNumsR);
}

public Double updateList(ArrayList<Double> list){
  double min=list.get(0);
  double max=list.get(0);
  for(int i=1; i<list.size(); i++){
    if(list.get(i)<min){
      min=list.get(i);
    } else if(list.get(i)>max){
      max=list.get(i);
    }
  }
  list.remove(list.indexOf(min));
  list.remove(list.indexOf(max));
  //System.out.println(list.size()+"size");
  double sum=0;
  for(int i=0; i<list.size(); i++){
    sum+=list.get(i);
  }
  //System.out.println(min+" <min max> "+max);
  return (sum/list.size());
}

public void addAveVal(double[] values2){
  aveNumsX.add(values2[0]);
  aveNumsZ.add(values2[2]);
  aveNumsR.add(values2[4]);
}






















    public void initialize() {
      //d = (2 * Math.PI * (workingAveZ - 0.381)) * (index5 / 360);
      //t = d / 0.1;
      //Vr = (index5 * (Math.PI / 180)) / t;

      
    }

//neg rot rots right
//neg y moves left
   public boolean centerDrive() {
    double ySpeedcon=.5;
    double rotSpeedcon=.5;
    if (id == -1 || workingAveR==0) {xSpeed=0;ySpeed=0;rotSpeed=0;} else {
      int xNeg;
      if(workingAveX<0){
        xNeg=-1;
      } else {
        xNeg=1;
      }
      int rotNeg;
      if(workingAveR<0){
        rotNeg=-1;
      } else {
        rotNeg=1;
      }
      finished=false;
      //if(workingAveZ>1) {
      //  xSpeed=.5*(workingAveZ);
      //} else if(workingAveZ<.75){
      //  xSpeed=-.5*(workingAveZ*1.25);
      //} else {
      //  xSpeed=0;
      //}
      if((workingAveX>-.1 && workingAveX<.1)&&(workingAveR>-3 && workingAveR<3)) {
        centerDrivetrain.drive(0, 0, 0, false);
        return true;
      //} else if(workingAveX<.1 && workingAveX>-.1){
      //  double per = workingAveR/360;
      //  double dis = workingAveZ*per;
      //  ySpeed = 
      } else if(workingAveR>-10 && workingAveR<10 && (workingAveX>.01 || workingAveX<-.01)){
        ySpeed=-ySpeedcon*((Math.pow(workingAveX+xNeg,2)-1)*xNeg);
        System.out.print("lin");
      } else if(workingAveR<0 && workingAveX > 0) {
        ySpeed=-ySpeedcon*(Math.abs(workingAveR/15));
        rotSpeed=-rotSpeedcon*(Math.pow(workingAveX+xNeg,2)-1);
        System.out.print("-+");
      } else if(workingAveR<0 && workingAveX < 0) {
        ySpeed=-ySpeedcon*(Math.abs(workingAveR/30));
        rotSpeed=rotSpeedcon*(Math.pow(workingAveX+xNeg,2)-1);
        System.out.print("--");
      } else if(workingAveR>0 && workingAveX>0) {
        ySpeed=ySpeedcon*(Math.abs(workingAveR/15));
        rotSpeed=-rotSpeedcon*(Math.pow(workingAveX+xNeg,2)-1);
        System.out.print("++");
      } else if(workingAveR>0 && workingAveX<0) {
        ySpeed=ySpeedcon*(Math.abs(workingAveR/15));
        rotSpeed=rotSpeedcon*(Math.pow(workingAveX+xNeg,2));
        System.out.print("+-");
      }
    }
    System.out.println("x: " + xSpeed);
    System.out.println("y: " + ySpeed);
    System.out.println("Rot: " + rotSpeed); //add to suflebord
    centerDrivetrain.drive(xSpeed, ySpeed, rotSpeed, false);
    return false;
   }

   public void autoMove(){
    double conePosZ = 2;
    double conePosX = 0;
    double Zgap = .5;
    double Xgap = .5;
    double startPos = conePosZ + Zgap;
    System.out.print("POOPPPPPPP");
   }

  }

    //public void centerDrive() {
    //  if (id == -1) {xSpeed=0;ySpeed=0;rotSpeed=0;} else {
    //  finished=false;
    //  if ((tx > 10 || tx < -10)) {
    //    faceTag();
    //  } else {
    //     if (workingAveX > 0.08) {
    //      // centerDrivetrain.drive(0, -0.3, 0, false); // moves left
    //      ySpeed=-.75*(workingAveX/2);
    //      System.out.println("Moving negative");
    //     }
    //     else if (workingAveX < -0.08){
    //       //centerDrivetrain.drive(0, 0.3, 0, false); // moves right
    //       ySpeed=.75*(workingAveX/2);
    //       System.out.println("Moving positive");
    //     } else if(workingAveX> -.08 && workingAveX<.08 && id > 0) {
    //       //centerDrivetrain.drive(0, 0, 0, false);
    //       xSpeed=0;
    //       ySpeed=0;
    //       rotSpeed=0;
    //       //find way to reset finished to false for a second use
    //     }
    //    
    //     if(!initialized) {initialize();}
    //    }
    //    }
    //    centerDrivetrain.drive(xSpeed, ySpeed, rotSpeed, false);
    //  }






 //   public void faceTag(){
 //     if(workingAveR > 5){
 //       //centerDrivetrain.drive(0, 0,-.35, false);
 //       rotSpeed=-0.35*(2/(workingAveZ));
 //     } else if(workingAveR < -5){
 //       //centerDrivetrain.drive(0, 0,.35, false);
 //       rotSpeed=0.35*(2/(workingAveZ));
 //     } else if(workingAveR < 5 && workingAveR > -5){
 //       //centerDrivetrain.drive(0, 0, 0, false);
 //       rotSpeed=0;
 //     }
 //   }
//
 //   public boolean isCenter() {
 //     if((workingAveX < 0.08 && workingAveX > -0.06) && (workingAveR < 3 && workingAveR > -3)) {
 //       return true;
 //     } else {
 //       return false;
 //     }
//
 //   }
 // }
