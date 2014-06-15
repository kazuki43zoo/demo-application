package com.github.kazuki43zoo.app.profile;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.service.account.AccountService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;

@TransactionTokenCheck("profile")
@RequestMapping("profile")
@Controller
public class ProfileController {

    @Inject
    AccountService accountService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String show(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute(user.getAccount());
        return "profile/view";
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET, params = "edit")
    public String edit(@AuthenticationPrincipal CustomUserDetails user, ProfileForm form,
            Model model) {
        beanMapper.map(user.getAccount(), form);
        form.setPassword(null);
        model.addAttribute(user.getAccount());
        return "profile/edit";
    }

    @TransactionTokenCheck
    @RequestMapping(method = RequestMethod.PATCH)
    public String save(@AuthenticationPrincipal CustomUserDetails user,
            @Validated ProfileForm form, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return edit(user, form, model);
        }

        Account inputAccount = beanMapper.map(form, Account.class);
        inputAccount.setAccountUuid(user.getAccount().getAccountUuid());
        Account savedAccount = accountService.changeProfile(inputAccount);

        beanMapper.map(savedAccount, user.getAccount());

        redirectAttributes.addFlashAttribute(ResultMessages.success().add("i.xx.pf.0001"));
        return "redirect:/profile";
    }
}
