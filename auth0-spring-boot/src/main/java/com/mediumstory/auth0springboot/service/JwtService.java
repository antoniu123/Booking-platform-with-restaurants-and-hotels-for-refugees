package com.mediumstory.auth0springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.shaded.json.JSONArray;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public List<String> getRoles() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Map<String, Object> myClaims = authenticationToken.getToken().getClaims();
        JSONArray roles = (JSONArray) myClaims.get("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
        List<String> exampleList = new ArrayList<String>();
        for (int i = 0; i < roles.size(); i++) {
            exampleList.add(roles.get(i).toString());
        }
        return exampleList;
    }

    public String getUser(){
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        return authenticationToken.getTokenAttributes()
                .get("sub")
                .toString()
                .substring(authenticationToken.getTokenAttributes().get("sub").toString().indexOf("|") + 1,
                        authenticationToken.getTokenAttributes().get("sub").toString().length() - 1);
    }


}
