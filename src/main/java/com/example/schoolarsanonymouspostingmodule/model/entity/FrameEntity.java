package com.example.schoolarsanonymouspostingmodule.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "frames")
public class FrameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "times_used")
    private Integer timesUsed;
}
