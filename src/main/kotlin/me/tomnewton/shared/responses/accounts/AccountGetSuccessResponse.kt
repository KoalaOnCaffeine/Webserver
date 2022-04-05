package me.tomnewton.shared.responses.accounts

import me.tomnewton.database.model.Account
import me.tomnewton.shared.responses.Response

class AccountGetSuccessResponse(account: Account) : Response(
    ACCOUNT_GET_SUCCESS, data = account.toJsonObject()
)