package com.sau.chatapp.sauchatapp.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sau.chatapp.sauchatapp.entities.User;
import com.sau.chatapp.sauchatapp.security.JwtUtils;
import com.sau.chatapp.sauchatapp.services.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class EncryptionUtil {
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserService userService;
	
	
	
    private static final String ALGORITHM = "AES";

    public static String encrypt(String text, String key) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    public static String decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    public String chatKey(String receiver, String sender, String jwt) throws Exception {
    	String encryptedKey= "";
    	String userName = jwtUtils.getUserNameFromJwtToken(jwt);
		if (receiver.equals("main")) {
			User senderUser = userService.getUserByName(sender);
			encryptedKey = encrypt(senderUser.getPassword(), "main");
			return encryptedKey;
		} else if (userName.equals(receiver) || userName.equals(sender)) {
			User senderUser = userService.getUserByName(sender);
			if (!receiver.equals("main")) {
				User receiverUser = userService.getUserByName(receiver);
				encryptedKey = encrypt(senderUser.getPassword(), receiverUser.getPassword());
			} else
				encryptedKey = encrypt(senderUser.getPassword(), "main");
			return encryptedKey;
		} else {
			System.err.println(
					"getKey Opersayonunda Hata olustu alıcı veya gönderici ismi giriş yapan kulanıcı ile uyuşmuyor. Sender: "
							+ sender + " Receiver: " + receiver + "userName: " + userName);
		}
		return encryptedKey;
    }
    private static SecretKeySpec generateSecretKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] hashedBytes = MessageDigest.getInstance("SHA-256").digest(keyBytes);
        return new SecretKeySpec(hashedBytes, ALGORITHM);
    }
}