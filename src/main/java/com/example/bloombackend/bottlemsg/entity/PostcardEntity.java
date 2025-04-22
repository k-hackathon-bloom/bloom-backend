package com.example.bloombackend.bottlemsg.entity;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.PostcardInfo;
import com.example.bloombackend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "postcard")
public class PostcardEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postcard_id")
    private Long id;

    @Getter
    @Column(name = "poster_img_url")
    private String postcardUrl;

    @Builder
    public PostcardEntity(String postcardUrl) {
        this.postcardUrl = postcardUrl;
    }

    public PostcardInfo getPostcardInfo() {
        return PostcardInfo.builder()
                .id(id)
                .url(postcardUrl)
                .build();
    }
}
