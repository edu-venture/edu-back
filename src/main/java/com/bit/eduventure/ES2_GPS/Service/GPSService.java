package com.bit.eduventure.ES2_GPS.Service;

import com.bit.eduventure.ES2_GPS.Repository.GPSRepository;
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
