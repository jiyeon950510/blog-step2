package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp;

import shop.mtcoding.blog.model.User;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)

public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

    @BeforeEach // test 메서드 실행 직전 마다 호출됨
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;
        // when
        ResultActions resultActions = mvc.perform(delete("/board/" + id).session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);
        /*
         * jsonPath
         * 최상위 : $
         * 객체상태 : 닷(.)
         * 배열 : [0]
         */
        // then
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardResp.BoardMainResDto> dtos = (List<BoardResp.BoardMainResDto>) map.get("dtos");
        String model = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");
    }

    @Test
    public void save_test() throws Exception {
        // given
        setUp();
        String title = "";
        for (int i = 0; i < 90; i++) {
            title += "가";
        }
        String requestBody = "title=제목&content=내용&userId=1";

        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void datail_test() throws Exception {
        // given
        int id = 1;
        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardResp.BoardDetailResDto dto = (BoardResp.BoardDetailResDto) map.get("dto");
        String model = om.writeValueAsString(dto);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk());
        // assertThat(dto.getUsername()).isEqualTo("love");
    }

    @Test
    public void update_test() throws Exception {
        // given
        setUp();
        String title = "";
        for (int i = 0; i < 90; i++) {
            title += "가";
        }
        String requestBody = "title=제목&content=내용&userId=1";
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                post("/board/" + id + "/update")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }
}