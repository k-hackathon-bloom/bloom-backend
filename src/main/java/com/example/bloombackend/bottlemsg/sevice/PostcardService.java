package com.example.bloombackend.bottlemsg.sevice;

import com.example.bloombackend.bottlemsg.controller.dto.response.PostcardsResponse;
import com.example.bloombackend.bottlemsg.entity.PostcardEntity;
import com.example.bloombackend.bottlemsg.repository.PostcardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostcardService {
    private final PostcardRepository postcardRepository;


    public PostcardService(PostcardRepository postcardRepository) {
        this.postcardRepository = postcardRepository;
    }

    @Transactional(readOnly = true)
    public PostcardsResponse getAllPostcards(){
        List<PostcardEntity> postcards = getAllPostcardEntities();
        return new PostcardsResponse(
                postcards.stream().map(PostcardEntity::getPostcardInfo).toList()
        );
    }
    public String getPostcardUrl(Long postcardId) {
        return getPostcardEntity(postcardId).getPostcardUrl();
    }

    public List<PostcardEntity> getAllPostcardEntities() {
        return postcardRepository.findAll();
    }

    public PostcardEntity getPostcardEntity(Long postcardId) {
        return postcardRepository.findById(postcardId).orElseThrow();
    }
}
