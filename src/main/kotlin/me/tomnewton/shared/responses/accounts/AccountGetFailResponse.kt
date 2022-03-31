package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.ErrorResponse

class AccountGetFailResponse() : ErrorResponse(
    ACCOUNT_GET_FAIL, "Either the account does not exist, or the server could not retrieve it"
)