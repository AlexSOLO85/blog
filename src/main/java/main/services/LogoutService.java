package main.services;

import main.api.response.BooleanResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LogoutService {
    public final ResponseEntity<BooleanResponse> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }
}
