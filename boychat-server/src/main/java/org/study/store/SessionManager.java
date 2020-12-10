package org.study.store;

import com.google.common.collect.Maps;
import org.study.store.impl.LocalSessionManager;

import java.util.Map;
import java.util.Optional;

/**
 * 登录态管理
 * @author Tomato
 * Created on 2020.12.10
 */
public interface SessionManager {

    Map<Class<?>, SessionManager> MANAGER_HOLDER = Maps.newHashMap();

    static SessionManager getSingletonByClass(Class<? extends SessionManager> clazz) {
        SessionManager sessionManager = MANAGER_HOLDER.get(clazz);
        if (sessionManager == null) {
            synchronized (MANAGER_HOLDER) {
                if (MANAGER_HOLDER.get(clazz) == null) {
                    try {
                        sessionManager = clazz.newInstance();
                    } catch (Exception e) {
                        sessionManager = new LocalSessionManager();
                    }
                    MANAGER_HOLDER.put(clazz, sessionManager);
                }
            }
        }
        return sessionManager;
    }

    /**
     * 保存session数据，并生成令牌
     * @param session session
     * @return token
     */
    String saveSession(Session session);

    /**
     * 根据令牌获取用户登录数据
     * @param token 令牌
     * @return session
     */
    Optional<Session> getSessionByToken(String token);

    /**
     * 根据userId获取数据
     * @param userId 用户标识
     * @return session
     */
    Optional<Session> getSessionByUserId(String userId);
}
