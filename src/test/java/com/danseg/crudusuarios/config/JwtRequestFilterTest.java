package com.danseg.crudusuarios.config;

import com.danseg.crudusuarios.service.impl.UserDetailsServiceImpl;
import com.danseg.crudusuarios.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtRequestFilterTest {

    private MockMvc mockMvc;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize the filter with the mocks manually
        jwtRequestFilter = new JwtRequestFilter(userDetailsService, jwtTokenUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .addFilters(jwtRequestFilter)
                .build();
    }

    @Test
    void doFilterInternalTest() throws Exception {
        String jwt = "string_jwt";
        String email = "test@example.com";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenUtil.extractUsername(jwt)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(jwt, userDetails)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(email);
        verify(jwtTokenUtil, times(1)).validateToken(jwt, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    static class TestController {}
}
