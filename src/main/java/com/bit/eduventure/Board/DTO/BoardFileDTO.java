package com.bit.eduventure.Board.DTO;


import com.bit.eduventure.Board.Entity.Board;
import com.bit.eduventure.Board.Entity.BoardFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFileDTO {
    private int boardNo;
    private int boardFileNo;
    private String boardFileName;
    private String boardFilePath;
    private String boardFileOrigin;
    private String boardFileCate;
    private String boardFileStatus;
    private String newFileName;

    public BoardFile DTOToEntity() {
        Board board = Board.builder()
                .boardNo(this.boardNo)
                .build();

        BoardFile boardFile = BoardFile.builder()
                .board(board)
                .boardFileNo(this.boardFileNo)
                .boardFileName(this.boardFileName)
                .boardFilePath(this.boardFilePath)
                .boardFileOrigin(this.boardFileOrigin)
                .boardFileCate(this.boardFileCate)
                .build();

        return boardFile;
    }
}
