package com.attendify.attendify_api.shared.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class CustomErrorController implements ErrorController {

    private final ObjectMapper objectMapper;

    public CustomErrorController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        String message;
        if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
            message = "Endpoint does not exist";
        } else {
            message = "An error occurred";
        }

        response.setContentType("application/json");
        response.setStatus(statusCode);

        var body = ErrorResponse.of(statusCode,
                statusCode == 404 ? "Not Found" : "Error",
                message);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}