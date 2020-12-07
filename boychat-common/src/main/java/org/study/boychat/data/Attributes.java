package org.study.boychat.data;

import io.netty.util.AttributeKey;
import org.boychat.constants.Constants;

/**
 * @author fanqie
 * Created on 2020.08.16
 */
public class Attributes {

    public static final AttributeKey<Boolean> LOGIN_MARK =
            AttributeKey.newInstance(Constants.Attributes.LOGIN_KEY);

}
