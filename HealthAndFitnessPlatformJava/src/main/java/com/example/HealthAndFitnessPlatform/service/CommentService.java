package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.CommentDTO;
import com.example.HealthAndFitnessPlatform.exception.CommentNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Comment;
import com.example.HealthAndFitnessPlatform.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    public List<CommentDTO> getAllComments(){
        List<Comment> commentList = commentRepository.findAll();
        return commentList.isEmpty() ? Collections.emptyList() : commentList
                    .stream()
                    .map(comment -> modelMapper.map(comment,CommentDTO.class))
                    .collect(Collectors.toList());
    }

    public CommentDTO findById(int commentId){
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id can not found : "+commentId));
            return modelMapper.map(comment,CommentDTO.class);
    }

    public CommentDTO createComment(CommentDTO comment){
            Comment firstComment = modelMapper.map(comment,Comment.class);
            Comment tempComment = commentRepository.save(firstComment);
            return modelMapper.map(tempComment,CommentDTO.class);
    }

    @Transactional
    public CommentDTO updateComment(int commentId,CommentDTO comment){
            Comment lastComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id not found : "+commentId));

            if (comment.getContent() != null && !comment.getContent().trim().isEmpty()) {
                lastComment.setContent(comment.getContent());
            }

            return modelMapper.map(lastComment,CommentDTO.class);
    }

    public void deleteComment(int commentId){
            Comment comment  = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id not found : "+commentId));
            commentRepository.delete(comment);
    }


}
