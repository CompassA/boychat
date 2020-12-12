package org.study.client.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
public class ClientDataManager {

    private final ClientLocalInfo clientLocalInfo;

    private ClientState clientState;

    public ClientDataManager() {
        this.clientLocalInfo = new ClientLocalInfo(StringUtils.EMPTY, StringUtils.EMPTY);
        this.clientState = ClientState.TO_LOGIN;
    }

    public synchronized void processAfterLogin(String email) {
        if (clientState != ClientState.TO_LOGIN) {
            return;
        }
        this.clientLocalInfo.setEmail(email);
        this.clientState = ClientState.WAIT_RESPONSE;
    }

    public synchronized void processAfterResponse(String token) {
        if (clientState != ClientState.WAIT_RESPONSE) {
            return;
        }
        this.clientLocalInfo.setToken(token);
        this.clientState = ClientState.CONNECTED;
    }

    public String getToken() {
        return this.clientLocalInfo.getToken();
    }

    public String getEmail() {
        return this.clientLocalInfo.getEmail();
    }

    public ClientState getClientState() {
        return this.clientState;
    }

}
