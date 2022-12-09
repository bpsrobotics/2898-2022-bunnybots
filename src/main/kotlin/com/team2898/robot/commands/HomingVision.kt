package com.team2898.robot.commands

import com.bpsrobotics.engine.utils.`M/s`
import com.bpsrobotics.engine.utils.Sugar.clamp
import com.team2898.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team2898.robot.subsystems.Vision
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import kotlin.math.atan2

/** Robot moves within 1 meter of target, assuming Apriltag is within visual range of camera */
class HomingVision : CommandBase() {
    override fun execute() {
        // Makes sure the robot is not closer than 1 meter
        if (Vision.magnitude2D > 1) {
            // Throttle reduces as it gets closer to target
            val throttle = (Vision.magnitude2D/10).clamp(0.5,1.0)
            val speeds = DifferentialDrive.curvatureDriveIK(throttle, atan2(Vision.xdist, Vision.zdist) / -3.0, true)
            Drivetrain.stupidDrive(`M/s`(speeds.left * -5), `M/s`(speeds.right * -5))
        } else {
            Drivetrain.rawDrive(0.0,0.0) // Full stop
        }
    }

    override fun isFinished(): Boolean {
        return Vision.magnitude2D <= 1 //Checks if is closer than 1 meter
    }

    override fun end(interrupted: Boolean) {
        Drivetrain.rawDrive(0.0,0.0) // Full stop
    }
}