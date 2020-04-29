package com.xun.wang.vlog.chat.model.entity.common;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xun.wang.vlog.chat.model.entity.BaseEntity;

import lombok.Data;

/**
 * @ClassName DictType
 * @Description 字典表类型
 * @Author xun.d.wang
 * @Date 2020/4/15 17:40
 * @Version 1.0
 **/
@Data
@Entity
@Table(name = "t_sys_dict_type")
public class SysDictTypeEntity extends BaseEntity {

    @Column(nullable = false, columnDefinition = "varchar(255) COMMENT '字典类型名称'")
    private String name;

    @OneToMany(mappedBy = "sysDictTypeEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SysDictEntity> sysDicts;
}
