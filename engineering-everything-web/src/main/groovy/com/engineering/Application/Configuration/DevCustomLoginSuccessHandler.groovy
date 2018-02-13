package com.engineering.Application.Configuration;

import api.user.User
import com.engineering.core.Service.ServiceUtilities;
import com.engineering.core.Service.UserValidationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory;
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

    private static Logger log = LoggerFactory.getLogger(DevCustomLoginSuccessHandler.class);

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    ServiceUtilities serviceUtilities;

    public DevCustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        log.info("<Authentication>[" + serviceUtilities.parseEmail(authentication) + "](authentication successfull)");
        if (session != null) {
            String redirectUrl = "/";
            try {
                User validateuser = userValidationService.validateuserexistence(authentication);
                if(validateuser == null)
                {
                    redirectUrl = "http://"+servicehost+":3000/#/register"
                }
                else if(validateuser.getUserrole().equals("teacher")) {
                    redirectUrl = "http://"+servicehost+":3000/#/teacher/"+validateuser.getBatches()[0];
                    log.info("<Authentication>["+validateuser.getEmail()+"](Teacher login)")
                }else if(validateuser.getUserrole().equals("student")){
                    redirectUrl = "http://"+servicehost+":3000/#/announcements"
                    log.info("<Authentication>["+validateuser.getEmail()+"](Student login)")
                }
            }catch (Exception e){
                log.error("encountered an exception"+ e);
            }
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}