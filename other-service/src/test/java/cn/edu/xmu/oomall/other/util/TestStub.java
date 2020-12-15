package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.ooad.util.Common;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author XQChen
 * @version 创建时间：2020/12/6 下午10:28
 */
public class TestStub {
    private static final Logger logger = LoggerFactory.getLogger(TestStub.class);

    // 秘钥
    static final String SECRET = "Role-Privilege-Token";
    // 签名是有谁生成
    static final String ISSUSER = "OOAD";
    // 签名的主题
    static final String SUBJECT = "this is a token";
    // 签名的观众
    static final String AUDIENCE = "MINIAPP";
    // Request中的变量名
    public static final String LOGIN_TOKEN_KEY = "authorization";

    private static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    /***
     * 获取token
     * @param userId
     * @param departId
     * @param expireTime
     * @return
     */
    public static String createToken(Long userId, Long departId, int expireTime) {
        logger.debug("createToken:");
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> map = new HashMap<String, Object>();
            Date nowDate = new Date();
            Date expireDate = getAfterDate(nowDate, 0, 0, 0, 0, 0, expireTime);
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String tokenId = Common.genSeqNum();
            StringBuilder message = new StringBuilder().append("createToken: ").append("userId = ")
                    .append(userId).append(" departId=").append(departId).append(" tokenId:").append(tokenId);
            logger.debug(message.toString());
            String token = JWT.create()
                    // 设置头部信息 Header
                    .withHeader(map)
                    // 设置 载荷 Payload
                    .withClaim("userId", userId)
                    .withClaim("departId", departId)
                    .withClaim("tokenId",tokenId)
                    .withIssuer(ISSUSER)
                    .withSubject(SUBJECT)
                    .withAudience(AUDIENCE)
                    // 生成签名的时间
                    .withIssuedAt(nowDate)
                    // 签名过期的时间
                    .withExpiresAt(expireDate)
                    // 签名 Signature
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
