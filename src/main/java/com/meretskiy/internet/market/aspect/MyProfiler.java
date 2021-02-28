package com.meretskiy.internet.market.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class MyProfiler {

    public Map<String, Integer> methodCountMap;
    public Map<String, Long> executionTimeMap;

    @PostConstruct
    public void init() {
        methodCountMap = new HashMap<>();
        executionTimeMap = new HashMap<>();
    }

    /**
     * После выполнения каждого метода в пакете market:
     * выводит имя количество выполнений метода с наибольшим количеством выполнений.
     *
     * @param joinPoint
     */
    @Before("execution(public * com.meretskiy.internet.market..* (..))")
    public void countMethods(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().toString();
        int count = 1;

        if (methodCountMap.containsKey(methodName)) {
            count += methodCountMap.get(methodName);
        }
        methodCountMap.put(methodName, count);
        log.info(String.valueOf(methodCountMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get()));
    }

    /**
     * После выполнения каждого метода в пакете controllers:
     * выводит имя и суммарное время выполнения метода который занимает по выполнению больше всего времени.
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(public * com.meretskiy.internet.market.controllers..*(..))")
    public Object countMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long begin = System.currentTimeMillis();
        Object out = proceedingJoinPoint.proceed();
        long duration = System.currentTimeMillis() - begin;

        String methodName = proceedingJoinPoint.getSignature().toString();

        if (executionTimeMap.containsKey(methodName)) {
            duration += executionTimeMap.get(methodName);
        }
        executionTimeMap.put(methodName, duration);
        log.info(String.valueOf(executionTimeMap.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).get()));
        return out;
    }
}
