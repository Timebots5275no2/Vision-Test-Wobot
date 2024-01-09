// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AutoCenter;
import frc.robot.subsystems.Drivetrain;

public class FOKhghui extends Command {

  private AutoCenter jfeui;
  /** Creates a new FOKhghui. */
  public FOKhghui(AutoCenter balls, Drivetrain ok) {
    // Use addRequirements() here to declare subsystem dependencies.
    jfeui = balls;
    addRequirements(jfeui);
    addRequirements(ok);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    jfeui.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      jfeui.centerDrive();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
