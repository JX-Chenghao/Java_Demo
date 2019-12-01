package com.ncu.springboot.pojo;

import lombok.Data;
//@Entity
@Data
public class Permission extends BaseBo{
    private static final long serialVersionUID = 4788839955936593560L;
    //@Id // 主键
    //@GeneratedValue(strategy= GenerationType.IDENTITY) // 自增策略
    //private Long id; // 实体一个唯一标识
    private String name;
    private String remark;
   /* @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;*/
}
