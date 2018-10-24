package com.ncu.springboot.mvc.helper;

import com.ncu.springboot.pojo.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

@Service
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator =
            new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private final int hashIterations = 2;
    public void encryptPassword(User user) {
        //user.setCredentialSalt(randomNumberGenerator.nextBytes().toHex());
        //使用了默认的salt = 123 来测试
        String newPassword = new SimpleHash(
                algorithmName,
                user.getPwd(),
                ByteSource.Util.bytes(user.getName()+user.getCredentialSalt()),
                hashIterations).toHex();
        user.setPwd(newPassword);
    }
}
