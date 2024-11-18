package ru.kartashov.employee_manager.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class AspectLogging {
    @Pointcut("execution(public * ru.kartashov.employee_manager.controller.*.*(..))")
    public void controllerLog() {}

    @Pointcut("execution(public * ru.kartashov.employee_manager.service.*.*(..))")
    public void serviceLog() {}

    @Before("controllerLog()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (request != null) {
            log.info("New request: IP - {}, URL: {}, HTTP_METHOD: {}, METHOD: {}.{}",
                    request.getRemoteAddr(),
                    request.getRequestURI(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
    }

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        String argString = args.length > 0 ? Arrays.toString(args) : " Method has no args";

        log.info("Run service: service method: {}.{}\n Method_arguments: [{}]",
                className, methodName, argString);
    }

    @AfterReturning(returning = "object", pointcut = "controllerLog()")
    public void  doAfterReturning(Object object) {
        log.info("Return value: {}" , object);
    }
}
