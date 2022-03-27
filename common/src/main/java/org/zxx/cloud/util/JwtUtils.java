package org.zxx.cloud.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.zxx.cloud.exception.JwtException;
import org.zxx.cloud.module.bo.JwtUserBO;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class JwtUtils {
    /**
     * 默认token有效期：60分钟
     * */
    private static final int defaultExpiresMinutes=60;
    private static final String defaultSaltString="ZhouXXIsTheBest!!!";
    /**
     签发对象：这个用户的id
     签发时间：现在
     有效时间：60分钟
     载荷内容：暂时设计为：userId#userName#roleId#MD5
     加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(JwtUserBO jwtUserBO) {

        return createToken(jwtUserBO,defaultExpiresMinutes);   //加密
    }

    public static String createToken(JwtUserBO jwtUserBO,int expiresMinutes) {

        if (expiresMinutes<defaultExpiresMinutes){
            expiresMinutes=defaultExpiresMinutes;
        }
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,expiresMinutes);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(jwtUserBO.getUserId())   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("userId", jwtUserBO.getUserId())    //载荷，随便写几个都可以
                .withClaim("userName", jwtUserBO.getUserName())
                .withClaim("roleId", jwtUserBO.getRoleId())
                .withClaim("roleName", jwtUserBO.getRoleName())
                .withClaim("sign", jwtUserBO.getUserId())   // 存放正真的加密token userId#userName#roleId#expiresDate#MD5
                .sign(Algorithm.HMAC256( jwtUserBO.getUserId()+defaultSaltString));   //加密
    }


    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     * @param token
     * @throws JwtException
     */
    public static void verifyToken(String token, String secret) throws JwtException {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret+defaultSaltString)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //效验失败
            //这里抛出的异常是我自定义的一个异常，你也可以写成别的
            throw new JwtException("无效的Token：效验失败");
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) throws JwtException {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new JwtException("无效的Token：解析失败");
        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }
}
