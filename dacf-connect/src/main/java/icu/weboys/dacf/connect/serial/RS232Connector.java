package icu.weboys.dacf.connect.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import icu.weboys.dacf.core.ObjectContainer;
import icu.weboys.dacf.core.abs.AbsConnector;
import icu.weboys.dacf.core.annotation.DACFConnector;
import icu.weboys.dacf.core.info.ModuleInfo;
import icu.weboys.dacf.core.util.BaseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.PortInUseException;

@Log4j2
@DACFConnector
public class RS232Connector extends AbsConnector<byte[]>{
    protected String comName;
    protected    int baudrate;
    protected    int databits;
    protected    int   parity;
    protected    int stopbits;
    protected    int  timeout;
    protected SerialPort serialPort;
    @Override
    public void init(String name) {
        super.init(name);
        ModuleInfo mdi = ObjectContainer.get(name);
        this.comName  = BaseUtils.getMapValue(mdi.getParams(),"comName","COM1");
        this.baudrate = BaseUtils.getMapValue(mdi.getParams(),"baudrate", 9600);
        this.databits = BaseUtils.getMapValue(mdi.getParams(),"databits",    8);
        this.  parity = BaseUtils.getMapValue(mdi.getParams(),"parity",      0);
        this.stopbits = BaseUtils.getMapValue(mdi.getParams(),"stopbits",    1);
        if(ObjectContainer.get(this.name).getEnable()){
            this.connect();
            return;
        }
    }

    @Override
    public void connect() {
       try{
           this.serialPort = SerialPort.getCommPort(this.comName);
           this.serialPort.setBaudRate(this.baudrate);
           this.serialPort.setParity(this.parity);
           this.serialPort.setNumDataBits(this.databits);
           this.serialPort.setNumStopBits(this.stopbits);
           this.serialPort.openPort(this.timeout);
           this.serialPort.addDataListener(new SerialPortDataListener() {
               @Override
               public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
               @Override
               public void serialEvent(SerialPortEvent event)
               {
                   if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                       return;
                   byte[] newData = new byte[serialPort.bytesAvailable()];
                   serialPort.readBytes(newData, newData.length);
                   recv(newData);
               }
           });
           super.connect();
       } catch( PortInUseException e) {
           log.error(String.format("[%s] Serial port %s occupied",this.name,this.comName) );
       }catch (Exception e) {
           log.error(String.format("[%s] Serial port %s initialization failed, please check the configuration",this.name,this.comName) );
       }
    }

    @Override
    public void close() {
        if( this.serialPort != null ) {this.serialPort.closePort(); this.serialPort = null;}
        super.close();
    }

    @Override
    public void send(byte[] data) {
        this.serialPort.writeBytes(data,data.length);
    }

}
