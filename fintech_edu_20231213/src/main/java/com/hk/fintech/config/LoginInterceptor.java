package com.hk.fintech.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("LoginInterceptor 실행");
		
		HttpSession session = request.getSession();
		if(session.getAttribute("ldto")==null) {
			System.out.println("로그인상태가 아님");
			response.sendRedirect("/");
			return false;// false이면 컨트롤러로 진입못함
		}
		
		return true;// true이면 컨트롤러로 진입함
	}
	
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		// TODO Auto-generated method stub
//		AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}
//	
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		// TODO Auto-generated method stub
//		AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
//	
//	@Override
//	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		// TODO Auto-generated method stub
//		AsyncHandlerInterceptor.super.afterConcurrentHandlingStarted(request, response, handler);
//	}
	
}
