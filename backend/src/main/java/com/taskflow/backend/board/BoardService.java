package com.taskflow.backend.board;

import com.taskflow.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private String getCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public BoardResponse create(BoardRequest request) {
        var user = userRepository.findByEmail(getCurrentEmail()).orElseThrow();
        var board = Board.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();
        var saved = boardRepository.save(board);
        return toResponse(saved);
    }

    public List<BoardResponse> getMyBoards() {
        return boardRepository.findByUserEmail(getCurrentEmail())
                .stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    private BoardResponse toResponse(Board b) {
        return new BoardResponse(b.getId(), b.getName(), b.getDescription(), b.getCreatedAt());
    }
}