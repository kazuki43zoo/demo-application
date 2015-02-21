package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.app.auth.LoginForm;
import com.github.kazuki43zoo.app.auth.LoginSharedHelper;
import com.github.kazuki43zoo.core.exception.InvalidAccessException;
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.service.password.PasswordService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.web.security.CurrentUser;
import org.dozer.Mapper;
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

import javax.inject.Inject;

@TransactionTokenCheck("password")
@RequestMapping("password")
@Controller
public class PasswordController {

    @Inject
    PasswordService passwordService;

    @Inject
    LoginSharedHelper loginSharedHelper;

    @Inject
    Mapper beanMapper;

    @ModelAttribute
    public PasswordForm setUpPasswordForm(@CurrentUser CustomUserDetails currentUser) {
        PasswordForm form = new PasswordForm();
        if (currentUser != null) {
            form.setAccountId(currentUser.getAccount().getAccountId());
        }
        return form;
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET)
    public String showChangeForm() {
        return "password/changeForm";
    }

    @RequestMapping(method = RequestMethod.GET, params = "encourageChange")
    public String encourageChange(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Message.AUTH_ENCOURAGE_CHANGE_PASSWORD
                .resultMessages());
        return "redirect:/password";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "change")
    public String change(
            @CurrentUser CustomUserDetails currentUser,
            @Validated PasswordForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            TransactionTokenContext transactionTokenContext) {

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        Account changedAccount;
        try {
            String accountId;
            if (currentUser != null) {
                accountId = currentUser.getAccount().getAccountId();
            } else {
                accountId = form.getAccountId();
            }
            changedAccount = passwordService.change(accountId, form.getOldPassword(),
                    form.getPassword());
        } catch (ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        transactionTokenContext.removeToken();

        if (currentUser != null) {
            beanMapper.map(changedAccount, currentUser.getAccount());
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute(new LoginForm(form.getAccountId(), null));
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/auth/login";
        }
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "changeAndLogin")
    public String changeAndLogin(
            @CurrentUser CustomUserDetails currentUser,
            @Validated PasswordForm form,
            BindingResult bindingResult,
            Model model,
            TransactionTokenContext transactionTokenContext) {

        if (currentUser != null) {
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

        return loginSharedHelper.generateAuthenticationProcessingUrl(form.getAccountId());
    }

}
