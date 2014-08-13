package com.github.kazuki43zoo.app.account;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenContext;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.service.account.AccountService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.web.security.CurrentUser;

@TransactionTokenCheck("profile")
@RequestMapping("profile")
@Controller
public class ProfileController {

    @Inject
    AccountService accountService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String show(@CurrentUser CustomUserDetails currentUser, Model model) {
        model.addAttribute(currentUser.getAccount());
        return "profile/detail";
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET, params = "edit")
    public String edit(@CurrentUser CustomUserDetails currentUser, ProfileForm form, Model model) {
        beanMapper.map(currentUser.getAccount(), form);
        model.addAttribute(currentUser.getAccount());
        return "profile/editForm";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.PATCH)
    public String save(@CurrentUser CustomUserDetails currentUser, @Validated ProfileForm form,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
            TransactionTokenContext transactionTokenContext) {

        if (bindingResult.hasErrors()) {
            return edit(currentUser, form, model);
        }

        Account inputAccount = beanMapper.map(form, Account.class);
        inputAccount.setAccountUuid(currentUser.getAccount().getAccountUuid());

        Account changedAccount = null;
        try {
            changedAccount = accountService.changeProfile(inputAccount);
        } catch (DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return editRedo(currentUser, form, model);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return editRedo(currentUser, form, model);
        }

        beanMapper.map(changedAccount, currentUser.getAccount());

        transactionTokenContext.removeToken();

        redirectAttributes.addFlashAttribute(Message.ACCOUNT_PROFILE_EDITED.resultMessages());
        return "redirect:/profile";
    }

    public String editRedo(@CurrentUser CustomUserDetails currentUser, ProfileForm form, Model model) {
        model.addAttribute(currentUser.getAccount());
        return "profile/editForm";
    }

    @RequestMapping(value = "authenticationHistories", method = RequestMethod.GET)
    public String showAuthenticationHistoryList(@CurrentUser CustomUserDetails currentUser,
            Model model) {
        Account account = accountService.getAccount(currentUser.getAccount().getAccountUuid());
        model.addAttribute(account);
        return "profile/authenticationHistoryList";
    }
}
