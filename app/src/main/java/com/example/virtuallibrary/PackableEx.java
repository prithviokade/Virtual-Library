package com.example.virtuallibrary;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}