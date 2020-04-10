package com.xianyuli.bio.rpc.api.service;

import com.xianyuli.bio.rpc.api.entity.User;

/**
 * @author LW
 */
public interface IUserService {
    /**
     * ID查询用户
     * @param id
     * @return
     */
    User findById(long id);
}
