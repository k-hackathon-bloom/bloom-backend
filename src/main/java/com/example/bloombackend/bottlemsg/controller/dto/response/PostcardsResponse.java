package com.example.bloombackend.bottlemsg.controller.dto.response;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.PostcardInfo;

import java.util.List;

public record PostcardsResponse(
        List<PostcardInfo> postcards
) {
}
