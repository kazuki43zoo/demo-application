package com.github.kazuki43zoo.auth.password;

import javax.inject.Inject;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenContext;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import com.github.kazuki43zoo.auth.login.LoginForm;
import com.github.kazuki43zoo.core.exception.InvalidAccessException;
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.service.password.PasswordService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;

@TransactionTokenCheck("password")
@RequestMapping("password")
@Controller
public class PasswordController {

    @Inject
    PasswordService passwordService;

    @ModelAttribute
    public PasswordForm setUpPasswordForm(@AuthenticationPrincipal CustomUserDetails user) {
        PasswordForm form = new PasswordForm();
        if (user != null) {
            form.setAccountId(user.getAccount().getAccountId());
        }
        return form;
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET)
    public String showChangeForm() {
        return "auth/passwordChangeForm";
    }

    @RequestMapping(method = RequestMethod.GET, params = "encourageChange")
    public String encourageChange(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.AUTH_ENCOURAGE_CHANGE_PASSWORD
                .buildResultMessages());
        return "redirect:/auth/password";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "change")
    public String change(@AuthenticationPrincipal CustomUserDetails user,
            @Validated PasswordForm form, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        try {
            String accountId;
            if (user != null) {
                accountId = user.getAccount().getAccountId();
            } else {
                accountId = form.getAccountId();
            }
            passwordService.change(accountId, form.getOldPassword(), form.getPassword());
        } catch (ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        transactionTokenContext.removeToken();

        if (user != null) {
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.buildResultMessages());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute(new LoginForm(form.getAccountId(), null));
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.buildResultMessages());
            return "redirect:/auth/login";
        }
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "changeAndLogin")
    public String changeAndLogin(@AuthenticationPrincipal CustomUserDetails user,
            @Validated PasswordForm form, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {

        if (user != null) {
            throw new InvalidAccessException();
        }

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        try {
            passwordService.change(form.getAccountId(), form.getOldPassword(), form.getPassword());
        } catch (ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        transactionTokenContext.removeToken();

        return "forward:/auth/authenticate";
    }

}
