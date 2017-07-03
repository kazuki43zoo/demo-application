package com.kazuki43zoo.app.account;

import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.core.security.CurrentUser;
import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.service.account.AccountService;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.dozer.Mapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.inject.Inject;

@TransactionTokenCheck("profile")
@RequestMapping("profile")
@Controller
public class ProfileController {

    @Inject
    AccountService accountService;

    @Inject
    Mapper beanMapper;

    @GetMapping
    public String show(final @CurrentUser CustomUserDetails authenticatedUser, final Model model) {
        model.addAttribute(authenticatedUser.getAccount());
        return "profile/detail";
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @GetMapping(params = "edit")
    public String edit(final @CurrentUser CustomUserDetails authenticatedUser, final ProfileForm form, final Model model) {
        beanMapper.map(authenticatedUser.getAccount(), form);
        model.addAttribute(authenticatedUser.getAccount());
        return "profile/editForm";
    }

    @TransactionTokenCheck
    @PostMapping(params = "_method=put")
    public String save(final @CurrentUser CustomUserDetails authenticatedUser, final @Validated ProfileForm form, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return edit(authenticatedUser, form, model);
        }

        final Account inputAccount = beanMapper.map(form, Account.class);
        inputAccount.setAccountUuid(authenticatedUser.getAccount().getAccountUuid());

        final Account changedAccount;
        try {
            changedAccount = accountService.changeProfile(inputAccount);
        } catch (final DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return editRedo(authenticatedUser, model);
        } catch (final BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return editRedo(authenticatedUser, model);
        }

        beanMapper.map(changedAccount, authenticatedUser.getAccount());

        redirectAttributes.addFlashAttribute(Message.ACCOUNT_PROFILE_EDITED.resultMessages());

        return "redirect:/app/profile";
    }

    @GetMapping(path = "authenticationHistories")
    public String showAuthenticationHistoryList(final @CurrentUser CustomUserDetails authenticatedUser, final Model model) {
        final Account account = accountService.getAccount(authenticatedUser.getAccount().getAccountUuid());
        model.addAttribute(account);
        return "profile/authenticationHistoryList";
    }

    private String editRedo(final CustomUserDetails authenticatedUser, final Model model) {
        model.addAttribute(authenticatedUser.getAccount());
        return "profile/editForm";
    }

}
