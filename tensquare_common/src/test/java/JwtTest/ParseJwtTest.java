package JwtTest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseJwtTest {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
        String
                token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLpqazlhL8iLCJpYXQiOjE1NzYxMzEwMzcsImV4cCI6MTU3NjEzMTA5Niwicm9sZXMiOiJhZG1pbiJ9.J_gFUFWFycmhzrVZwVscO43gwPTPXxPr2qoXWnMm1EU";
        Claims claims =
                Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("签发时间:" + sdf.format(claims.getIssuedAt()));
        System.out.println("过期时间:" + sdf.format(claims.getExpiration()));
        System.out.println("自定义:" + claims.get("roles"));
        System.out.println("当前时间:" + sdf.format(new Date()));
    }
}
