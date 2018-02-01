package com.engineering.Application.Configuration;

import api.user.User;
import com.engineering.core.Service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class DevCustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserValidationService userValidationService;

    @Value('${engineering.everything.host}')
    private String servicehost;

    public DevCustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println("session object is " + session.toString());
        if (session != null) {
            String redirectUrl = "/";
            try {
              //  UserRegGoogleContoller ug = new UserRegGoogleContoller();
                System.out.print("AUthentication object is" + authentication);
                User validateuser = userValidationService.validateuserexistence(authentication);
                System.out.println("validate usr value" + validateuser);
                if(validateuser == null)
                {
                    redirectUrl = "http://"+servicehost+":3000/#/register";
                }
                else if(validateuser.getUserrole().equals("teacher")) {
                    redirectUrl = "http://"+servicehost+":3000/#/teacher/"+validateuser.getBatches()[0];
                }else if(validateuser.getUserrole().equals("student")){
                    redirectUrl = "http://"+servicehost+":3000/#/announcements";
                }
            }catch (Exception e){
                System.out.print("encountered an exception"+ e);
            }
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}