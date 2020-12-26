package com.NIO.Server.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "devices")
public class DeviceProfile {

//     public class Device{
//         private String cameraip;
//         private String motoip;
//
//         public String getMotoip() {
//             return motoip;
//         }
//
//
//         public String getCameraip() {
//             return cameraip;
//         }
//
//     }
     private List<Map<String,String>> devicelist;
//     private List<Device> devicelist;
    private List<String> list;

    public void setDevicelist(List<Map<String, String>> devicelist) {
        this.devicelist = devicelist;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }


    public List<Map<String, String>> getDevicelist() {
        return devicelist;
    }



//    public List<Device> getDevicelist() {
//        return devicelist;
//    }
//
//    public void setDevicelist(List<Device> devicelist) {
//        this.devicelist = devicelist;
//    }
}
