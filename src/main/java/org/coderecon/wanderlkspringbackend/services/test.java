//package org.coderecon.wanderlkspringbackend.services;
//import org.coderecon.wanderlkspringbackend.repositories.EncryptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.security.Key;
//import java.util.Optional;
//
//@Service
//public class testService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EncryptionRepository encryptionRepository;
//
//    @Autowired
//    private EncryptionService encryptionService;
//
//    public User createUser(String name, String data) throws Exception {
//        // Generate a unique key for the user
//        Key key = encryptionService.generateKey();
//        String encryptedData = encryptionService.encrypt(data, key);
//
//        // Save the encryption key
//        EncryptionKey encryptionKey = new EncryptionKey();
//        encryptionKey.setUserId(user.getId());
//        encryptionKey.setEncryptionKey(Base64.getEncoder().encodeToString(key.getEncoded()));
//        encryptionKeyRepository.save(encryptionKey);
//
//        // Save the user with encrypted data
//        User user = new User();
//        user.setName(name);
//        user.setEncryptedData(encryptedData);
//        user.setEncryptionKeyId(encryptionKey.getId());
//        userRepository.save(user);
//
//        return user;
//    }
//
//    public String getUserData(String userId) throws Exception {
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            Optional<EncryptionKey> keyOpt = encryptionKeyRepository.findById(user.getEncryptionKeyId());
//            if (keyOpt.isPresent()) {
//                EncryptionKey encryptionKey = keyOpt.get();
//                Key key = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey.getEncryptionKey()), "AES");
//                return encryptionService.decrypt(user.getEncryptedData(), key);
//            }
//        }
//        throw new RuntimeException("User or EncryptionKey not found");
//    }
//}
