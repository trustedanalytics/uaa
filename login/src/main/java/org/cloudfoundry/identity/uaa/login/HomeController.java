/*******************************************************************************
 *     Cloud Foundry 
 *     Copyright (c) [2009-2014] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package org.cloudfoundry.identity.uaa.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends AbstractControllerInfo {
    private static final String LINK_FIELD = "login-link";

    private final Log logger = LogFactory.getLog(getClass());
    protected final Environment environment;
    @Autowired
    private TileInfo tileInfo;

    public HomeController(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping(value = { "/", "/home" })
    public String home(Model model, Principal principal) {
        List<Map<String, String>> tiles = tileInfo.getLoginTiles();
        if (tiles != null && tiles.size() == 1) {
            String url = tiles.get(0).get(LINK_FIELD);
            if (url != null && url.length() > 0) {
                return "redirect:" + url;
            }
        }

        model.addAttribute("principal", principal);
        model.addAttribute("tiles", tiles);
        boolean invitationsEnabled = "true".equalsIgnoreCase(environment.getProperty("login.invitationsEnabled"));
        if (invitationsEnabled) {
            model.addAttribute("invitationsLink", "/invitations/new");
        }
        populateBuildAndLinkInfo(model);
        return "home";
    }

    @RequestMapping("/error500")
    public String error500(Model model, HttpServletRequest request) {
        logger.error("Internal error", (Throwable) request.getAttribute("javax.servlet.error.exception"));

        populateBuildAndLinkInfo(model);
        return "error";
    }

    @RequestMapping("/error404")
    public String error404(Model model) {
        populateBuildAndLinkInfo(model);
        return "error";
    }
}
