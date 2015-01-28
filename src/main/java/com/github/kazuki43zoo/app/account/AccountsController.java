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
import org.terasoluna.gfw.web.token.transaction.TransactionTokenContext;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.inject.Inject;

@TransactionTokenCheck("accounts")
@RequestMapping("accounts")
@Controller
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountsController {

    @lombok.NonNull
    private final Mapper beanMapper;

    @lombok.NonNull
    private final AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@Validated AccountsSearchQuery query, BindingResult bindingResult,
                       @PageableDefault(size = 15) Pageable pageable, Model model) {
        if (bindingResult.hasErrors()) {
            return "account/list";
        }
        AccountsSearchCriteria criteria = beanMapper.map(query, AccountsSearchCriteria.class);
        Page<Account> page = accountService.searchAccounts(criteria, pageable);
        model.addAttribute("page", page);
        return "account/list";
    }

    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @RequestMapping(value = "{accountUuid}", method = RequestMethod.GET)
    public String show(@PathVariable("accountUuid") String accountUuid, Model model) {
        Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        return "account/detail";
    }

    @TransactionTokenCheck(value = "create", type = TransactionTokenType.BEGIN)
    @RequestMapping(method = RequestMethod.GET, params = "form=create")
    public String createForm(AccountForm form) {
        return "account/createForm";
    }

    @TransactionTokenCheck(value = "create")
    @RequestMapping(method = RequestMethod.POST)
    public String create(@Validated AccountForm form, BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {
        if (bindingResult.hasErrors()) {
            return createForm(form);
        }

        Account inputAccount = beanMapper.map(form, Account.class);
        for (String authority : form.getAuthorities()) {
            inputAccount.addAuthority(new AccountAuthority(null, authority));
        }
        Account createdAccount;
        try {
            createdAccount = accountService.create(inputAccount);
        } catch (DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return createForm(form);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form);
        }

        transactionTokenContext.removeToken();

        return redirectDetailView(redirectAttributes, createdAccount.getAccountUuid(),
                Message.ACCOUNT_CREATED);
    }

    @TransactionTokenCheck(value = "edit", type = TransactionTokenType.BEGIN)
    @RequestMapping(value = "{accountUuid}", method = RequestMethod.GET, params = "form=edit")
    public String editForm(@PathVariable("accountUuid") String accountUuid, AccountForm form,
                           Model model) {
        Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        beanMapper.map(account, form);
        for (AccountAuthority accountAuthority : account.getAuthorities()) {
            form.addAuthority(accountAuthority.getAuthority());
        }
        form.setPassword(null);
        return "account/editForm";
    }

    @TransactionTokenCheck(value = "edit")
    @RequestMapping(value = "{accountUuid}", method = RequestMethod.PUT)
    public String edit(@PathVariable("accountUuid") String accountUuid,
                       @Validated AccountForm form, BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {

        if (bindingResult.hasErrors()) {
            return editRedo(accountUuid, form, model);
        }

        Account inputAccount = beanMapper.map(form, Account.class);
        inputAccount.setAccountUuid(accountUuid);
        for (String authority : form.getAuthorities()) {
            inputAccount.addAuthority(new AccountAuthority(accountUuid, authority));
        }
        try {
            accountService.change(inputAccount);
        } catch (DuplicateKeyException e) {
            model.addAttribute(Message.ACCOUNT_ID_USED.resultMessages());
            return editRedo(accountUuid, form, model);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return editRedo(accountUuid, form, model);
        }

        transactionTokenContext.removeToken();

        return redirectDetailView(redirectAttributes, accountUuid, Message.ACCOUNT_EDITED);
    }

    @TransactionTokenCheck
    @RequestMapping(value = "{accountUuid}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("accountUuid") String accountUuid,
                         RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {

        accountService.delete(accountUuid);

        transactionTokenContext.removeToken();

        redirectAttributes.addFlashAttribute(Message.ACCOUNT_DELETED.resultMessages());
        return "redirect:/accounts";
    }

    @TransactionTokenCheck
    @RequestMapping(value = "{accountUuid}/unlock", method = RequestMethod.POST)
    public String unlock(@PathVariable("accountUuid") String accountUuid,
                         RedirectAttributes redirectAttributes, TransactionTokenContext transactionTokenContext) {

        accountService.unlock(accountUuid);

        transactionTokenContext.removeToken();

        return redirectDetailView(redirectAttributes, accountUuid, Message.ACCOUNT_UNLOCKED);
    }

    private String editRedo(String accountUuid, AccountForm form, Model model) {
        Account account = accountService.getAccount(accountUuid);
        model.addAttribute(account);
        return "account/editForm";
    }

    private String redirectDetailView(RedirectAttributes redirectAttributes, String accountUuid,
                                      Message message) {
        redirectAttributes.addFlashAttribute(message.resultMessages());
        redirectAttributes.addAttribute("accountUuid", accountUuid);
        return "redirect:/accounts/{accountUuid}";
    }

}
