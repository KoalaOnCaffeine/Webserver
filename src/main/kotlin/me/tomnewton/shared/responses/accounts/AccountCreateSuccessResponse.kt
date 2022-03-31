package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.Response

class AccountCreateSuccessResponse(accountID: Int) : Response(
    ACCOUNT_CREATE_SUCCESS, "Account created successfully", "{id: $accountID}"
)