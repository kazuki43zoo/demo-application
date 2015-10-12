package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthority;
import com.github.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;
import com.github.kazuki43zoo.domain.service.account.AccountService;
import org.dozer.Mapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.inject.Inject;

@TransactionTokenCheck("accounts")
@RequestMapping("accounts")
@Controller
public class AccountsController {

    @Inject
    Mapper beanMapper;

    @Inject
    AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(final @Validated AccountsSearchQuery query, final BindingResult bindingResult, final @PageableDefault(size = 15) Pageable pageable, final Model model) {
        if (bindingResult.hasErrors()) {
            return "account/list";
        }
        final AccountsSearchCriteria criteria = beanMapper.map(query, AccountsSearchCriteria.class);
        final Page<Account> page = accountService.searchAccounts(criteria, pageable);
        model.addAttribute("page", page);
        return "account/list";
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(path = "{accountUuid}", method = RequestMethod.GET)
    public String show(final @PathVariable("accountUuid") String accountUuid, final Model model) {
        final Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        return "account/detail";
    }

    @TransactionTokenCheck(value = "create", type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET, params = "form=create")
    public String createForm(final AccountForm form) {
        return "account/createForm";
    }

    @TransactionTokenCheck(value = "create")
    @RequestMapping(method = RequestMethod.POST)
    public String create(final @Validated AccountForm form, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return createForm(form);
        }

        final Account inputAccount = beanMapper.map(form, Account.class);
        for (final String authority : form.getAuthorities()) {
            inputAccount.addAuthority(new AccountAuthority(null, authority));
        }

        final Account createdAccount;
        try {
            createdAccount = accountService.create(inputAccount);
        } catch (final DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return createForm(form);
        } catch (final BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form);
        }

        return redirectDetailView(createdAccount.getAccountUuid(), Message.ACCOUNT_CREATED, redirectAttributes);
    }

    @TransactionTokenCheck(value = "edit", type = TransactionTokenType.BEGIN)
    @RequestMapping(path = "{accountUuid}", method = RequestMethod.GET, params = "form=edit")
    public String editForm(final @PathVariable("accountUuid") String accountUuid, final AccountForm form, final Model model) {

        final Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        beanMapper.map(account, form);
        for (final AccountAuthority accountAuthority : account.getAuthorities()) {
            form.addAuthority(accountAuthority.getAuthority());
        }
        form.setPassword(null);
        return "account/editForm";
    }

    @TransactionTokenCheck(value = "edit")
    @RequestMapping(path = "{accountUuid}", method = RequestMethod.POST, params = "_method=put")
    public String edit(final @PathVariable("accountUuid") String accountUuid, final @Validated AccountForm form, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return editRedo(accountUuid, model);
        }

        final Account inputAccount = beanMapper.map(form, Account.class);
        inputAccount.setAccountUuid(accountUuid);
        for (final String authority : form.getAuthorities()) {
            inputAccount.addAuthority(new AccountAuthority(accountUuid, authority));
        }

        try {
            accountService.change(inputAccount);
        } catch (final DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return editRedo(accountUuid, model);
        } catch (final BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return editRedo(accountUuid, model);
        }

        return redirectDetailView(accountUuid, Message.ACCOUNT_EDITED, redirectAttributes);
    }

    @TransactionTokenCheck
    @RequestMapping(path = "{accountUuid}", method = RequestMethod.POST, params = "_method=delete")
    public String delete(final @PathVariable("accountUuid") String accountUuid, final RedirectAttributes redirectAttributes) {

        accountService.delete(accountUuid);

        redirectAttributes.addFlashAttribute(Message.ACCOUNT_DELETED.resultMessages());
        return "redirect:/app/accounts";
    }

    @TransactionTokenCheck
    @RequestMapping(path = "{accountUuid}/unlock", method = RequestMethod.POST, params = "_method=patch")
    public String unlock(final @PathVariable("accountUuid") String accountUuid, final RedirectAttributes redirectAttributes) {

        accountService.unlock(accountUuid);

        return redirectDetailView(accountUuid, Message.ACCOUNT_UNLOCKED, redirectAttributes);
    }

    private String editRedo(final String accountUuid, final Model model) {
        final Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        return "account/editForm";
    }

    private String redirectDetailView(final String accountUuid, final Message message, final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(message.resultMessages());
        redirectAttributes.addAttribute("accountUuid", accountUuid);
        return "redirect:/app/accounts/{accountUuid}";
    }

}
