package com.auction.userservice.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around(
        "execution(* com.auction.userservice.controller..*(..))"
    )
    public Object logController(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "CONTROLLER"
        );
    }

    @Around(
        "execution(* com.auction.userservice.service..*(..))"
    )
    public Object logService(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "SERVICE"
        );
    }

    private Object logExecution(

            ProceedingJoinPoint joinPoint,

            String layer

    ) throws Throwable {

        String className =
                joinPoint.getSignature()
                        .getDeclaringTypeName();

        String methodName =
                joinPoint.getSignature()
                        .getName();

        long startTime =
                System.currentTimeMillis();

        log.info(
                "[{}] Started -> {}.{}()",
                layer,
                className,
                methodName
        );

        log.debug(
                "[{}] Arguments -> {}",
                layer,
                Arrays.toString(
                        joinPoint.getArgs()
                )
        );

        try {

            Object result =
                    joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "[{}] Completed -> {}.{}() | {} ms",
                    layer,
                    className,
                    methodName,
                    executionTime
            );

            return result;

        } catch (Throwable ex) {

            log.error(
                    "[{}] Exception -> {}.{}() | {}",
                    layer,
                    className,
                    methodName,
                    ex.getMessage(),
                    ex
            );

            throw ex;
        }
    }
}