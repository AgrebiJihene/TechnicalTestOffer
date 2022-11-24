package com.example.user.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * It is an aspect class that is used to enable the spring AOP functionality
 */
@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Declares the around advice that is applied before and after the method matching with a pointcut expression
     * Logs input and output arguments of each method as well as the processing time
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Object
     * @throws Throwable if there is any Exception
     */
    @Around("tracePointcut()")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        if (log.isInfoEnabled()) {
            log.info("Enter: {}.{}() with argument[s] = {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(), Arrays.toString(proceedingJoinPoint.getArgs()));
        }
        try {
            StopWatch stopWatch = new StopWatch(methodSignature.getDeclaringType().getSimpleName() + "->" + methodSignature.getName());
            stopWatch.start(methodSignature.getName());
            Object result = proceedingJoinPoint.proceed();
            stopWatch.stop();
            if (log.isInfoEnabled()) {
                log.info("Exit: {}.{}() with result = {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                        proceedingJoinPoint.getSignature().getName(), result);
                log.info(stopWatch.prettyPrint());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}({})",
                    proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName(), Arrays.toString(proceedingJoinPoint.getArgs()));
            throw e;
        }

    }

    /**
     * Displays all the available methods of UserController, the around advice will be called for all the methods
     */
    @Pointcut("execution(* com.example.user.controllers.UserController.*(..))")
    public void tracePointcut() {
    }

}
