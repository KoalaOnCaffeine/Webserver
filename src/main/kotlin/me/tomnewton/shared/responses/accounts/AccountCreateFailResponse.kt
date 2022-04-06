package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.ErrorResponse

class AccountCreateFailResponse(reason: String) : ErrorResponse(
    ACCOUNT_CREATE_FAIL, reason
)