package com.tayobus.sugamaga.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuthException;
import com.tayobus.sugamaga.api.common.utils.TokenUtils;
import com.tayobus.sugamaga.api.request.UserCustomRequest;
import com.tayobus.sugamaga.api.service.UserService;
import com.tayobus.sugamaga.db.entity.UserCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Api(tags = "유저 API")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "유저 커스텀 조회", description = "user custom info get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "firebase 로그인 성공 후 " +
                    "발급 받은 accessToken",
                    required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "refreshToken", value = "firebase 로그인 성공 후 " +
                    "발급 받은 refreshToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/custom")
    public ResponseEntity<?> getCustom(HttpServletRequest httpServletRequest) throws FirebaseAuthException {
        logger.info("get custom");

        String uid = TokenUtils.getInstance()
                .getUid( httpServletRequest.getHeader("accessToken"));

        try {
            UserCustom userCustom = userService.getUserCustom(uid);

            return new ResponseEntity<>(userCustom, HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.toString());

            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "유저 커스텀 수정", description = "user custom info get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "firebase 로그인 성공 후 발급 받은 accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping ("/custom")
    public ResponseEntity<?> putCustom(@RequestBody UserCustomRequest userCustomRequest, HttpServletRequest httpServletRequest) throws FirebaseAuthException {
        logger.info("put custom");

        String uid = TokenUtils.getInstance().getUid(httpServletRequest.getHeader("accessToken"));

        try {
            userService.putUserCustom(uid, userCustomRequest);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.toString());

            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
    }


}