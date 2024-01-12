package com.dxj.enumeration;

import lombok.Data;

public enum PrivateHttpStatus {
    USERNAME_PASSWORD_WRONG(411,"账号或密码错误");
    private final int status;
    private final String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    PrivateHttpStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
