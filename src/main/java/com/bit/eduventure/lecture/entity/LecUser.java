package com.bit.eduventure.lecture.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "T_LECTURE_USER")
public class LecUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int lectureId;

    int userNo;


}
