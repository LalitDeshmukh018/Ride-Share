package com.Lalitdk.project.uber.uberApp.services;

import com.Lalitdk.project.uber.uberApp.entities.Session;
import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int  SESSION_LIMIT =2;

    public  void generateNewSession(User user, String refreshToken){
        List<Session> userSession = sessionRepository.findByUser(user);
        if (userSession.size() == SESSION_LIMIT){
            userSession.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastRecentlyUsedSession = userSession.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken){
       Session session= sessionRepository.findByRefreshToken(refreshToken)
               .orElseThrow(()-> new SessionAuthenticationException("Session not found for Refresh token" + refreshToken ));
       session.setLastUsedAt(LocalDateTime.now());
       sessionRepository.save(session);

    }
}
