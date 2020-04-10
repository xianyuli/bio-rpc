package com.xianyuli.bio.rpc.provider.impl;

import com.xianyuli.bio.rpc.api.entity.User;
import com.xianyuli.bio.rpc.api.service.IUserService;

public class UserServiceImpl implements IUserService {
    @Override
    public User findById(long id) {
        User user = new User();
        user.setId(id);
        user.setName("xianyuli");
        return user;
    }
}
