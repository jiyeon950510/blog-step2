package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardupdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    /*
     * 스프링 파싱 전략
     * 기본 전략은 x-www.url : 다른 컨텐츠일 경우 전략을 바꿔줘야햄
     * 리쿼스트 바디를 붙이면 일단 버퍼로 읽음. if로 분류, 컨텐츠 타입이 텍스트면 바로보냄(스트림으로 읽음).
     * 컨텐트 타입이 제이슨인 경우 오브젝트메퍼를 활용해서 파싱해줌.
     * 클라이언트로부터 데이터가 날라올때, 바디가 제이슨인 경우 리퀘스트 바디를 붙여서 파싱
     */

    @PutMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardupdateReqDto boardUpdateDto,
            HttpServletResponse response) {
        // System.out.println(boardUpdateDto.getTitle());
        // System.out.println(boardUpdateDto.getContent());
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (boardUpdateDto.getTitle() == null || boardUpdateDto.getTitle().isEmpty()) {
            throw new CustomApiException("title을 작성해주세요");
        }
        if (boardUpdateDto.getContent() == null || boardUpdateDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요");
        }
        boardService.글수정(id, boardUpdateDto, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정 성공", null), HttpStatus.OK);
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        // 세선체크
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        // 게시글 체크
        Board boardUb = boardRepository.findById(id);
        if (boardUb == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다");
        }
        // 권한체크
        if (boardUb.getUserId() != principal.getId()) {
            throw new CustomException("게시글을 수정할 권한이 없습니다.");
        }

        model.addAttribute("board", boardUb);
        return "board/updateForm";
    }

    @DeleteMapping("/board/{id}") // @ResponseBody 데이터를 응답
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }

    @PostMapping("/board")
    public String save(BoardSaveReqDto BoardSaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (BoardSaveReqDto.getTitle() == null || BoardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("title을 작성해주세요");
        }
        if (BoardSaveReqDto.getContent() == null || BoardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("content를 작성해주세요");
        }
        boardService.글쓰기(BoardSaveReqDto, principal.getId());
        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    public String main(Model model) {
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

}