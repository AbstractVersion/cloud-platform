/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.repository;

import com.micro.env.entity.SessionInfo;
import com.micro.env.repository.exception.SessionNotFound;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author onelove
 */
@Repository
public class SessionRepository {

    @Autowired
    private SessionRepoInterface sessionRepository;

    public List<SessionInfo> findSessions() {
        Iterable<SessionInfo> source = sessionRepository.findAll();
        List<SessionInfo> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public SessionInfo findSession(String uuid) throws SessionNotFound {
        Optional<SessionInfo> token = sessionRepository.findById(uuid);
        if (!token.isPresent()) {
            throw new SessionNotFound("We where not able to identify the requested session.");
        }
        return token.get();
    }

    public SessionInfo createSession(SessionInfo session) {
        sessionRepository.save(session);
        return session;
    }

    public void editSession(SessionInfo session) {
        sessionRepository.save(session);
    }

}
