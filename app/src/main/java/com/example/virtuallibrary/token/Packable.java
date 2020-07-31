package com.example.virtuallibrary.token;

import com.example.virtuallibrary.token.ByteBuf;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
