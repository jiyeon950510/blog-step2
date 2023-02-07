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

}
