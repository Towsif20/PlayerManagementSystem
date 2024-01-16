package com.towsif.PlayerManagementSystem.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request, Model model)
    {
        boolean isApiRequest = isApiRequest(request);

        if(isApiRequest)
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);

        model.addAttribute("errorMsg", exception.getMessage());

        return "error";
    }

    @ExceptionHandler({PropertyReferenceException.class, ValidationException.class, ClassCastException.class})
    public Object handleBadRequests(RuntimeException exception, HttpServletRequest request, Model model)
    {
        boolean isApiRequest = isApiRequest(request);

        if(isApiRequest)
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

        model.addAttribute("errorMsg", exception.getMessage());

        return "error";
    }

    private static boolean isApiRequest(HttpServletRequest request)
    {
        return request.getRequestURI().startsWith("/api");
    }
}
