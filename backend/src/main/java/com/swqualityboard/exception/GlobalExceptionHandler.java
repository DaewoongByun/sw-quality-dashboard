package com.swqualityboard.exception;

import com.swqualityboard.exception.memo.MemoDuplicateException;
import com.swqualityboard.exception.memo.MemoNotFoundException;
import com.swqualityboard.exception.system.SystemNotFoundException;
import com.swqualityboard.exception.team.TeamNotFoundException;
import com.swqualityboard.exception.user.*;
import com.swqualityboard.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.swqualityboard.response.ResponseStatus.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Valid 조건을 만족하지 못한 요청에 대한 에러 핸들러
    @ExceptionHandler(BindException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(
            BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        log.debug("Vaildation failed", ex);

        return new ResponseEntity<>(new Response<>(400,stringBuilder.toString()), HttpStatus.BAD_REQUEST);
    }

    // 이미 존재하는 Email 가입에 대한 에러 핸들러
    @ExceptionHandler(UserDuplicateEmailException.class)
    public final ResponseEntity<Object> handleUserDuplicateEmailException(
            UserDuplicateEmailException ex) {
        log.debug("중복된 Email", ex);
        return new ResponseEntity<>(new Response<>(EXISTS_EMAIL), HttpStatus.CONFLICT);
    }

    // 이미 존재하는 Nickname 가입에 대한 에러 핸들러
    @ExceptionHandler(UserDuplicateNicknameException.class)
    public final ResponseEntity<Object> handleUserDuplicateNicknameException(
            UserDuplicateNicknameException ex) {
        log.debug("중복된 Nickname", ex);
        return new ResponseEntity<>(new Response<>(EXISTS_NICKNAME), HttpStatus.CONFLICT);
    }

    // 탈퇴한 회원
    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
        log.debug("인증되지 않은 유저입니다.", ex);
        return new ResponseEntity<>(new Response<>(UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    // 존재하지 않는 권한 조회에 대한 에러 핸들러
    @ExceptionHandler(AuthorityNotFoundException.class)
    public final ResponseEntity<Object> handleAuthorityNotFoundException(
            AuthorityNotFoundException ex) {
        log.debug("존재하지 않는 권한", ex);
        return new ResponseEntity<>(new Response<>(NOT_FOUND_AUTHORITY), HttpStatus.NOT_FOUND);
    }

    // 존재하지 않는 유저 정보 조회에 대한 에러 핸들러
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        log.debug("존재하지 않는 유저", ex);
        return new ResponseEntity<>(new Response<>(NOT_FOUND_USER), HttpStatus.NOT_FOUND);
    }

    // 비밀번호 불일치에 대한 에러 핸들러
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        log.debug("비밀번호 불일치", ex);
        return new ResponseEntity<>(new Response<>(UNAUTHORIZED_BAD_CREDENTIALS), HttpStatus.UNAUTHORIZED);
    }

    // 존재하지 않는 팀 조회에 대한 에러 핸들러
    @ExceptionHandler(TeamNotFoundException.class)
    public final ResponseEntity<Object> handleTeamNotFoundException(
            TeamNotFoundException ex) {
        log.debug("존재하지 않는 팀", ex);
        return new ResponseEntity<>(new Response<>(NOT_FOUND_TEAM), HttpStatus.NOT_FOUND);
    }

    // 존재하지 않는 시스템 조회에 대한 에러 핸들러
    @ExceptionHandler(SystemNotFoundException.class)
    public final ResponseEntity<Object> handleSystemNotFoundException(
            SystemNotFoundException ex) {
        log.debug("존재하지 않는 시스템", ex);
        return new ResponseEntity<>(new Response<>(NOT_FOUND_SYSTEM), HttpStatus.NOT_FOUND);
    }

    // 이미 존재하는 메모에 대한 에러 핸들러
    @ExceptionHandler(MemoDuplicateException.class)
    public final ResponseEntity<Object> handleMemoDuplicateException(
            MemoDuplicateException ex) {
        log.debug("이미존재하는 메모 ", ex);
        return new ResponseEntity<>(new Response<>(EXISTS_MEMO), HttpStatus.CONFLICT);
    }

    // 존재하지 않는 메모 조회에 대한 에러 핸들러
    @ExceptionHandler(MemoNotFoundException.class)
    public final ResponseEntity<Object> handleMemoNotFoundException(
            MemoNotFoundException ex) {
        log.debug("존재하지 않는 메모", ex);
        return new ResponseEntity<>(new Response<>(NOT_FOUND_MEMO), HttpStatus.NOT_FOUND);
    }
}
