package com.xianyuli.bio.rpc.api.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private long id;

    private String name;

}
