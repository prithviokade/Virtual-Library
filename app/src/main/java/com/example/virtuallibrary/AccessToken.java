package com.example.virtuallibrary;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.TreeMap;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AccessToken {

    public static final int VERSION_LENGTH = 3;
    public static final int APP_ID_LENGTH = 32;

    public enum Privileges {
        kJoinChannel(1),
        kPublishAudioStream(2),
        kPublishVideoStream(3),
        kPublishDataStream(4);

        public short intValue;

        Privileges(int value) {
            intValue = (short) value;
        }
    }

    private static final String VER = "006";

    public String appId;
    public String appCertificate;
    public String channelName;
    public String uid;
    public byte[] signature;
    public byte[] messageRawContent;
    public int crcChannelName;
    public int crcUid;
    public PrivilegeMessage message;
    public int expireTimestamp;

    public AccessToken(String appId, String appCertificate, String channelName, String uid) {
        this.appId = appId;
        this.appCertificate = appCertificate;
        this.channelName = channelName;
        this.uid = uid;
        this.crcChannelName = 0;
        this.crcUid = 0;
        this.message = new PrivilegeMessage();
    }

    public String build() throws Exception {
        if (! isUUID(appId)) {
            return "";
        }

        if (!isUUID(appCertificate)) {
            return "";
        }

        messageRawContent = pack(message);
        signature = generateSignature(appCertificate,
                appId, channelName, uid, messageRawContent);
        crcChannelName = crc32(channelName);
        crcUid = crc32(uid);

        PackContent packContent = new PackContent(signature, crcChannelName, crcUid, messageRawContent);
        byte[] content = pack(packContent);
        return getVersion() + this.appId + base64Encode(content);
    }

    public static int crc32(String data) {
        // get bytes from string
        byte[] bytes = data.getBytes();
        return crc32(bytes);
    }

    public static int crc32(byte[] bytes) {
        CRC32 checksum = new CRC32();
        checksum.update(bytes);
        return (int)checksum.getValue();
    }


    public static String base64Encode(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    public static byte[] base64Decode(String data) {
        return Base64.decodeBase64(data.getBytes());
    }

    private boolean isUUID(String appId) {
        if (appId.length() != 32) {
            return false;
        }
        return appId.matches("\\p{XDigit}+");
    }

    public static byte[] pack(PackableEx packableEx) {
        ByteBuf buffer = new ByteBuf();
        packableEx.marshal(buffer);
        return buffer.asBytes();
    }

    public static void unpack(byte[] data, PackableEx packableEx) {
        ByteBuf buffer = new ByteBuf(data);
        packableEx.unmarshal(buffer);
    }

    public static int randomInt() {
        return new SecureRandom().nextInt();
    }


    public static int getTimestamp() {
        return (int)((new Date().getTime())/1000);
    }

    public static byte[] hmacSign(String keyString, byte[] msg) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(msg);
    }

    public void addPrivilege(Privileges privilege, int expireTimestamp) {
        message.messages.put(privilege.intValue, expireTimestamp);
    }

    public static String getVersion() {
        return VER;
    }

    public static byte[] generateSignature(String appCertificate,
                                           String appID, String channelName, String uid, byte[] message) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(appID.getBytes());
            baos.write(channelName.getBytes());
            baos.write(uid.getBytes());
            baos.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hmacSign(appCertificate, baos.toByteArray());
    }

    public boolean fromString(String token) {
        if (!getVersion().equals(token.substring(0, VERSION_LENGTH))) {
            return false;
        }

        try {
            appId = token.substring(VERSION_LENGTH, VERSION_LENGTH + APP_ID_LENGTH);
            PackContent packContent = new PackContent();
            unpack(base64Decode(token.substring(VERSION_LENGTH + APP_ID_LENGTH, token.length())), packContent);
            signature = packContent.signature;
            crcChannelName = packContent.crcChannelName;
            crcUid = packContent.crcUid;
            messageRawContent = packContent.rawMessage;
            unpack(messageRawContent, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public class PrivilegeMessage implements PackableEx {
        public int salt;
        public int ts;
        public TreeMap<Short, Integer> messages;

        public PrivilegeMessage() {
            salt = randomInt();
            ts = getTimestamp() + 24 * 3600;
            messages = new TreeMap<>();
        }

        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(salt).put(ts).putIntMap(messages);
        }

        @Override
        public void unmarshal(ByteBuf in) {
            salt = in.readInt();
            ts = in.readInt();
            messages = in.readIntMap();
        }
    }

    public class PackContent implements PackableEx {
        public byte[] signature;
        public int crcChannelName;
        public int crcUid;
        public byte[] rawMessage;

        public PackContent() {
            // Nothing done
        }

        public PackContent(byte[] signature, int crcChannelName, int crcUid, byte[] rawMessage) {
            this.signature = signature;
            this.crcChannelName = crcChannelName;
            this.crcUid = crcUid;
            this.rawMessage = rawMessage;
        }

        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(signature).put(crcChannelName).put(crcUid).put(rawMessage);
        }

        @Override
        public void unmarshal(ByteBuf in) {
            signature = in.readBytes();
            crcChannelName = in.readInt();
            crcUid = in.readInt();
            rawMessage = in.readBytes();
        }
    }
}