package org.study.client.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tomato
 * Created on 2020.12.12
 */
@Setter
@Getter
@AllArgsConstructor
public class ClientLocalInfo {

    /**
     * 本地用户的email
     */
    private String email;

    /**
     * 建立连接后的token
     */
    private String token;
}
