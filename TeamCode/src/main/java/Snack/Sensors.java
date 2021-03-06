package Snack;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

public class Sensors {

    //gyro
    public BNO055IMU gyro;
    Orientation angles;
    Acceleration gravity;
    BNO055IMU.Parameters parameters;

    public Sensors(LinearOpMode opMode) throws InterruptedException{
        //gyro parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        gyro = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        gyro.initialize(parameters);
    }

    public void updateValues(){
        angles = gyro.getAngularOrientation();
    }

    public boolean resetGyro(){
        return gyro.initialize(parameters);
    }

    //returns positive angle to turn right & negative angle to turn left
    public double angleDiff(double goalAngle) {
        double currAngle = gyroYaw();
        if (currAngle >= 0 && goalAngle >= 0 || currAngle <= 0 && goalAngle <= 0) { //curr & goal are both positive or both negative
            return -(currAngle - goalAngle);
        } else if (Math.abs(currAngle - goalAngle) <= 180) {//diff btwn curr & goal is less than or equal to 180
            return -(currAngle - goalAngle);
        } else if (currAngle > goalAngle) {//curr is greater than goal
            return (360 - (currAngle - goalAngle));
        } else {//goal is greater than curr
            return -(360 + (currAngle - goalAngle));
        }
    }

    //gyro method to get yaw
    public double gyroYaw(){
        updateValues();
        double yaw = angles.firstAngle * -1;
        if (angles.firstAngle < -180){
            yaw -= 360;
        }
        return yaw;
    }

    //gyro method to get pitch
    public double gyroPitch(){
        updateValues();
        double pitch = angles.secondAngle;
        return pitch;
    }

    //gyro method to get roll
    public double gyroRoll(){
        updateValues();
        double roll = angles.thirdAngle;
        return roll;
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
