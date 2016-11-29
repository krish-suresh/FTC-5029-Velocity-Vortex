/*
 * Copyright (C) 2016 Powerstackers
 *
 * Teleop code for Velocity Vortex.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.powerstackers.velocity.common;

import com.powerstackers.velocity.common.enums.PublicEnums;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import static com.powerstackers.velocity.common.VelRobotConstants.BEACON_RESTING;
import static java.lang.Math.PI;

/**
 * Basic configurations for our robot in autonomous mode. All the functionality of a teleop bot,
 * and more!
 * 
 * @author Derek Helm
 */

public class VelRobotAuto extends VelRobot {

    /**
     * Reduction on motor gearbox.
     */
    private static final double gearbox = 40;
    /**
     * Stores the number of encoder ticks in one motor revolution.
     * For AndyMark Neverest 40's, it's 1120. The encoder tick count is actually 7 pulses per
     * revolution of the encoder disk, with 4 revolutions per cycle. Since the gearbox increases the
     * number of rotations by a factor of 40, the final count is 7 * 4 * 40 = 1120. For 20 or 60
     * reduction motors, the  number would be different.
     */
    private static final double ticksPerRevolution = 28 * gearbox;
    /**
     * Motor diameter in centimeters.
     */
    private static final double wheelDiameter = 10.0;
    /**
     * Gear ratio between the motor and the drive wheels. Used in calculating distance.
     */
    private static final double driveGearMultiplier = 1.5;
//    double turnOvershootThreshold = 0.1;

    LinearOpMode mode;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public VelRobotAuto(LinearOpMode mode) {
        super(mode);
    }

    /**
     * Tap the beacon on the correct side.
     * @param allianceColor The color that we are currently playing as.
     */
    public void tapBeacon(PublicEnums.AllianceColor allianceColor) {
        PublicEnums.AllianceColor dominantColor;
        double positionBeaconServo;

        // Detect the color shown on the beacon's left half, and record it.
        if (sensorColor.red() > sensorColor.blue()) {
            dominantColor = PublicEnums.AllianceColor.RED;
        } else {
            dominantColor = PublicEnums.AllianceColor.BLUE;
        }

        // Tap the correct side based on the dominant color.
        if (dominantColor == allianceColor) {
            positionBeaconServo = VelRobotConstants.BEACON_TAP_LEFT;
        } else {
            positionBeaconServo = VelRobotConstants.BEACON_TAP_RIGHT;
        }

        // Trim the servo value and set the servo position.
        positionBeaconServo = trimServoValue(positionBeaconServo);
        servoBeacon.setPosition(positionBeaconServo);
    }

    /**
     * Turn the robot a certain number of degrees from center.
     * @param degrees Number of DEGREES to turn. Positive is counterclockwise, negative is clockwise.
     * @param speed Speed at which to turn.
     * @throws InterruptedException Make sure that we don't get trapped in this method when interrupted.
     */
    void turnDegrees(double degrees, double speed) throws InterruptedException {

        double degreesSoFar = getGyroHeading();

        if (degrees > 180) {
//            robot.setPowerLeft(-1 * speed);
//            robot.setPowerRight(speed);
            mode.telemetry.addData("gyro1", getGyroHeading());
        } else if (degrees < 180 || degrees == 180) {
//            robot.setPowerLeft(speed);
//            robot.setPowerRight(-1 * speed);
            mode.telemetry.addData("gyro2", getGyroHeading());
        } else {
//            robot.setPowerAll(0);
        }
        mode.telemetry.addData("Gyro", degrees + "," + degreesSoFar);
        // For as long as the current degree measure doesn't equal the target. This will work in the clockwise and
        // counterclockwise directions, since we are comparing the absolute values
        while ((degreesSoFar) < (degrees)) {
            mode.telemetry.addData("gyrocompare", degreesSoFar=getGyroHeading());
        }

        // Stop all drive motors
//        robot.setPowerAll(0);
    }

    /**
     * Move the robot across the playing field a certain distance.
     * Indicating a negative speed or distance will cause the robot to move in reverse.
     * @param  distance The distance that we want to travel, in centimeters.
     * @param angle The angle to move at, in radians.
     * @param  speed The speed at which to travel.
     */
    public void goDistanceInCm(double distance, double angle, double speed) {
        // TODO Only goes forward, can't strafe.
        zeroEncoders();
        setMovement(angle, speed, 0.0);
        while(motorDrive1.getCurrentPosition() < cmToTicks(distance)) {}
        stopMovement();
    }

    /**
     * Reset the encoders on all motors.
     */
    public void zeroEncoders() {
        motorDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDrive3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDrive4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        try {
            mode.idle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        motorDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDrive2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDrive3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDrive4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /**
     * Converts a distance in encoder ticks to a distance in inches.
     * <p>We calculate this by taking the number of ticks traveled, divided by the number of ticks
     * per revolution, and then multiplied by the gear ratio multiplier to get the number of wheel
     * rotations. Multiply one more time by the circumference of the wheels (PI*wheelDiameter).
     * @param  ticks long representing the distance in ticks.
     * @return       that distance in inches.
     */
    public static double ticksToCm(int ticks) {
        // TODO This is wrong.
        return (ticks/ticksPerRevolution)*driveGearMultiplier*(PI*wheelDiameter);
    }

    /**
     * Converts a distance in inches to a distance in encoder ticks.
     * <p>We calculate this by taking the number of wheel rotations (inches/(PI*wheelDiameter))
     * multiplied by the inverse of the gear ratio, to get the number of motor rotations. Multiply
     * one more time by the number of motor encoder ticks per one motor revolution.
     * @param  inches double containing the distance you want to travel.
     * @return        that distance in encoder ticks.
     */
    public static int cmToTicks(double inches) {
        // TODO This is wrong now.
        return (int) ((1/driveGearMultiplier)*ticksPerRevolution*(inches/(PI*wheelDiameter)));
    }
    
    /**
     * Trim a servo value between the minimum and maximum ranges.
     * @param servoValue Value to trim.
     * @return A raw double with the trimmed value.
     */
    private static double trimServoValue(double servoValue) {
        return Range.clip(servoValue, 0.0, 1.0);
    }

    public long getLeftEncoder() {
        return motorDrive1.getCurrentPosition();
    }

    public long getRightEncoder() {
        return motorDrive2.getCurrentPosition();
    }

    public double getGyroHeading() {
        return  sensorGyro.getHeading();
    }

    public void calibrateGyro() {
        sensorGyro.calibrate();
    }

    public  boolean isGyrocalibrate() {
        return sensorGyro.isCalibrating();
    }

    public double getrawXGyro() {
        return sensorGyro.rawX();
    }

    public double getrawYGyro() {
        return sensorGyro.rawY();
    }

    public double getrawZGyro() {
        return sensorGyro.rawZ();
    }

    public OpMode getParentOpMode() {
        return mode;
    }

}
