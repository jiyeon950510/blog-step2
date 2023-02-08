package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.model.BoardRepository;

public class Thumbnail {

    @Autowired
    private BoardRepository boardRepository;

    public void ThumbnailUtil(BoardSaveReqDto boardSaveReqDto) {
        // String img = "";

        Document doc = Jsoup.parse(boardSaveReqDto.getContent());
        Elements els = doc.select("img"); // 복수, 배열로 리턴
        if (els.size() == 0) {
            // 디비 thumnail -> /images/profile.ifif/
            String temp = "/images/dm.png";
        }
        Element el = els.get(0);

        String img = el.attr("src");
    }
}
