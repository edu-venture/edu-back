package com.bit.eduventure.ES8_Quiz;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuizUserHistoryDTO {

    private Integer no;
    private Integer boardNo;
    private Integer id;


    public QuizUserHistory DTOToEntity() {
        return QuizUserHistory.builder()
                .boardNo(this.boardNo).pkNo(this.no).id(this.id)
                .build();
    }


}
