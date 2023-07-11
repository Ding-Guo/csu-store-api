package org.csu.api.util;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author 旺旺米雪饼
 */
@Component
public class Md5TokenGenerator {

    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
