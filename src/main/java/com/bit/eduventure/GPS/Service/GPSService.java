package com.bit.eduventure.GPS.Service;

import com.bit.eduventure.GPS.Repository.GPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GPSService {

    private GPSRepository gpsRepository;

    @Autowired
    public GPSService(GPSRepository gpsRepository){
        this.gpsRepository = gpsRepository;
    }


}
