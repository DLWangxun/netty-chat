package com.xun.wang.vlog.chat.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @ClassName BaseRepository
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/4/17 15:15
 * @Version 1.0
 **/
@NoRepositoryBean
public interface BaseRepository <T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
