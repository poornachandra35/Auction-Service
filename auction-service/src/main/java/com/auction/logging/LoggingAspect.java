package com.auction.logging;

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
    // CONTROLLER LAYER LOGGING
    // =====================================================

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

    // =====================================================
    // SERVICE LAYER LOGGING
    // =====================================================

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

    // =====================================================
    // SCHEDULER LAYER LOGGING
    // =====================================================

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

    // =====================================================
    // COMMON CENTRALIZED LOGGING METHOD
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

        Object[] arguments =
                joinPoint.getArgs();

        long startTime =
                System.currentTimeMillis();

        // =====================================================
        // METHOD START LOG
        // =====================================================

        log.info(
                "[{}] Method Started -> {}.{}()",
                layer,
                className,
                methodName
        );

        // =====================================================
        // METHOD ARGUMENTS LOG
        // =====================================================

        log.debug(
                "[{}] Arguments -> {}",
                layer,
                Arrays.toString(arguments)
        );

        try {

            // =====================================================
            // EXECUTE ORIGINAL METHOD
            // =====================================================

            Object result =
                    joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis()
                            - startTime;

            // =====================================================
            // METHOD COMPLETION LOG
            // =====================================================

            log.info(
                    "[{}] Method Completed -> {}.{}() | Execution Time: {} ms",
                    layer,
                    className,
                    methodName,
                    executionTime
            );

            // =====================================================
            // RETURN VALUE LOG
            // =====================================================

            log.debug(
                    "[{}] Return Value -> {}",
                    layer,
                    result
            );

            return result;

        } catch (Throwable ex) {

            long executionTime =
                    System.currentTimeMillis()
                            - startTime;

            // =====================================================
            // EXCEPTION LOG
            // =====================================================

            log.error(
                    "[{}] Exception in {}.{}() | Execution Time: {} ms | Message: {}",
                    layer,
                    className,
                    methodName,
                    executionTime,
                    ex.getMessage(),
                    ex
            );

            throw ex;
        }
    }
}