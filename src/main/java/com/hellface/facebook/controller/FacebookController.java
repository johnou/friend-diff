package com.hellface.facebook.controller;

import com.hellface.facebook.dao.Account;
import com.hellface.facebook.dao.AccountService;
import com.hellface.facebook.dao.Friend;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.restfb.FacebookClient.AccessToken;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@Controller
@PropertySource("classpath:application.properties")
public class FacebookController {

    private static final String TOKEN_NAME = "fb_token";

    @Autowired
    private FacebookClient facebookClient;

    @Autowired
    private AccountService accountService;

    @Value("${facebook.apiId}")
    private String applicationId;

    @Value("${facebook.apiSecret}")
    private String applicationSecret;

    @Value("${facebook.redirectUri}")
    private String redirectUri;

    @RequestMapping("/")
    ModelAndView index(HttpServletResponse response, @CookieValue(value = TOKEN_NAME, defaultValue = "") String accessToken) {
        ModelMap model = new ModelMap();
        boolean authenticated = StringUtils.hasText(accessToken);
        if (authenticated) {
            try {
                fetchFriendConnections(accessToken, model);
            } catch (FacebookOAuthException e) {
                model.addAttribute("actionErrors", Collections.singletonList(e.getLocalizedMessage()));
                invalidateToken(response);
                authenticated = false;
            }
        }

        model.addAttribute("applicationId", applicationId);
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("redirectUri", redirectUri);

        return new ModelAndView("index", model);
    }

    private void invalidateToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void fetchFriendConnections(String accessToken, ModelMap model) {
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(accessToken, applicationSecret, Version.VERSION_2_2);
        User user = facebookClient.fetchObject("me", User.class);

        Connection<User> connections = facebookClient.fetchConnection("/me/friends", User.class);
        model.addAttribute("totalFriends", connections.getTotalCount());

        List<Friend> friends = fetchFriendConnections(facebookClient);
        Collections.sort(friends, FRIEND_COMPARATOR);
        model.addAttribute("friends", friends);

        List<Account> oldAccounts = accountService.findByIdentifier(user.getId());

        List<Friend> addedFriends = findAddedFriends(friends, oldAccounts);
        List<Friend> removedFriends = findRemovedFriends(friends, oldAccounts);

        model.addAttribute("removedFriends", removedFriends);
        model.addAttribute("addedFriends", addedFriends);

        if (oldAccounts.isEmpty() || removedFriends.size() > 0 || addedFriends.size() > 0) {
            accountService.save(user.getId(), friends);
        }
    }

    private static final Comparator<Friend> FRIEND_COMPARATOR = new Comparator<Friend>() {
        @Override
        public int compare(Friend o1, Friend o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };

    private List<Friend> findAddedFriends(List<Friend> friends, List<Account> oldAccounts) {
        List<Friend> addedFriends = new ArrayList<Friend>();
        if (!oldAccounts.isEmpty()) {
            addedFriends.addAll(friends);
            addedFriends.removeAll(getLast(oldAccounts).getFriends());
        }
        return addedFriends;
    }

    private List<Friend> findRemovedFriends(List<Friend> friends, List<Account> oldAccounts) {
        List<Friend> removedFriends = new ArrayList<Friend>();
        if (!oldAccounts.isEmpty()) {
            removedFriends.addAll(getLast(oldAccounts).getFriends());
            removedFriends.removeAll(friends);
        }
        return removedFriends;
    }

    private <T> T getLast(List<T> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(list.size() - 1);
    }

    private List<Friend> fetchFriendConnections(DefaultFacebookClient facebookClient) {
        List<Friend> friends = new ArrayList<Friend>(50);
        Connection<User> connections = facebookClient.fetchConnection("/me/taggable_friends", User.class);
        for (User connection : connections.getData()) {
            User.Picture picture = connection.getPicture();
            friends.add(new Friend(connection.getName(), connection.getName(), picture != null ? picture.getUrl() : ""));
        }
        return friends;
    }

    @RequestMapping("/authenticate")
    String authenticate(HttpServletResponse response, @RequestParam(value = "code", required = false) String code,
                        @RequestParam(value = "error_description", required = false) String error,
                        RedirectAttributes redirectAttributes) {
        if (StringUtils.hasText(error)) {
            redirectAttributes.addFlashAttribute("actionErrors", Collections.singletonList(error));
            return "redirect:/";
        }

        AccessToken accessToken = facebookClient.obtainUserAccessToken(applicationId, applicationSecret, redirectUri, code);
        Cookie cookie = new Cookie(TOKEN_NAME, accessToken.getAccessToken());
        cookie.setMaxAge(calculateMaxAge(accessToken));
        response.addCookie(cookie);
        return "redirect:/";
    }

    private int calculateMaxAge(AccessToken accessToken) {
        long expiry = accessToken.getExpires().getTime() - System.currentTimeMillis();
        return (int) TimeUnit.MILLISECONDS.toSeconds(expiry);
    }

    @Bean
    public FacebookClient facebookClient() {
        return new DefaultFacebookClient(applicationSecret, Version.VERSION_2_2);
    }
}
