// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.lang.constant.DirectMethodHandleDesc;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AutoCenter extends SubsystemBase {
  /** Creates a new AutoCenter. */

  private Drivetrain centerDrivetrain;
  private double[] values;
  private int id;
  private double tx;
  private int count;


  private double d;
  private double t;
  private double index5;
  private double Vr;
  private boolean finished;
  private boolean initialized;
  private double xSpeed;
  private double ySpeed;
  private double rotSpeed;


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
    index5 = values[4];
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
























    public void initialize() {
      //d = (2 * Math.PI * (values[2] - 0.381)) * (index5 / 360);
      //t = d / 0.1;
      //Vr = (index5 * (Math.PI / 180)) / t;
      index5 = values[4];

      
    }





    public void centerDrive() {
      if (id == -1) {xSpeed=0;ySpeed=0;rotSpeed=0;} else {
      finished=false;
      if ((tx > 10 || tx < -10)) {
        faceTag();
      } else {
      System.out.print(isCenter());
         if (values[0] > 0.08&&values[4]>5) {
          // centerDrivetrain.drive(0, -0.3, 0, false); // moves left
          ySpeed=-.3;
          System.out.println("Moving negative");
         }
         else if (values[0] < -0.08){
           //centerDrivetrain.drive(0, 0.3, 0, false); // moves right
           ySpeed=.3;
           System.out.println("Moving positive");
         } else if(values[0]> -.08 && values[0]<.08 && id > 0) {
           //centerDrivetrain.drive(0, 0, 0, false);
           xSpeed=0;
           ySpeed=0;
           rotSpeed=0;
           //find way to reset finished to false for a second use
         }
        
         if(!initialized) {initialize();}
        }
        }
        centerDrivetrain.drive(xSpeed, ySpeed, rotSpeed, false);
      }






    public void faceTag(){
      xSpeed=0;
      ySpeed=0;
      if(values[4] > 5){
        //centerDrivetrain.drive(0, 0,-.35, false);
        rotSpeed=-0.35*(2/(values[2]+.001));
      } else if(values[4] < -5){
        //centerDrivetrain.drive(0, 0,.35, false);
        rotSpeed=0.35*(2/(values[2]+.001));
      } else if(values[4] < 5 && values[4] > -5){
        //centerDrivetrain.drive(0, 0, 0, false);
        rotSpeed=0;
      }
    }

    public boolean isCenter() {
      if((values[0] < 0.08 && values[0] > -0.06) && (values[4] < 3 && values[4] > -3)) {
        return true;
      } else {
        return false;
      }

    }
  }
