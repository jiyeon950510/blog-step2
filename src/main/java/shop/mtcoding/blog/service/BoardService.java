package shop.mtcoding.blog.service;

import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardupdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.util.HtmlParser;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private HttpSession session;

    // where 절에 걸리는 파다메터를 앞에 받기 (약속)
    @Transactional
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {

        // 1. content 내용을 document 로 받고, img 찾아내서 (0,1,2), src를 찾아 thumbnail 에 추가

        String thumnail = HtmlParser.getThumbnail(boardSaveReqDto.getContent());

        int result = boardRepository.insert(boardSaveReqDto.getTitle(),
                boardSaveReqDto.getContent(), userId, thumnail);
        if (result != 1) {
            throw new CustomApiException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return 1;
    }

    @Transactional
    public void 게시글삭제(int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글을 삭제할 수 없습니다");
        }
        if (boardPS.getUserId() != userId) {
            throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        try {
            boardRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            // 로그를 남겨 놔야함(DB or File)
        }
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정(int id, BoardupdateReqDto boardupdateReqDto, int principalId) {
        Board boardUb = boardRepository.findById(id);
        if (boardUb == null) {
            throw new CustomApiException("없는 게시글을 찾을 수 없습니다");
        }
        if (boardUb.getUserId() != principalId) {
            throw new CustomApiException("해당 게시글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        // 부가로직은 추후 aop 쓸꺼ㄱㅇㄷ
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        String thumnail = HtmlParser.getThumbnail(boardSaveReqDto.getContent());

        int result = boardRepository.updateById(id, boardupdateReqDto.getTitle(), boardupdateReqDto.getContent(),
                thumnail);
        if (result != 1) {
            throw new CustomApiException("게시글 수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return 1;
    }
}
