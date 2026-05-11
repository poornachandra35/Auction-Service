package com.auction.notification_service.aop;

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

    // =====================================================
    // CONTROLLER LOGGING
    // =====================================================

    @Around(
        "execution(* com.auction.notification_service.controller..*(..))"
    )
    public Object logController(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "CONTROLLER"
        );
    }

    // =====================================================
    // SERVICE LOGGING
    // =====================================================

    @Around(
        "execution(* com.auction.notification_service.service..*(..))"
    )
    public Object logService(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "SERVICE"
        );
    }

    // =====================================================
    // COMMON LOGGER
    // =====================================================

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