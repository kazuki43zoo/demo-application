package com.kazuki43zoo.app.account;

import com.kazuki43zoo.app.auth.LoginForm;
import com.kazuki43zoo.app.auth.LoginSharedHelper;
import com.kazuki43zoo.core.exception.InvalidAccessException;
import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.core.security.CurrentUser;
import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.service.account.AccountSharedService;
import com.kazuki43zoo.domain.service.password.PasswordService;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

@TransactionTokenCheck("password")
@RequestMapping("password")
@Controller
@lombok.RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    private final LoginSharedHelper loginSharedHelper;

    private final AccountSharedService accountSharedService;

    private final Mapper beanMapper;

    @ModelAttribute
    public PasswordForm setUpPasswordForm(final @CurrentUser CustomUserDetails currentUser) {
        final PasswordForm form = new PasswordForm();
        if (currentUser != null) {
            form.setUsername(currentUser.getAccount().getAccountId());
        }
        return form;
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @GetMapping
    public String showChangeForm() {
        return "password/changeForm";
    }

    @PostMapping(params = "encourageChange")
    public String encourageChange(final PasswordForm form, final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(form);
        final Account account = this.accountSharedService.getAccount(form.getUsername());
        if (account != null) {
            final Message message = (account.getPasswordModifiedAt() == null)
                    ? Message.AUTH_ENCOURAGE_CHANGE_PASSWORD_NOT_INITIALIZED
                    : Message.AUTH_ENCOURAGE_CHANGE_PASSWORD_EXPIRED;
            redirectAttributes.addFlashAttribute(message.resultMessages());
        }
        return "redirect:/app/password";
    }

    @TransactionTokenCheck
    @PostMapping(params = "change")
    public String change(final @CurrentUser CustomUserDetails currentUser, final @Validated PasswordForm form, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        final Account changedAccount;
        try {
            String accountId = (currentUser != null) ? currentUser.getAccount().getAccountId() : form.getUsername();
            changedAccount = this.passwordService.change(accountId, form.getCurrentPassword(), form.getPassword());
        } catch (final ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        if (currentUser != null) {
            this.beanMapper.map(changedAccount, currentUser.getAccount());
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute(new LoginForm(form.getUsername(), null));
            redirectAttributes.addFlashAttribute(Message.PASSWORD_CHANGED.resultMessages());
            return "redirect:/app/auth/login";
        }
    }

    @TransactionTokenCheck
    @PostMapping(params = "changeAndLogin")
    public String changeAndLogin(final @CurrentUser CustomUserDetails currentUser, final @Validated PasswordForm form, final BindingResult bindingResult, final Model model) {

        if (currentUser != null) {
            throw new InvalidAccessException();
        }

        if (bindingResult.hasErrors()) {
            return showChangeForm();
        }

        try {
            this.passwordService.change(form.getUsername(), form.getCurrentPassword(), form.getPassword());
        } catch (final ResultMessagesNotificationException e) {
            model.addAttribute(e.getResultMessages());
            return showChangeForm();
        }

        return this.loginSharedHelper.generateAuthenticationProcessingUrl(form.getUsername());
    }

}
