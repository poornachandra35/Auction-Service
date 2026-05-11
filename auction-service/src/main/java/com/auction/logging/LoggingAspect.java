package com.auction.logging;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // =========================================
    // CONTROLLER LAYER LOGGING
    // =========================================
    @Around(
            "execution(* com.auction.controller..*(..))"
    )
    public Object logControllerMethods(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "CONTROLLER"
        );
    }

    // =========================================
    // SERVICE LAYER LOGGING
    // =========================================
    @Around(
            "execution(* com.auction.service..*(..))"
    )
    public Object logServiceMethods(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "SERVICE"
        );
    }

    // =========================================
    // SCHEDULER LOGGING
    // =========================================
    @Around(
            "execution(* com.auction.scheduler..*(..))"
    )
    public Object logSchedulerMethods(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        return logExecution(
                joinPoint,
                "SCHEDULER"
        );
    }

    // =========================================
    // COMMON LOGGING METHOD
    // =========================================
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
                "[{}] Method started: {}.{}",
                layer,
                className,
                methodName
        );

        try {

            Object result =
                    joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "[{}] Method completed: {}.{} | Execution Time: {} ms",
                    layer,
                    className,
                    methodName,
                    executionTime
            );

            return result;

        } catch (Exception ex) {

            log.error(
                    "[{}] Exception in {}.{} | Message: {}",
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