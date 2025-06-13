package com.kumagaya.controller;

import com.kumagaya.entity.User;
import com.kumagaya.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    
    @Autowired
    private UserMapper userMapper; //为了演示方便，直接使用Mapper,没有使用Service层
    
    @PostMapping
//    @CachePut(cacheNames = "user", key = "#user.id")
    @CachePut(cacheNames = "userCache", key = "#result.id")
    // key的生成:userCache::id ===> cacheNames+"::" + key
    // 方法执行后将结果存入缓存
    public User save(@RequestBody User user) {
        userMapper.insert(user);
        return user;
    }
    
    @GetMapping
    @Cacheable(cacheNames = "userCache", key = "#id") // key的生成:userCache::10
    // 方法执行前,先去缓存中查找，如果有则直接返回缓存中的数据
    // 此处的key来自方法参数id
    // 如果缓存中没有，会通过反射 执行方法，并将结果存入缓存,下次查就不会再执行方法了
    public User getById(Long id) {
        return userMapper.getById(id);
    }
    
    @DeleteMapping
    @CacheEvict(cacheNames = "userCache", key = "#id") // key的生成:userCache::10
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
    
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @DeleteMapping("/delAll")
    public void deleteAll() {
        userMapper.deleteAll();
    }

}
