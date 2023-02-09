package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {

    @Setter
    @Getter
    public static class BoardSaveReqDto {
        private String title;
        private String content;
        private String thumbnail;
    }

    @Setter
    @Getter
    public static class BoardupdateReqDto {
        private String title;
        private String content;
    }
}
