package com.note.nebula.nebula.apps_security;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Steven Albert on 12/4/2017.
 */

public class SecureNote {

    private byte[] key;

    private static SecureNote secureNote;

    public static SecureNote getInstance() {
        return secureNote;
    }

    public static void exitNote() { secureNote = null; }

    public SecureNote(byte[] Key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            Key = digest.digest(Key);
            key = Arrays.copyOf(Key, Key.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        secureNote = this;
    }

    public byte[] encryptMessage(byte[] message) {
        try {
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secureNote.key, secureNote.key.length), "AES"));
            return Authenticator.concat(encryptCipher.doFinal(message), encryptCipher.getIV());
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decryptMessage(byte[] message) {
        try {
            Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivByte = Arrays.copyOfRange(message, message.length-16, message.length);
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secureNote.key, secureNote.key.length), "AES"), iv);
            return decryptCipher.doFinal(Arrays.copyOf(message, message.length-16));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
