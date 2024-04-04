package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository; // 댓글 리파지터리 객체 주입
    @Autowired
    private ArticleRepository articleRepository; // 게시글 리파지터리 객체 주입


    public List<CommentDto> comments(Long articleId) {

        // 1. 댓글 조회
        // 2. 엔티티 -> DTO 반환
        // 3. 결과 반환
        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment-> CommentDto.createCommentDto(comment))
                .collect(Collectors.toList()); // 스트림 데이터를 리스트 자료형으로 변환
    }

    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {
        // 1. 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! " + " 대상 게시글이 없습니다."));
        // 2. 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article);
        // 3. 댓글 엔티티를 DB에 저장
        Comment created = commentRepository.save(comment);
        // 4. DTO 로 변환해 반환
        return CommentDto.createCommentDto(created);
    }

    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("댓글 수정 실패!! " + " 대상 댓글이 없습니다!!"));
        // 2. 가져온 댓글 수정하기
        target.patch(dto);
        // 3. 수정한 댓글을 DB에 갱신하기 (덮어쓰기)
        Comment updated = commentRepository.save(target);
        // 4. DTO 로 변환해 반환
        return CommentDto.createCommentDto(updated);

    }
    @Transactional
    public CommentDto delete(Long id) {
        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("댓글 삭제 실패!!" + "대상이 없습니다!!"));
        // 2. 댓글 삭제
        commentRepository.delete(target);
        // 3. 삭제 댓글을 DTO 로 변환 및 반환
        return CommentDto.createCommentDto(target);

    }
}
