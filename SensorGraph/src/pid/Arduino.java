/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid;

import com.fazecast.jSerialComm.SerialPort;
import frames.ProgressDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ismael
 */
public class Arduino implements Closeable{
    private final SerialPort port;
    private final BufferedReader in;
    private ScheduledExecutorService service;
    private ArduinoListener listener;
    
    public Arduino(SerialPort port) {
        if(!port.isOpen()) ProgressDialog.doBackground(() -> {
            port.openPort();
        });
        
        this.port = port;
        in = new BufferedReader(new InputStreamReader(this.port.getInputStream()));
    }
    
    public boolean isOpen(){
        return this.port.isOpen();
    }
    
    @Override
    public void close() throws IOException {
        this.send("SHUTDOWN\n");
        this.in.close();
        this.port.closePort();
    }
    
    public void send(String send){
        byte s[] = send.getBytes();
        this.port.writeBytes(s, s.length);
    }
    
    public interface ArduinoListener{
        public void onReceive(float received);
    }

    public void setListener(ArduinoListener listener) {
        this.listener = listener;
        if(listener != null){
            if(service == null){
                service = Executors.newSingleThreadScheduledExecutor();
            }
            service.scheduleAtFixedRate(new VerifyIncomming(), 0, 100, TimeUnit.MILLISECONDS);
        }else{
            service.shutdownNow();
            service = null;
        }
    }
    
    private class VerifyIncomming implements Runnable{
        @Override
        public void run() {
            try {                
                if(in.ready()){
                    if(listener != null){
                        String read = in.readLine();
                        try{
                            listener.onReceive(Float.parseFloat(read));   
                        }catch(NumberFormatException ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            } catch (IOException ex) {
                
            }
        }
        
    }
    
}
