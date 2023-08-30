package com.bit.eduventure.lecture.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LecUser {

    @Id
    int id;

    int lectureId;

    int userNo;


}
