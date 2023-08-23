package com.bit.eduventure.ES8_Quiz;

import com.bit.eduventure.ES7_Board.Entity.Board;
import com.bit.eduventure.ES7_Board.Entity.BoardFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizBoardFileDTO {
    private int boardNo;
    private int boardFileNo;
    private String boardFileName;
    private String boardFilePath;
    private String boardFileOrigin;
    private String boardFileCate;
    private String boardFileStatus;
    private String newFileName;

    public QuizBoardFile DTOToEntity() {
        QuizBoard quizBoard = QuizBoard.builder()
                .boardNo(this.boardNo)
                .build();

        QuizBoardFile quizBoardFile = QuizBoardFile.builder()
                .quizBoard(quizBoard)
                .boardFileNo(this.boardFileNo)
                .boardFileName(this.boardFileName)
                .boardFilePath(this.boardFilePath)
                .boardFileOrigin(this.boardFileOrigin)
                .boardFileCate(this.boardFileCate)
                .build();

        return quizBoardFile;
    }



}
