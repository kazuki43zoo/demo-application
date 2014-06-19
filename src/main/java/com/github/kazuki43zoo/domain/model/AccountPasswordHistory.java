package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountPasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String password;
    private Date createdAt;

}
