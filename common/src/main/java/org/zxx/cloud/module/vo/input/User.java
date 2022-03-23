package org.zxx.cloud.module.vo.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    //序列化版本号
    private static final long serialVersionUID = 1L;


    int id;
    String name;
    int age;

}
