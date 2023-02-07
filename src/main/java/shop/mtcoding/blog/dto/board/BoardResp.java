package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {// 응답의 dto

    @Getter
    @Setter
    public static class BoardMainResDto {
        private int id;
        private String title;
        private String username;

    }

    @Setter
    @Getter
    public static class BoardDetailResDto {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username;

    }

}
