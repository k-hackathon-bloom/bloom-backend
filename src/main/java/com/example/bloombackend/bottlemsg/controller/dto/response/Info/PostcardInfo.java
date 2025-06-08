package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

import lombok.Builder;

@Builder
public record PostcardInfo(
        Long id,
        String url
) {
}
