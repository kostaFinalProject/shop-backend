package com.example.shop.aop;

import com.example.shop.config.CustomUserDetails;
import com.example.shop.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class SecurityAspect {

    private final ValidationService validationService;
    private static final ThreadLocal<Long> memberIdHolder = new ThreadLocal<>();

    @Around("execution(* com.example.shop.controller.*.*(..)) && "
    +"!@annotation(com.example.shop.aop.PublicApi) && "
    + "!@annotation(com.example.shop.aop.TokenApi)")
    public Object injectMemberId(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("인증이 필요합니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        Long memberId = validationService.validateMemberByUserId(userId).getId();
        memberIdHolder.set(memberId);

        try {
            return joinPoint.proceed();
        } finally {
            memberIdHolder.remove();
        }
    }

    public static Long getCurrentMemberId() {
        return memberIdHolder.get();
    }
}
