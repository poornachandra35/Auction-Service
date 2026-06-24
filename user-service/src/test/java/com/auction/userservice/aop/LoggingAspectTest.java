package com.auction.userservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingAspectTest {

    private final LoggingAspect loggingAspect =
            new LoggingAspect();

    // ================= CONTROLLER =================

    @Test
    void testLogController() throws Throwable {

        ProceedingJoinPoint joinPoint =
                mock(ProceedingJoinPoint.class);

        Signature signature =
                mock(Signature.class);

        when(joinPoint.getSignature())
                .thenReturn(signature);

        when(signature.getDeclaringTypeName())
                .thenReturn("AuthController");

        when(signature.getName())
                .thenReturn("login");

        when(joinPoint.getArgs())
                .thenReturn(new Object[]{"test"});

        when(joinPoint.proceed())
                .thenReturn("success");

        Object result =
                loggingAspect.logController(joinPoint);

        assertEquals("success", result);
    }

    // ================= SERVICE =================

    @Test
    void testLogService() throws Throwable {

        ProceedingJoinPoint joinPoint =
                mock(ProceedingJoinPoint.class);

        Signature signature =
                mock(Signature.class);

        when(joinPoint.getSignature())
                .thenReturn(signature);

        when(signature.getDeclaringTypeName())
                .thenReturn("AuthService");

        when(signature.getName())
                .thenReturn("register");

        when(joinPoint.getArgs())
                .thenReturn(new Object[]{"test"});

        when(joinPoint.proceed())
                .thenReturn("success");

        Object result =
                loggingAspect.logService(joinPoint);

        assertEquals("success", result);
    }

    // ================= EXCEPTION =================

    @Test
    void testLogServiceException() throws Throwable {

        ProceedingJoinPoint joinPoint =
                mock(ProceedingJoinPoint.class);

        Signature signature =
                mock(Signature.class);

        when(joinPoint.getSignature())
                .thenReturn(signature);

        when(signature.getDeclaringTypeName())
                .thenReturn("AuthService");

        when(signature.getName())
                .thenReturn("register");

        when(joinPoint.getArgs())
                .thenReturn(new Object[]{"test"});

        when(joinPoint.proceed())
                .thenThrow(
                        new RuntimeException("Error")
                );

        assertThrows(
                RuntimeException.class,
                () -> loggingAspect.logService(joinPoint)
        );
    }
}