package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.app.auth.LoginForm;
import com.github.kazuki43zoo.app.auth.LoginSharedHelper;
import com.github.kazuki43zoo.core.exception.InvalidAccessException;
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.core.security.CurrentUser;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;
import com.github.kazuki43zoo.domain.service.password.PasswordService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
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
    AccountSharedService accountSharedService;

    @Inject
    Mapper beanMapper;

    @ModelAttribute
    public PasswordForm setUpPasswordForm(@CurrentUser CustomUserDetails currentUser) {
        PasswordForm form = new PasswordForm();
        if (currentUser != null) {
            form.setUsername(currentUser.getAccount().getAccountId());
        }
        return form;
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET)
    public String showChangeForm() {
        return "password/changeForm";
    }

    @RequestMapping(method = RequestMethod.POST, params = "encourageChange")
    public String encourageChange(PasswordForm form, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(form);
        Account account = accountSharedService.getAccount(form.getUsername());
        if (account != null) {
            Message message = (account.getPasswordModifiedAt() == null)
                    ? Message.AUTH_ENCOURAGE_CHANGE_PASSWORD_NOT_INITIALIZED
                    : Message.AUTH_ENCOURAGE_CHANGE_PASSWORD_EXPIRED;
            redirectAttributes.addFlashAttribute(message.resultMessages());
        }
        return "redirect:/app/password";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "change")
    public String change(@CurrentUser CustomUserDetails currentUser,
            @Validated PasswordForm form, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        Account changedAccount;
        try {
            String accountId = (currentUser != null) ? currentUser.getAccount().getAccountId() : form.getUsername();
            changedAccount = passwordService.change(accountId, form.getCurrentPassword(), form.getPassword());
        } catch (ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        if (currentUser != null) {
            beanMapper.map(changedAccount, currentUser.getAccount());
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute(new LoginForm(form.getUsername(), null));
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/app/auth/login";
        }
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.POST, params = "changeAndLogin")
    public String changeAndLogin(@CurrentUser CustomUserDetails currentUser,
            @Validated PasswordForm form, BindingResult bindingResult, Model model) {

        if (currentUser != null) {
            throw new InvalidAccessException();
        }

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        try {
            passwordService.change(form.getUsername(), form.getCurrentPassword(), form.getPassword());
        } catch (ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        return loginSharedHelper.generateAuthenticationProcessingUrl(form.getUsername());
    }

}
