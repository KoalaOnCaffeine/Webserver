package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.ErrorResponse

class AccountCreateFailResponse() : ErrorResponse(
    ACCOUNT_CREATE_FAIL, "The account was unable to be created. This is a server error."
)