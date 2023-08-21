package com.bit.eduventure.ES2_GPS.Repository;


import com.bit.eduventure.ES2_GPS.Entity.DriverPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverPhotoRepository extends JpaRepository<DriverPhoto, Long> {
    @Query(value = "SELECT * FROM driverphoto WHERE carnumber = :carnumber ORDER BY id DESC LIMIT 1", nativeQuery = true)
    DriverPhoto findLatestPhoto(@Param("carnumber") Integer carnumber);


}
