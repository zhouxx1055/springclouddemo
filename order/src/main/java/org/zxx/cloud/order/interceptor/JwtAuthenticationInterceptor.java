package org.zxx.cloud.order.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.zxx.cloud.exception.JwtException;
import org.zxx.cloud.module.dto.UserDTO;
import org.zxx.cloud.order.conf.PassToken;
import org.zxx.cloud.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;



public class JwtAuthenticationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        } else {//默认全部检查
            // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
            String token = httpServletRequest.getHeader("token");


            // 执行认证
            if (token == null) {
                //这里其实是登录失效,没token了   这个错误也是我自定义的，读者需要自己修改
                throw new JwtException(11, "未认证，请登录！");
            }

            // 获取 token 中的 user Name
            String userId = JwtUtils.getAudience(token);

            //通过userid 看是否有这个user   因为我们需要检查用户是否存在，读者可以自行修改逻辑
            UserDTO user = null;//accountService.getByUserName(userId);

            if (user == null) {
                //这个错误也是我自定义的
                throw new JwtException(12, "未注册用户，请注册！");
            }

            // 验证 token
            JwtUtils.verifyToken(token, userId);

            //获取载荷内容
            String userName = JwtUtils.getClaimByName(token, "userName").asString();
            String realName = JwtUtils.getClaimByName(token, "realName").asString();

            //放入attribute以便后面调用
//            request.setAttribute("userName", userName);
//            request.setAttribute("realName", realName);


            return true;

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}