package com.dxj.module.security.controller;

import com.dxj.annotation.Log;
import com.dxj.annotation.rest.AnonymousPostMapping;
import com.dxj.config.RsaProperties;
import com.dxj.module.security.config.bean.LoginProperties;
import com.dxj.module.security.config.bean.SecurityProperties;
import com.dxj.module.security.domain.dto.AuthUserDTO;
import com.dxj.module.security.domain.dto.JwtUserDTO;
import com.dxj.module.security.domain.entity.TokenProvider;
import com.dxj.module.security.service.OnlineUserService;
import com.dxj.util.RedisUtils;
import com.dxj.util.RsaUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/remote/auth")
@RequiredArgsConstructor
@Api(tags = "系统：远程授权接口")
public class RemoteAuthController {
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Resource
    private LoginProperties loginProperties;
    @Log("用户登录")
    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDTO authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDTO jwtUserDto = (JwtUserDTO) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return ResponseEntity.ok(authInfo);
    }
}
