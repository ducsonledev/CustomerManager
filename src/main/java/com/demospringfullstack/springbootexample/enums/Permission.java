package com.demospringfullstack.springbootexample.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_CREATE,
    ADMIN_DELETE,
    MANAGER_READ,
    MANAGER_UPDATE,
    MANAGER_CREATE,
    MANAGER_DELETE;
}
