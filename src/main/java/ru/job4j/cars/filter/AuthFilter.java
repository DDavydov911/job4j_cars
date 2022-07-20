package ru.job4j.cars.filter;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (uri.endsWith("login") || uri.endsWith("index")
                || uri.endsWith("registration") || uri.contains("carPhoto")) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null
                || req.getSession().getAttribute("user.name") == "Гость") {
            res.sendRedirect(req.getContextPath() + "/index");
            return;
        }
        chain.doFilter(req, res);
    }
}
*/