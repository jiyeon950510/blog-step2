package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailResDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResDto;

@Mapper
public interface BoardRepository {
        public List<Board> findAll();

        public List<BoardMainResDto> findAllWithUser();

        public Board findById(int id);

        public int insert(@Param("title") String title, @Param("content") String content,
                        @Param("userId") int userId, @Param("thumbnail") String thumbnail);

        public int updateById(@Param("id") int id, @Param("title") String title,
                        @Param("content") String content, @Param("thumbnail") String thumbnail);

        public int deleteById(int id);

        public BoardDetailResDto findByIdWithUser(int id);
}