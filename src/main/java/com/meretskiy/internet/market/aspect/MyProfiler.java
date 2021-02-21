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

        log.info(
                methodCountMap.entrySet().stream()
                        .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                        .limit(1)
                        .collect(Collectors.toList())
                        .toString()
        );
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

        Map.Entry<String, Long> maxTimeDuration =
                executionTimeMap.entrySet().stream().max((e1, e2) -> e1.getValue() > e2.getValue() ? 1 : -1).get();
        log.info("[" + maxTimeDuration.getKey() + "] : [" + maxTimeDuration.getValue() + "]");

        return out;
    }
}
