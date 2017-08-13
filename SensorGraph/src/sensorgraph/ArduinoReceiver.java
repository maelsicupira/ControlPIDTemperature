/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensorgraph;

/**
 *
 * @author ntbra
 */
public class ArduinoReceiver {
    private int sensorValue;
    private int pidValue;

    public void setPidValue(int pidValue) {
        this.pidValue = pidValue;
    }

    public void setSensorValue(int sensorValue) {
        this.sensorValue = sensorValue;
    }

    public int getPidValue() {
        return pidValue;
    }

    public int getSensorValue() {
        return sensorValue;
    }
    
}
