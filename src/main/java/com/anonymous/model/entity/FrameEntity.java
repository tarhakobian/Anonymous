package com.anonymous.model.entity;

import jakarta.persistence.*;
import lombok.Data;
/**
 * Author : Taron Hakobyan
 */
@Data
@Entity
@Table(name = "frames")
public class FrameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "times_used")
    private Integer timesUsed;
}
