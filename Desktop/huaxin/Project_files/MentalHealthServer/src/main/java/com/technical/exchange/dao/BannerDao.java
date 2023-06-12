package com.technical.exchange.dao;

import com.technical.exchange.bean.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//轮播图
@Repository
public interface BannerDao extends JpaRepository<Banner, Integer> {

}
