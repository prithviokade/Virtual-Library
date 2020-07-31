package com.example.virtuallibrary;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
