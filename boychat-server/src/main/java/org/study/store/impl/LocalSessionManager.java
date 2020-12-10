package org.study.store.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.study.store.Session;
import org.study.store.SessionManager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * 本地session管理
 * @author Tomato
 * Created on 2020.12.10
 */
public class LocalSessionManager implements SessionManager {

    /**
     * userId -> session
     */
    private static final ConcurrentMap<String, Session> USED_ID_SESSION_MAP = Maps.newConcurrentMap();

    /**
     * token -> session
     */
    private static final ConcurrentMap<String, Session> TOKEN_SESSION_MAP = Maps.newConcurrentMap();

    @Override
    public String saveSession(Session session) {
        if (session == null || StringUtils.isBlank(session.getUserId())
                || StringUtils.isBlank(session.getUserName())
                || StringUtils.isBlank(session.getUserPassword())
                || session.getChannel() == null) {
            return null;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        session.setToken(token);
        USED_ID_SESSION_MAP.put(session.getUserId(), session);
        TOKEN_SESSION_MAP.put(token, session);
        return token;
    }

    @Override
    public Optional<Session> getSessionByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }
        return Optional.ofNullable(TOKEN_SESSION_MAP.get(token));
    }

    @Override
    public Optional<Session> getSessionByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(USED_ID_SESSION_MAP.get(userId));
    }
}
