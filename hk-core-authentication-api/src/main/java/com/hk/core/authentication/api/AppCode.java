package com.hk.core.authentication.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: kevin
 * @date 2018-07-12 16:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppCode implements Serializable {

    private String appId;

    private String appCode;

    private String appName;
}
