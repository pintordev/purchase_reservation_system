package com.pintor.member_module.common.filter;

import com.pintor.member_module.common.errors.exception.ApiResException;
import com.pintor.member_module.common.response.FailCode;
import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.util.EndpointEntries;
import com.pintor.member_module.domain.auth.service.AuthService;
import com.pintor.member_module.domain.member.response.MemberPrincipalResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final EndpointEntries endpointEntries;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (this.endpointEntries.isPermitAll(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (this.endpointEntries.isInternal(request)) {
            this.checkServerToken(request, response);
        } else {
            this.checkBearerToken(request, response);
        }
        filterChain.doFilter(request, response);
    }

    private void checkBearerToken(HttpServletRequest request, HttpServletResponse response) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.UNAUTHORIZED
                    )
            );
        }

        MemberPrincipalResponse passport = this.authService.getMemberPrincipalByAuthToken(bearerToken);

        request.setAttribute("X-Member-Id", passport.id().toString());
    }

    private void checkServerToken(HttpServletRequest request, HttpServletResponse response) {

        String serverToken = request.getHeader("X-Server-Token");

        if (serverToken == null) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN
                    )
            );
        }

        if (!this.redisTemplate.opsForValue().get("server_token").equals(serverToken)) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN
                    )
            );
        }
    }
}
