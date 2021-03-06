package com.swqualityboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swqualityboard.TestConfig;
import com.swqualityboard.configuration.annotation.WithAuthUser;
import com.swqualityboard.configuration.security.SecurityConfig;
import com.swqualityboard.dto.memo.create.CreateMemoInput;
import com.swqualityboard.dto.memo.update.UpdateMemoInput;
import com.swqualityboard.service.MemoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.swqualityboard.ApiDocumentUtils.getDocumentRequest;
import static com.swqualityboard.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith(RestDocumentationExtension.class) // JUnit 5 ????????? ?????? ????????? ?????????
@Import({TestConfig.class})
@WebMvcTest(controllers = MemoController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
class MemoControllerTest {
    @MockBean
    private MemoService memoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @TestConfiguration
    static class DefaultConfigWithoutCsrf extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http.csrf().disable();
        }
    }

    @WithAuthUser(email = "admin1@gmail.com", role = "ROLE_ADMIN")
    @DisplayName("?????? ?????? ??????")
    @Test
    public void ??????_??????_??????() throws Exception {
        //given
        CreateMemoInput createMemoInput = CreateMemoInput.builder()
                .systemQualityId("6188fdc9948f96df1cff9ba1")
                .content("??????????????????.")
                .build();

        //when
        doNothing().when(memoService).createMemo(any(), any(CreateMemoInput.class));

        //then
        mockMvc.perform(post("/api/memos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer JWT ACCESS TOKEN")
                        .content(objectMapper.writeValueAsString(createMemoInput)).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated()) // 201
                .andDo(
                        document(
                                "memoApi/create_memo/successful",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(headerWithName("Authorization").description("Bearer JWT Token")),
                                requestFields(
                                        fieldWithPath("systemQualityId").type(JsonFieldType.STRING)
                                                .description("????????? SW ???????????? ?????? ??????")
                                                .attributes(key("constraint")
                                                        .value("?????? 1??? ????????? ???????????? ??????????????????.")),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("?????? ??????")
                                                .attributes(key("constraint")
                                                        .value("?????? 1????????? ??????????????????."))
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                                                .description("?????? ?????? ??????"),
                                        fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                                .description("?????? ??????"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("?????? ?????????"),
                                        fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                .description("api ?????? ??????")
                                )
                        ));
    }

    @WithAuthUser(email = "admin1@gmail.com", role = "ROLE_ADMIN")
    @DisplayName("?????? ?????? ??????")
    @Test
    public void ??????_??????_??????() throws Exception {
        //given
        UpdateMemoInput updateMemoInput = UpdateMemoInput.builder()
                .content("??????????????????22.")
                .build();

        //when
        doNothing().when(memoService).updateMemo(any(), any(UpdateMemoInput.class));

        //then
        mockMvc.perform(patch("/api/memos/{id}", "memoId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer JWT ACCESS TOKEN")
                        .content(objectMapper.writeValueAsString(updateMemoInput)).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // 200
                .andDo(
                        document(
                                "memoApi/update_memo/successful",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(headerWithName("Authorization").description("Bearer JWT Token")),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("????????? ?????? ??????")
                                                .attributes(key("constraint")
                                                        .value("?????? 1????????? ??????????????????."))
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                                                .description("?????? ?????? ??????"),
                                        fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                                .description("?????? ??????"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("?????? ?????????"),
                                        fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                .description("api ?????? ??????")
                                )
                        ));
    }

    @WithAuthUser(email = "admin1@gmail.com", role = "ROLE_ADMIN")
    @DisplayName("?????? ?????? ??????")
    @Test
    public void ??????_??????_??????() throws Exception {
        //given

        //when
        doNothing().when(memoService).deleteMemo(any());

        //then
        mockMvc.perform(delete("/api/memos/{id}", "memoId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer JWT ACCESS TOKEN"))
                .andDo(print())
                .andExpect(status().isOk()) // 200
                .andDo(
                        document(
                                "memoApi/delete_memo/successful",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(headerWithName("Authorization").description("Bearer JWT Token")),
                                responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                                                .description("?????? ?????? ??????"),
                                        fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                                .description("?????? ??????"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("?????? ?????????"),
                                        fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                .description("api ?????? ??????")
                                )
                        ));
    }

}
