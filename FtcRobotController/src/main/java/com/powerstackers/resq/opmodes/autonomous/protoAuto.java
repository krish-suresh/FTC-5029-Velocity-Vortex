package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.RobotAuto;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Derek on 1/14/2016.
 */
public class protoAuto extends LinearOpMode {

    /*Color Values
     *
     */
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

    @Override
    public void runOpMode() throws InterruptedException {



        /*
         * Motors
         */
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        RobotAuto.motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitForStart();

        while (RobotAuto.enRightPosition > RobotAuto.EnRightS1 || RobotAuto.enLeftPosition > RobotAuto.EnLeftS1) {
            RobotAuto.enLeftPosition = RobotAuto.motorBLeft.getCurrentPosition();
            RobotAuto.enRightPosition = RobotAuto.motorBRight.getCurrentPosition();
            RobotAuto.motorBrush.setPower(1);
            RobotAuto.motorFRight.setPower(RobotAuto.EnRightpower);
            RobotAuto.motorBRight.setPower(RobotAuto.EnRightpower);
            RobotAuto.motorFLeft.setPower(RobotAuto.EnLeftpower);
            RobotAuto.motorBLeft.setPower(RobotAuto.EnLeftpower);
            telemetry.addData("EncoderL", "Value: " + String.valueOf(RobotAuto.motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderR", "Value: " + String.valueOf(RobotAuto.motorBRight.getCurrentPosition()));

//            if (RobotAuto.enLeftPosition > RobotAuto.EnLeftS1 && RobotAuto.enRightPosition > RobotAuto.EnRightS1) {
//
//                RobotAuto.motorBLeft.setPower(0);
//                RobotAuto.motorBRight.setPower(0);
//
//            }

        }
        RobotAuto.motorBrush.setPower(0);
        RobotAuto.motorBLeft.setPower(0);
        RobotAuto.motorBRight.setPower(0);
        RobotAuto.motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }
}
