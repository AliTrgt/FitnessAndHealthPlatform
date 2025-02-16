package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.CommentDTO;
import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.exception.CommentNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Comment;
import com.example.HealthAndFitnessPlatform.repository.CommentRepository;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DTOConverter dtoConverter;

    public CommentService(CommentRepository commentRepository, DTOConverter dtoConverter) {
        this.commentRepository = commentRepository;
        this.dtoConverter = dtoConverter;
    }

    public List<CommentDTO> getAllComments(){
            return commentRepository.findAll()
                    .stream()
                    .map(dtoConverter::convertToCommentDTO)
                    .collect(Collectors.toList());
    }

    public CommentDTO findById(int commentId){
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id can not found : "+commentId));
            return dtoConverter.convertToCommentDTO(comment);
    }

    public CommentDTO createComment(Comment comment){
            Comment tempComment = commentRepository.save(comment);
            return dtoConverter.convertToCommentDTO(tempComment);
    }

    public CommentDTO updateComment(int commentId,Comment comment){
            Comment lastComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id not found : "+commentId));

            if (comment.getContent() != null && !comment.getContent().trim().isEmpty()) {
                lastComment.setContent(comment.getContent());
            }

            return dtoConverter.convertToCommentDTO(commentRepository.save(lastComment));
    }

    public void deleteComment(int commentId){
            Comment comment  = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment id not found : "+commentId));
            commentRepository.delete(comment);
    }


}
