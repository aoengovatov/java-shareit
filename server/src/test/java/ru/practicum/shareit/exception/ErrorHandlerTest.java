package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    ErrorHandler errorHandler;

    @Test
    void handleIncorrectParameterException_whenInvoked_thenReturnStatusNotFound() {
        String exceptionMessage = "IncorrectParameterException";
        ResponseEntity<List<String>> response = errorHandler.handleIncorrectParameterException(
                new IncorrectParameterException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleUserNotFoundException_whenInvoked_thenReturnStatusNotFound() {
        String exceptionMessage = "NotFound";
        ResponseEntity<List<String>> response = errorHandler.handleUserNotFoundException(
                new UserNotFoundException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleItemNotFoundException_whenInvoked_thenReturnStatusNotFound() {
        String exceptionMessage = "NotFound";
        ResponseEntity<List<String>> response = errorHandler.handleItemNotFoundException(
                new ItemNotFoundException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleBookingNotFoundException_whenInvoked_thenReturnStatusNotFound() {
        String exceptionMessage = "NotFound";
        ResponseEntity<List<String>> response = errorHandler.handleBookingNotFoundException(
                new BookingNotFoundException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleItemRequestNotFoundException_whenInvoked_thenReturnStatusNotFound() {
        String exceptionMessage = "NotFound";
        ResponseEntity<List<String>> response = errorHandler.handleItemRequestNotFoundException(
                new ItemRequestNotFoundException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void handleBadParameterException_whenInvoked_thenReturnStatusBadRequest() {
        String exceptionMessage = "BadRequest";

        ResponseEntity<List<String>> response = errorHandler.handleBadParameterException(
                new BadParameterException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleThrowableException_whenInvoked_thenReturnInternalServerError() {
        String exceptionMessage = "InternalServerError";

        ResponseEntity response = errorHandler.handleThrowableException(
                new Throwable(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void handleValidationException_whenInvoked_thenReturnInternalServerError() {
        String exceptionMessage = "InternalServerError";

        ResponseEntity response = errorHandler.handleValidationException(
                new ValidationException(exceptionMessage));

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}