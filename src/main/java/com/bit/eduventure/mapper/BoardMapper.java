//package com.bit.eduventure.mapper;
//
//import com.bit.eduventure.ES7_Board.DTO.BoardDTO;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//
//@Mapper
//public interface BoardMapper {
//    @Select("SELECT BOARD_NO" +
//            "     , BOARD_TITLE" +
//            "     , BOARD_CONTENT" +
//            "     , BOARD_WRITER" +
//            "     , BOARD_REGDATE" +
//            "     , BOARD_CNT" +
//            "   FROM T_BOARD" +
//            "   WHERE BOARD_NO = #{boardNo}")
//    BoardDTO getBoard(int boardNo);
//
//    @Select("SELECT BOARD_NO" +
//            "     , BOARD_TITLE" +
//            "     , BOARD_CONTENT" +
//            "     , BOARD_WRITER" +
//            "     , BOARD_REGDATE" +
//            "     , BOARD_CNT" +
//            "   FROM T_BOARD")
//    List<BoardDTO> getBoardList();
//
//    @Insert("INSERT INTO T_BOARD(" +
//            "   BOARD_TITLE," +
//            "   BOARD_CONTENT," +
//            "   BOARD_WRITER" +
//            ") VALUES (" +
//            "   #{boardTitle}," +
//            "   #{boardContent}," +
//            "   #{boardWriter}" +
//            ")")
//    void insertBoard(BoardDTO boardDTO);
//
//    @Update("UPDATE T_BOARD" +
//            "   SET" +
//            "       BOARD_TITLE = #{boardTitle}," +
//            "       BOARD_CONTENT = #{boardContent}" +
//            "   WHERE BOARD_NO = #{boardNo}")
//    void updateBoard(BoardDTO boardDTO);
//
//    @Delete("DELETE FROM T_BOARD" +
//            "   WHERE BOARD_NO = #{boardNo}")
//    void deleteBoard(int boardNo);
//}
