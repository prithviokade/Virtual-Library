package com.example.virtuallibrary.token;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}