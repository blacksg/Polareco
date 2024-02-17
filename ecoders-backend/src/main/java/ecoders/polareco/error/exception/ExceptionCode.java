package ecoders.polareco.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    MEMBER_ALREADY_EXISTS(CONFLICT, "Member corresponding to the email already exists"),
    EMAIL_VERIFICATION_CODE_NOT_FOUND(NOT_FOUND, "Email verification code not found"),
    EMAIL_VERIFICATION_CODE_MISMATCH(FORBIDDEN, "Email verification code mismatch"),
    MEMBER_NOT_FOUND(NOT_FOUND, "Member not found"),
    LOGIN_FAILED(UNAUTHORIZED, "Login failed"),
    JWT_EXPIRED(FORBIDDEN, "Expired JWT"),
    JWT_INVALID(FORBIDDEN, "Invalid JWT"),
    REFRESH_TOKEN_INVALID(FORBIDDEN, "Invalid refresh token"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "Refresh token not found"),
    MAIL_MESSAGING_FAILED(INTERNAL_SERVER_ERROR, "Mail messaging failed"),
    PASSWORD_RESET_TOKEN_NOT_FOUND(NOT_FOUND, "Password reset token not found"),
    PASSWORD_RESET_TOKEN_MISMATCH(FORBIDDEN, "Password reset token mismatch"),
    PASSWORD_MISMATCH(UNAUTHORIZED, "Password mismatch"),
    IMAGE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "Image upload failed"),
    POST_NOT_FOUND(NOT_FOUND, "Post not found"),
    CANNOT_CHANGE_POST(FORBIDDEN, "Post cannot change"),
    CANNOT_DELETE_POST(FORBIDDEN, "Post cannot delete"),
    COMMENT_NOT_FOUND(NOT_FOUND, "Comment not found"),
    CANNOT_CHANGE_COMMENT(FORBIDDEN, "Comment cannot change"),
    CANNOT_DELETE_COMMENT(FORBIDDEN, "Comment cannot delete");

    private final HttpStatus httpStatus;

    private final String message;
}
