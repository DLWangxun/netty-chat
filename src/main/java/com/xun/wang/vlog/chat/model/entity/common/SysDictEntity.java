package com.xun.wang.vlog.chat.model.entity.common;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xun.wang.vlog.chat.model.entity.BaseEntity;

import lombok.Data;

/**
 * @ClassName SysDict
 * @Description 数据字典
 * @Author xun.d.wang
 * @Date 2020/4/15 17:41
 * @Version 1.0
 **/

@Data
@Entity
@Table(name = "t_sys_dict")

public class SysDictEntity  extends BaseEntity {


    @Column( nullable = false, columnDefinition = "varchar(255) COMMENT '字典名称'")
    private String name;

    @Column( nullable = false, columnDefinition = "varchar(255) COMMENT '字典值'")
    private String value;

    @Column(nullable = false, columnDefinition = "TINYINT COMMENT '排序'")
    private int seq;

    @ManyToOne(cascade={ CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="typeCode")
    private SysDictTypeEntity sysDictTypeEntity;

}
