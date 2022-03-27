package org.zxx.cloud.module.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtUserBO {

    String userId;
    String UserName;
    String UserType;
    String RoleId;
    String RoleName;
}
