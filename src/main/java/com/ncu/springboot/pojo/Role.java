package com.ncu.springboot.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Role  {
    private static final long serialVersionUID = 4788839955936593560L;
    @Id // 主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 自增策略
    private Long id; // 实体一个唯一标识
    private String name;
    private String roleType;
    private String remark;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
