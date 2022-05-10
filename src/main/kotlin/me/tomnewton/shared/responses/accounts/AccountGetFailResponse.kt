package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.ErrorResponse

class AccountGetFailResponse(reason: String) : ErrorResponse(
    ACCOUNT_GET_FAIL, reason
)