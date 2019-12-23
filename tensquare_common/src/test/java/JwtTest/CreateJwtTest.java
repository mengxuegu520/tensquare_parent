package JwtTest;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * 创建类CreateJwtTest，用于生成token
 */
public class CreateJwtTest {
    public static void main(String[] args) {
        /*
        setIssuedAt用于设置签发时间
        signWith用于设置签名秘钥
         */
        long now = System.currentTimeMillis();//当前时间
        long exp = now + 1000 * 60;//过期时间为1分钟
        JwtBuilder jwtBuilder = Jwts.builder().setId("666")
                .setSubject("马儿")
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .claim("roles","admin") //自定义
                .signWith(SignatureAlgorithm.HS256, "itcast");
        System.out.println(jwtBuilder.compact());
    }

}
